/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.data;

public interface ColumnMapping
{
	String getDataSetColumn();

	void setDataSetColumn(String dataSetColumn);

	String getSourceColumn();

	void setSourceColumn(String sourceColumn);
}
