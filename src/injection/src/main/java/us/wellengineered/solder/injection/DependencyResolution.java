/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.Lifecycle;

/**
 * Provides a unified Strategy Pattern for dependency resolution.
 *
 * @param <TResolution> The resolution type of resolution.
 */
public interface DependencyResolution<TResolution> extends Lifecycle
{
	DependencyLifetime getDependencyLifetime();

	TResolution resolve(Class<? extends TResolution> resolutionClass, DependencyManager dependencyManager, String selectorKey);
}
