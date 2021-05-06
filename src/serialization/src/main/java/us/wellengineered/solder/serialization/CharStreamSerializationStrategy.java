/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

import java.io.Reader;
import java.io.Writer;

public interface CharStreamSerializationStrategy
{
	<TObject> TObject deserializeObjectFromCharStream(Class<? extends TObject> clazz, Reader inputReader) throws Exception;

	<TObject> void serializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> clazz, TObject obj) throws Exception;
}