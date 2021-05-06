/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

@FunctionalInterface
public interface ConvertorDelegate<Tin, Tout>
{
	Tout invoke(long index, Tin item) throws Exception;

	default Tout invoke(Tin item) throws Exception
	{
		return this.invoke(-1L, item);
	}
}
