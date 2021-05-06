/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

import us.wellengineered.solder.primitives.Lifecycle;

public interface DbConnection extends Lifecycle
{
	String getConnectionString();

	void setConnectionString(String connectionString);

	int getConnectionTimeout();

	String getDatabase();

	ConnectionState getConnectionState();

	DbTransaction beginTransaction();

	DbTransaction beginTransaction(IsolationLevel il);

	void changeDatabase(String databaseName);

	void close();

	DbCommand createCommand();

	void open();
}
