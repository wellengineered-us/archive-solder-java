/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public enum ParameterDirection
{
	UNKNOWN(0),

	IN(1),

	OUT(2),

	IN_OUT(1 | 2);

	ParameterDirection(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
