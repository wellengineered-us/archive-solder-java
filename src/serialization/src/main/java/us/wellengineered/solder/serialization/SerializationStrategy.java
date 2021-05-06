/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

public interface SerializationStrategy extends FileSerializationStrategy,
		BytesSerializationStrategy, StringSerializationStrategy,
		ByteStreamSerializationStrategy, CharStreamSerializationStrategy
{
}