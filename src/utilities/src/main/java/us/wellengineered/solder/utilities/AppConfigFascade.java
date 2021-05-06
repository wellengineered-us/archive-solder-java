/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

public interface AppConfigFascade
{
	<TValue> TValue getAppSetting(Class<? extends TValue> valueClass, String key) throws AppConfigException;

	boolean hasAppSetting(String key);
}
