/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.MethodParameterModifier;

public final class AppConfigFascadeImpl implements AppConfigFascade
{
	public AppConfigFascadeImpl(DataTypeFascade dataTypeFascade)
	{
		if (dataTypeFascade == null)
			throw new ArgumentNullException("dataTypeFascade");

		this.dataTypeFascade = dataTypeFascade;
	}

	private final DataTypeFascade dataTypeFascade;

	private DataTypeFascade getDataTypeFascade()
	{
		return this.dataTypeFascade;
	}

	@Override
	public <TValue> TValue getAppSetting(Class<? extends TValue> valueClass, String key) throws AppConfigException
	{
		String svalue;
		TValue ovalue;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		if (key == null)
			throw new ArgumentNullException("key");

		svalue = System.getProperty(key);

		if (svalue == null)
			throw new AppConfigException(String.format("Key '%s' was not found in property container.", key));

		final MethodParameterModifier.Out<TValue> outValue = new MethodParameterModifier.Out<>();

		if (!this.getDataTypeFascade().tryParse(valueClass, svalue, outValue))
			throw new AppConfigException(String.format("Property key '%s' value '%s' is not a valid '%s'.", key, svalue, valueClass.getName()));

		ovalue = outValue.getValue();

		return ovalue;
	}

	@Override
	public boolean hasAppSetting(String key)
	{
		String value;

		if (key == null)
			throw new ArgumentNullException("key");

		value = System.getProperty(key);

		return value != null;
	}
}
