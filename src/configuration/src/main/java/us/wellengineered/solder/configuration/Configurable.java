/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

public interface Configurable<TConfiguration extends ConfigurationObject>
{
	TConfiguration getConfiguration();

	void setConfiguration(TConfiguration configuration);
}
