/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.sysdata;

public enum CommandType
{
	UNKNOWN(0),

	TEXT(1),

	STORED_PROCEDURE(4),

	TABLE_DIRECT(512);

	CommandType(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
