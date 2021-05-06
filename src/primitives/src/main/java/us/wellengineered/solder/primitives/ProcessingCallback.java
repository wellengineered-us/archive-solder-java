/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

@FunctionalInterface
public interface ProcessingCallback
{
	void onProgress(long punctuateModulo, String sourceLabel, long itemIndex, boolean processCompleted, double rollingTiming);
}
