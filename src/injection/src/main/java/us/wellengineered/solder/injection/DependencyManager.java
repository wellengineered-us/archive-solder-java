/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.Lifecycle;

/**
 * Provides dependency registration and resolution services.
 */
public interface DependencyManager extends Lifecycle
{
	<TResolution> void addResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes, DependencyResolution<? extends TResolution> dependencyResolution);

	boolean clearAllResolutions();

	<TResolution> boolean clearResolutions(Class<? extends TResolution> resolutionClass, boolean includeAssignableTypes);

	<TResolution> boolean hasResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes);

	<TResolution> void removeResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes);

	<TResolution> TResolution resolveDependency(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes);
}
