/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.sysdata;

public enum IsolationLevel
{
	UNSPECIFIED(-1),

	CHAOS(16),

	READ_COMMITTED(4096),

	READ_UNCOMMITTED(256),

	REPEATABLE_READ(65536),

	SERIALIZABLE(1048576),

	SNAPSHOT(16777216);

	IsolationLevel(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
