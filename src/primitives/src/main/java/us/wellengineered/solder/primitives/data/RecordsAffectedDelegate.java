/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

@FunctionalInterface
public interface RecordsAffectedDelegate
{
	void invoke(int recordsAffected);
}
