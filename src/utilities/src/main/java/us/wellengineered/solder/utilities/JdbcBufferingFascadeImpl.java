/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.data.*;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.NotImplementedException;

import java.sql.Driver;
import java.util.Map;

public final class JdbcBufferingFascadeImpl implements JdbcBufferingFascade
{
	public JdbcBufferingFascadeImpl(DataTypeFascade dataTypeFascade)
	{
		if (dataTypeFascade == null)
			throw new ArgumentNullException("dataTypeFascade");

		this.dataTypeFascade = dataTypeFascade;
	}

	private final DataTypeFascade dataTypeFascade;

	private DataTypeFascade getDataTypeFascade()
	{
		return this.dataTypeFascade;
	}

	@Override
	public DbParameter createParameter(Class<? extends Driver> driverClass, String sourceColumn, ParameterDirection parameterDirection, DbType parameterType, int parameterSize, byte parameterPrecision, byte parameterScale, boolean parameterIsNullable, String parameterName, Object parameterValue)
	{
		DbParameterImpl dbParameter;

		if (driverClass == null)
			throw new ArgumentNullException("driverClass");

		dbParameter = new DbParameterImpl();

		dbParameter.setParameterName(parameterName);
		dbParameter.setSize(parameterSize);
		dbParameter.setValue(parameterValue);
		dbParameter.setDirection(parameterDirection);
		dbParameter.setDbType(parameterType);
		dbParameter.setNullable(parameterIsNullable);
		dbParameter.setPrecision(parameterPrecision);
		dbParameter.setScale(parameterScale);
		dbParameter.setSourceColumn(sourceColumn);

		return dbParameter;
	}

	@Override
	public Iterable<Map<String, Object>> executeRecords(boolean schemaOnly, Class<? extends Driver> driverClass, String connectionUrl, boolean transactional, IsolationLevel isolationLevel, CommandType commandType, String commandText, Iterable<DbParameter> commandParameters, RecordsAffectedDelegate recordsAffectedDelegate) throws Exception
	{
		/*Driver driver;
		Savepoint savepoint;
		Properties properties;

		List<Map<String, Object>> rows;
		CommandBehavior commandBehavior;
		final boolean COMMAND_PREPARE = false;
		final boolean OPEN = true;
		final Integer COMMAND_TIMEOUT = null;
		long resultIndex = -1;

		if (driverClass == null)
			throw new ArgumentNullException("driverClass");

		if (connectionUrl == null)
			throw new ArgumentNullException("connectionUrl");

		driver = Utils.newObjectFromClass(driverClass);
		properties = new Properties();

		try (Connection connection = driver.connect(connectionUrl, properties))
		{
			failFastOnlyWhen(connection == null, "connection == null");

			try
			{
				Map<String, Object> row;
				String key;
				Object value;

				connection.setAutoCommit(!transactional);

				if (transactional)
					savepoint = connection.setSavepoint();
				else
					savepoint = null;

				rows = new ArrayList<>();

				commandBehavior = schemaOnly ? CommandBehavior.SCHEMA_ONLY : CommandBehavior.DEFAULT;

				final AutoCloseable dbDataReader = null;

				try(dbDataReader)
				{
					if (!schemaOnly)
					{
						do
						{
							if (recordsAffectedDelegate != null)
								recordsAffectedDelegate.invoke((int) ++resultIndex);

							while (dbDataReader.Read())
							{
								row = new __Row(StringComparer.OrdinalIgnoreCase);

								for (long fieldIndex = 0; fieldIndex < dbDataReader.FieldCount; fieldIndex++)
								{
									key = dbDataReader.GetName(fieldIndex);
									value = dbDataReader.GetValue(fieldIndex);
									value = this.DataTypeFascade.ChangeType < object > (value);

									if (row.ContainsKey(key) || (key ? ? string.Empty).Length == 0)
									key = String.format("Field_{0:0000}", fieldIndex);

									row.Add(key, value);
								}

								rows.Add(row);
							}
						}
						while (dbDataReader.NextResult());
					}
					else
					{
						if (!dbDataReader.CanGetColumnSchema())
							throw new NotSupportedException(string.Format("The connection command type '{0}' does not support schema access.", dbDataReader.GetType().FullName));

						dbColumns = dbDataReader.GetColumnSchema();
						{
							if ((object) dbColumns != null)
							{
								for (long recordIndex = 0; recordIndex < dbColumns.Count; recordIndex++)
								{
									dbColumn = dbColumns[(int) recordIndex];

									propertyInfos = dbColumn.GetType().GetProperties(BindingFlags.Public | BindingFlags.Instance);

									row = new __Row(StringComparer.OrdinalIgnoreCase);
									row.Add(string.Empty, dbColumn);

									if ((object) propertyInfos != null)
									{
										for (long fieldIndex = 0; fieldIndex < propertyInfos.Length; fieldIndex++)
										{
											propertyInfo = propertyInfos[fieldIndex];

											if (propertyInfo.GetIndexParameters().Any())
												continue;

											key = propertyInfo.Name;
											value = propertyInfo.GetValue(dbColumn);
											value = this.DataTypeFascade.ChangeType < object > (value);

											row.Add(key, value);
										}
									}
								}
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				if (connection != null && savepoint != null)
				{
					connection.rollback(savepoint);
				}

				throw ex;
			}
			finally
			{
				if (connection != null && savepoint != null)
				{
					connection.commit();
				}
			}

			return rows;
		}*/

		throw new NotImplementedException();
	}
}
