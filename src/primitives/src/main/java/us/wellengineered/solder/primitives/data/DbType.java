/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public enum DbType
{
	ANSI_STRING(0),

	BINARY(1),

	BYTE(2),

	BOOLEAN(3),

	CURRENCY(4),

	DATE(5),

	DATE_TIME(6),

	DECIMAL(7),

	DOUBLE(8),

	GUID(9),

	INT16(10),

	INT32(11),

	INT64(12),

	OBJECT(13),

	SBYTE(14),

	SINGLE(15),

	STRING(16),

	TIME(17),

	UINT16(18),

	UINT32(19),

	UINT64(20),

	VAR_NUMERIC(21),

	ANSI_STRING_FIXED_LENGTH(22),

	STRING_FIXED_LENGTH(23),

	XML(25),

	DATE_TIME2(26),

	DATE_TIME_OFFSET(27);

	DbType(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
