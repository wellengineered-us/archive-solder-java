/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.primitives.exceptions.ArgumentOutOfRangeException;

public final class DefaultContextualStorageFactory implements ContextualStorageFactory
{
	public DefaultContextualStorageFactory(ContextScope contextScope)
	{
		this.contextScope = contextScope;
	}

	private final ContextScope contextScope;

	public ContextScope getContextScope()
	{
		return this.contextScope;
	}

	@Override
	public ContextualStorageStrategy getContextualStorage()
	{
		switch (this.getContextScope())
		{
			case GLOBAL_STATIC_UNSAFE:
				return new GlobalStaticContextualStorageStrategy();
			case LOCAL_THREAD_SAFE:
				return new ThreadLocalContextualStorageStrategy();
			case LOCAL_ASYNC_SAFE:
				return new AsyncLocalContextualStorageStrategy();
			case LOCAL_REQUEST_SAFE:
				return new RequestLocalContextualStorageStrategy();
			default:
				throw new ArgumentOutOfRangeException();
		}
	}
}
