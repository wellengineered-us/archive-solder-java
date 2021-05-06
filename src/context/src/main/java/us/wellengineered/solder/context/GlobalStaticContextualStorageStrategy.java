/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GlobalStaticContextualStorageStrategy implements ContextualStorageStrategy
{
	public GlobalStaticContextualStorageStrategy()
	{
		this(LazySingleton.get());
	}

	public GlobalStaticContextualStorageStrategy(Map<String, Object> context)
	{
		if (context == null)
			throw new ArgumentNullException("context");

		this.context = context;
	}

	private final Map<String, Object> context;

	private Map<String, Object> getContext()
	{
		return this.context;
	}

	/**
	 * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 */
	private static class LazySingleton
	{
		static
		{
		}

		private static final Map<String, Object> __ = new LinkedHashMap<>();

		public static Map<String, Object> get()
		{
			return __;
		}
	}

	public void resetValues()
	{
		this.getContext().clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <TValue> TValue getValue(String key)
	{
		Object value;
		value = this.getContext().get(key);
		return (TValue) value;
	}

	@Override
	public boolean hasValue(String key)
	{
		return this.getContext().containsKey(key);
	}

	@Override
	public void removeValue(String key)
	{
		if (this.getContext().containsKey(key))
			this.getContext().remove(key);
	}

	@Override
	public <TValue> void setValue(String key, TValue value)
	{
		this.removeValue(key);
		this.getContext().put(key, value);
	}
}
