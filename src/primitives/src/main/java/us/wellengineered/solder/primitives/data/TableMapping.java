/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public interface TableMapping
{
	ColumnMappingCollection getColumnMappings();

	String getDataSetTable();

	void setDataSetTable(String dataSetTable);

	String getSourceTable();

	void setSourceTable(String sourceTable);
}
