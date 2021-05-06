/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.primitives.exceptions.FailFastException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;

public final class Utils
{
	public static final String EMPTY_STRING = "";

	public static <TOp> TOp failFastOnlyWhen(boolean conditional, String label) throws FailFastException
	{
		if (conditional)
		{
			final String message = String.format("Fail-fast assertion failed: '%s'", label);
			System.err.println(message);
			throw new FailFastException(message); // no catching this
		}

		return nop();
	}

	public static <TOp> TOp failFastWithException(Exception ex)
	{
		ex.printStackTrace();
		System.exit(-2);
		return nop();
	}

	public static String formatCallerInfo()
	{
		return formatCallerInfo(1 + 1);
	}

	public static String formatCallerInfo(int skipFrames)
	{
		final int SKIP_FRAMES = skipFrames + 1;
		final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		final StackWalker.StackFrame stackFrame = stackWalker.walk(frames -> frames.skip(SKIP_FRAMES).findFirst()).get();

		return String.format("%s::%s(),%s", stackFrame.getClassName(), stackFrame.getMethodName(), formatCurrentThreadId());
	}

	public static String formatCurrentThreadId()
	{
		return String.format("%05X", Thread.currentThread().getId());
	}

	public static String formatObjectInfo(Object obj)
	{
		if (obj == null)
			throw new ArgumentNullException("obj");

		return String.format("%s@%05X", obj.getClass().getName(), System.identityHashCode(obj));
	}

	public static String formatUUID(UUID uuid)
	{
		if (uuid == null)
			throw new ArgumentNullException("uuid");

		return uuid.toString().replace("-", "").toUpperCase();
	}

	public static <TValue> TValue getValueOrDefault(TValue value, final TValue defaultValue)
	{
		value = value == null ? defaultValue : value;
		return value;
	}

	public static boolean hitWithBrick(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Closeable)
			{
				((Closeable) obj).close();
				result = true;
			}
			else if (obj instanceof AutoCloseable)
			{
				((AutoCloseable) obj).close();
				result = true;
			}
		}

		return result;
	}

	public static boolean isNullOrEmptyString(String value)
	{
		return value == null || value.isEmpty();
	}

	public static <TSubclass> Class<? extends TSubclass> loadClassByName(String className, Class<TSubclass> subClazz)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (subClazz == null)
			throw new ArgumentNullException("subClazz");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return Class.forName(className).asSubclass(subClazz);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <TObject> Class<? extends TObject> loadClassByName(String className)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return (Class<? extends TObject>) Class.forName(className);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	public static boolean maybeCreate(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Creatable)
			{
				((Creatable) obj).create();
				result = true;
			}
		}

		return result;
	}

	public static boolean maybeDispose(Object obj) throws Exception
	{
		boolean result = false;
		if (obj != null)
		{
			if (obj instanceof Disposable)
			{
				((Disposable) obj).dispose();
				result = true;
			}
		}

		return result;
	}

	public static <TObject> TObject newObjectFromClass(Class<? extends TObject> clazz)
	{
		if (clazz == null)
			throw new ArgumentNullException("clazz");

		try
		{
			return clazz.getConstructor().newInstance();
		}
		catch (NoSuchMethodException ex)
		{
			return null;
		}
		catch (IllegalAccessException ex)
		{
			return null;
		}
		catch (InvocationTargetException ex)
		{
			return null;
		}
		catch (InstantiationException ex)
		{
			return null;
		}
	}

	public static <TOp> TOp nop()
	{
		return null;
	}

	public static void safeDispose(Disposable disposable)
	{
		try
		{
			if (disposable != null)
				disposable.dispose();
		}
		catch (Exception ex)
		{
			failFastWithException(ex);
		}
	}

	public static long streamCopy(InputStream source, OutputStream destination) throws IOException
	{
		if (source == null)
			throw new ArgumentNullException("source");

		if (destination == null)
			throw new ArgumentNullException("destination");

		final int BUFFER_SIZE = 4096;
		final byte[] buffer = new byte[BUFFER_SIZE];
		long total = 0L;

		while (true)
		{
			final int b = source.read(buffer);

			if (b == -1)
				break;

			destination.write(buffer, 0, b);
			total += b;
		}

		return total;
	}

	public static String toStringSafe(Object value)
	{
		return value == null ? EMPTY_STRING : value.toString();
	}

	public static class MapExtensionMethods
	{
		public static <K, V> boolean tryMapGetValue(Map<K, V> map, K key, MethodParameterModifier.Out<V> outValue)
		{
			boolean result;
			V value;

			if (map == null)
				throw new ArgumentNullException("map");

			if (outValue == null)
				throw new ArgumentNullException("outValue");

			if (!map.containsKey(key))
			{
				value = null;
				result = false;
			}
			else
			{
				value = map.get(key);
				result = true;
			}

			outValue.setValue(value);
			return result;
		}

		public static <K, V> boolean tryMapGetValueOrPutDefault(Map<K, V> map, K key, MethodParameterModifier.Ref<V> refValue)
		{
			boolean result;
			V value;

			if (map == null)
				throw new ArgumentNullException("map");

			if (refValue == null)
				throw new ArgumentNullException("refValue");

			if (!map.containsKey(key))
			{
				map.put(key, (value = refValue.getValue()));
				result = false;
			}
			else
			{
				value = map.get(key);
				result = true;
			}

			refValue.setValue(value);
			return result;
		}

	/*public static <K, V> boolean tryPutValue(Map<K, V> thisMap, K key, V value)
	{
		if (thisMap == null)
			throw new ArgumentNullException("thisMap");

		if(thisMap.containsKey(key))
			return false;

		thisMap.put(key, value);
		return true;
	}*/
	}
}
