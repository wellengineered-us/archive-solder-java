/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.sysdata;

public interface DataParameter
{
	DbType getDbType();

	void setDbType(DbType dbType);

	ParameterDirection getDirection();

	void setParameterDirection(ParameterDirection parameterDirection);

	String getParameterName();

	void setParameterName(String parameterName);

	int getParameterOrdinal();

	void setParameterOrdinal(int parameterOrdinal);

	String getSourceColumn();

	void setSourceColumn(String sourceColumn);

	Object getSourceVersion();

	void setSourceVersion(Object sourceVersion);

	Object getValue();

	void setValue(Object value);

	boolean isNullable();
}
