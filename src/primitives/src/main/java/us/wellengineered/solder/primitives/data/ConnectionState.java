/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public enum ConnectionState
{
	CLOSED(0),

	OPEN(1),

	CONNECTING(2),

	EXECUTING(4),

	FETCHING(8),

	BROKEN(16);

	ConnectionState(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
