/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

public class ExecutableArgumentImpl<TValue> implements ExecutableArgument<TValue>
{
	public ExecutableArgumentImpl(String name, boolean required, boolean bounded, Class<? extends TValue> valueClass, TValue defaultValue)
	{
		if (name == null)
			throw new ArgumentNullException("name");

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		this.name = name;
		this.required = required;
		this.bounded = bounded;
		this.valueClass = valueClass;
		this.defaultValue = defaultValue;
	}

	private final String name;
	private final boolean required;
	private final boolean bounded;
	private final Class<? extends TValue> valueClass;
	private final TValue defaultValue;

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean isBounded()
	{
		return this.bounded;
	}

	@Override
	public boolean isRequired()
	{
		return this.required;
	}

	@Override
	public Class<? extends TValue> getValueClass()
	{
		return this.valueClass;
	}

	@Override
	public TValue getDefaultValue()
	{
		return this.defaultValue;
	}
}
