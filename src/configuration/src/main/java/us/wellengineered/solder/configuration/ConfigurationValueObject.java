/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

public interface ConfigurationValueObject<TValue> extends ConfigurationObject
{
	String getName();

	void setName(String name);

	TValue getValue();

	void setValue(TValue value);
}
