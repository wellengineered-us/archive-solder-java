/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

public interface StringSerializationStrategy
{
	<TObject> TObject deserializeObjectFromString(Class<? extends TObject> clazz, String inputString) throws Exception;

	<TObject> String serializeObjectToString(/*String outputString,*/ Class<? extends TObject> clazz, TObject obj) throws Exception;
}