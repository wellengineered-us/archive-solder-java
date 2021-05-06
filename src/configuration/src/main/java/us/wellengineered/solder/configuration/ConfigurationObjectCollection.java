/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import us.wellengineered.solder.primitives.Message;

import java.util.Collection;


/**
 * Represents an configuration object collection.
 *
 * @param <TItemConfigurationObject> The contained configuration object class.
 */
public interface ConfigurationObjectCollection<TItemConfigurationObject extends ConfigurationObject> extends Collection<TItemConfigurationObject>
{
	/**
	 * Gets the site configuration object or null if this instance is unattached.
	 *
	 * @return a site configuration object or null if this instance is unattached.
	 */
	ConfigurationObject getSite();

	Iterable<Message> validateAll();

	Iterable<Message> validateAll(Object context);
}
