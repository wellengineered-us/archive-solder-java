/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.sysdata;

public enum ParameterDirection
{
	UNKNOWN(0),

	INPUT(1),

	OUTPUT(2),

	INPUT_OUTPUT(3),

	RETURN_VALUE(6);

	ParameterDirection(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
