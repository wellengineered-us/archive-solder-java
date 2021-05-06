/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public interface DbParameter extends DataParameter
{
	DbType getDbType();

	ParameterDirection getDirection();

	String getParameterName();

	Integer getParameterOrdinal();

	String getSourceColumn();

	Object getValue();

	void setValue(Object value);

	Boolean isNullable();
}
