/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public enum StatementType
{
	SELECT(0),

	INSERT(1),

	UPDATE(2),

	DELETE(3),

	BATCH(6);

	StatementType(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
