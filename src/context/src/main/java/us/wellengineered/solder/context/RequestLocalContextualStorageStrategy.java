/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

public final class RequestLocalContextualStorageStrategy implements ContextualStorageStrategy
{
	public RequestLocalContextualStorageStrategy()
	{
	}

	@Override
	public <TValue> TValue getValue(String key)
	{
		return null;
	}

	@Override
	public boolean hasValue(String key)
	{
		return false;
	}

	@Override
	public void removeValue(String key)
	{

	}

	@Override
	public <TValue> void setValue(String key, TValue tValue)
	{

	}
}
