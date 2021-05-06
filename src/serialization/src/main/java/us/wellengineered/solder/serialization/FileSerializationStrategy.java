/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

public interface FileSerializationStrategy
{
	<TObject> TObject deserializeObjectFromFile(Class<? extends TObject> clazz, String inputFilePath) throws Exception;

	<TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> clazz, TObject obj) throws Exception;
}