/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

import us.wellengineered.solder.primitives.Lifecycle;

public interface DbTransaction extends Lifecycle
{
	DbConnection getConnection();

	IsolationLevel getIsolationLevel();

	void commit();

	void rollback();
}
