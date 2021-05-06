/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.data.*;

import java.sql.Driver;
import java.util.Map;

public interface JdbcBufferingFascade
{
	DbParameter createParameter(Class<? extends Driver> driverClass, String sourceColumn, ParameterDirection parameterDirection, DbType parameterType, int parameterSize, byte parameterPrecision, byte parameterScale, boolean parameterIsNullable, String parameterName, Object parameterValue);

	Iterable<Map<String, Object>> executeRecords(boolean schemaOnly, Class<? extends Driver> driverClass, String connectionUrl, boolean transactional, IsolationLevel isolationLevel, CommandType commandType, String commandText, Iterable<DbParameter> commandParameters, RecordsAffectedDelegate recordsAffectedDelegate) throws Exception;
}
