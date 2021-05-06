/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public interface DataParameter
{
	Byte getPrecision();

	Byte getScale();

	Integer getSize();
}
