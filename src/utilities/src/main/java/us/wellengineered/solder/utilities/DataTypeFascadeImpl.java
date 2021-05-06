/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.Utils;

import java.lang.module.ModuleDescriptor;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Formattable;
import java.util.UUID;

public final class DataTypeFascadeImpl implements DataTypeFascade
{
	public DataTypeFascadeImpl()
	{
	}

	@Override
	public <TValue> TValue changeClass(Class<? extends TValue> valueClass, Object value)
	{
		return (TValue) value;
	}

	@Override
	public <TValue> TValue defaultValue(Class<? extends TValue> valueClass)
	{
		TValue value;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		value = valueClass.isPrimitive() ? Utils.newObjectFromClass(valueClass) : null;

		return value;
	}

	@Override
	public Object inferDbTypeFromClass(Class<?> valueClass)
	{
		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		return null;
	}

	@Override
	public boolean isNullOrEmpty(String value)
	{
		return value == null || value.length() <= 0;
	}

	@Override
	public boolean isNullOrWhiteSpace(String value)
	{
		return value == null || this.isWhiteSpace(value);
	}

	@Override
	public boolean isWhiteSpace(String value)
	{
		if (value == null)
			return false;

		for (int i = 0; i < value.length(); i++)
		{
			if (!Character.isSpaceChar(value.charAt(i)))
				return false;
		}

		return true;
	}

	@Override
	public boolean objectsEqualValueSemantics(Object objA, Object objB)
	{
		if ((objA != null && objB == null) ||
				(objA == null && objB != null))
			return false; // prevent null coalescence

		return (objA != null ? objA.equals(objB) : (objB != null ? objB.equals(objA) : true /* both null */));
	}

	@Override
	public <TValue> String safeToString(TValue value, String format, String defaultValue)
	{
		String retval;

		if (value == null)
			return defaultValue;

		if (value instanceof Formattable)
			retval = String.format(format, (Formattable) value);
		else
			retval = value.toString();

		if (this.isNullOrWhiteSpace(retval))
			retval = defaultValue;

		return retval;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <TValue> boolean tryParse(Class<? extends TValue> valueClass, String value, MethodParameterModifier.Out<TValue> outResult)
	{
		boolean retval;
		boolean nonNullable;
		TValue result;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		if (outResult == null)
			throw new ArgumentNullException("outResult");

		if (valueClass == String.class)
		{
			retval = true;
			result = (TValue) value;
		}
		else if ((nonNullable = valueClass == boolean.class)
				|| valueClass == Boolean.class)
		{
			Boolean zresult;
			zresult = Boolean.parseBoolean(value);
			retval = true; // OK, for now
			result = (TValue) (nonNullable ? zresult /*.booleanValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
		}
		else if ((nonNullable = valueClass == char.class)
				|| valueClass == Character.class)
		{
			Character zresult;
			zresult = (retval = !this.isNullOrEmpty(value)) ? value.charAt(0) : null;
			result = (TValue) (nonNullable ? zresult /*.charValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
		}
		else if ((nonNullable = valueClass == byte.class)
				|| valueClass == Byte.class)
		{
			Byte zresult;

			try
			{
				zresult = Byte.parseByte(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.intValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == short.class)
				|| valueClass == Short.class)
		{
			Short zresult;

			try
			{
				zresult = Short.parseShort(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.intValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == int.class)
				|| valueClass == Integer.class)
		{
			Integer zresult;

			try
			{
				zresult = Integer.parseInt(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.intValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == long.class)
				|| valueClass == Long.class)
		{
			Long zresult;

			try
			{
				zresult = Long.parseLong(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.longValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == float.class)
				|| valueClass == Float.class)
		{
			Float zresult;

			try
			{
				zresult = Float.parseFloat(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.intValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == double.class)
				|| valueClass == Double.class)
		{
			Double zresult;

			try
			{
				zresult = Double.parseDouble(value);
				retval = true;
				result = (TValue) (nonNullable ? zresult /*.intValue()*/ : zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == UUID.class)
		{
			UUID zresult;

			try
			{
				zresult = UUID.fromString(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == Instant.class)
		{
			Instant zresult;

			try
			{
				zresult = Instant.parse(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (DateTimeParseException dtpex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == ModuleDescriptor.Version.class)
		{
			ModuleDescriptor.Version zresult;

			try
			{
				zresult = ModuleDescriptor.Version.parse(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass.isEnum()) // special case
		{
			Enum<?> zresult;

			try
			{
				zresult = Enum.valueOf((Class<? extends Enum>) valueClass, value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else
			throw new ArgumentOutOfRangeException("valueClass", String.format("The value type '%s' is not supported.", valueClass.getName()));

		outResult.setValue(result);
		return retval;
	}
}
