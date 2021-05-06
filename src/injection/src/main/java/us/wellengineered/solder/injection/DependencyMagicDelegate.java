/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

@FunctionalInterface
public interface DependencyMagicDelegate
{
	void invoke(DependencyManager dependencyManager) throws Exception;
}
