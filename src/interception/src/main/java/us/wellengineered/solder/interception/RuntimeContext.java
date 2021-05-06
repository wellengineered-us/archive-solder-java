/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

public interface RuntimeContext
{
	boolean shouldContinueInterception();

	int getInterceptionCount();

	int getInterceptionIndex();

	void abortInterceptionChain();
}
