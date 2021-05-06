/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

public enum DependencyLifetime
{
	/**
	 * Unknown or undefined dependency lifetime.
	 */
	UNKNOWN(0),

	/**
	 * Transient lifetime dependencies are created each time they are requested.
	 */
	TRANSIENT(1),

	/**
	 * Scoped lifetime dependencies are created once per "request".
	 */
	SCOPED(2),

	/**
	 * Singleton lifetime dependencies are created the first time they are requested,
	 * and then every subsequent request will use the same instance. If your application requires singleton behavior,
	 * allowing the dependency manager to manage the dependency’s lifetime is recommended instead of implementing the
	 * singleton design pattern and managing your object’s lifetime in the class yourself.
	 */
	SINGLETON(3),

	/**
	 * ReflectionFascadeLegacyInstance lifetime dependencies are those for which you can choose to add an instance
	 * directly to the dependency manager. If you do so, this instance will be used for all subsequent requests
	 * (this technique will create a Singleton-scoped instance). One key difference between ReflectionFascadeLegacyInstance
	 * dependencies and Singleton dependencies is that the ReflectionFascadeLegacyInstance dependencies is
	 * created in "your code", while the Singleton service is lazy-loaded the first time it is requested.
	 */
	INSTANCE(4);

	DependencyLifetime(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
