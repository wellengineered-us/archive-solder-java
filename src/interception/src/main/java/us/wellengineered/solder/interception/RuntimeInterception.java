/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.Lifecycle;

public interface RuntimeInterception extends Lifecycle
{
	void invoke(RuntimeInvocation runtimeInvocation, RuntimeContext runtimeContext) throws Exception;
}
