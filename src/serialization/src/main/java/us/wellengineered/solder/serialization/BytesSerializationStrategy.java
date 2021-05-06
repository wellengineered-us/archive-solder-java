/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

public interface BytesSerializationStrategy
{
	<TObject> TObject deserializeObjectFromBytes(Class<? extends TObject> clazz, byte[] inputBytes) throws Exception;

	<TObject> byte[] serializeObjectToBytes(/*byte[] outputBytes,*/ Class<? extends TObject> clazz, TObject obj) throws Exception;
}