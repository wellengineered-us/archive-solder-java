/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

public interface ContextualStorageStrategy
{
	<TValue> TValue getValue(String key);

	boolean hasValue(String key);

	void removeValue(String key);

	<TValue> void setValue(String key, TValue value);
}
