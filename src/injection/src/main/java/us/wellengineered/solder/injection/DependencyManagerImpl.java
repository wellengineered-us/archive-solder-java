/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.AbstractLifecycle;

public final class DependencyManagerImpl extends AbstractLifecycle<Exception, Exception> implements DependencyManager
{
	public DependencyManagerImpl()
	{
	}

	@Override
	protected void create(boolean creating) throws Exception
	{

	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{

	}

	@Override
	public <TResolution> void addResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes, DependencyResolution<? extends TResolution> dependencyResolution)
	{

	}

	@Override
	public boolean clearAllResolutions()
	{
		return false;
	}

	@Override
	public <TResolution> boolean clearResolutions(Class<? extends TResolution> resolutionClass, boolean includeAssignableTypes)
	{
		return false;
	}

	@Override
	public <TResolution> boolean hasResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes)
	{
		return false;
	}

	@Override
	public <TResolution> void removeResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes)
	{

	}

	@Override
	public <TResolution> TResolution resolveDependency(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes)
	{
		return null;
	}
}
