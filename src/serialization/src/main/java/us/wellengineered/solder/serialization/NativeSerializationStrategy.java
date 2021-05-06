/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

public interface NativeSerializationStrategy<TNativeInput, TNativeOutput>
{
	<TObject> TObject deserializeObjectFromNative(Class<? extends TObject> clazz, TNativeInput nativeInput) throws Exception;

	<TObject> void serializeObjectToNative(TNativeOutput nativeOutput, Class<? extends TObject> clazz, TObject obj) throws Exception;
}