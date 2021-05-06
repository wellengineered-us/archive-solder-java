/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

import us.wellengineered.solder.primitives.Lifecycle;

public interface ExecutableApplicationFascade extends Lifecycle
{
	int entryPoint(String[] args);
}
