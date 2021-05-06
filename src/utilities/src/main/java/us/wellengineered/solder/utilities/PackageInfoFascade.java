/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

/**
 * Provides a mechanism to get package metadata.
 */
public interface PackageInfoFascade
{
	/**
	 * Get the package company.
	 *
	 * @return Return the package company as a string.
	 */
	String getCompany();

	/**
	 * Get the package configuration.
	 *
	 * @return Return the package configuration as a string.
	 */
	String getConfiguration();

	/**
	 * Get the package copyright.
	 *
	 * @return Return the package copyright as a string.
	 */
	String getCopyright();

	/**
	 * Get the package description.
	 *
	 * @return Return the package description as a string.
	 */
	String getDescription();

	/**
	 * Get the package name.
	 *
	 * @return Return the package name as a string.
	 */
	String getName();

	/**
	 * Get the package product.
	 *
	 * @return Return the package product as a string.
	 */
	String getProduct();

	/**
	 * Get the package title.
	 *
	 * @return Return the package title as a string.
	 */
	String getTitle();

	/**
	 * Get the package trademark.
	 *
	 * @return Return the package trademark as a string.
	 */
	String getTrademark();

	/**
	 * Get the package version.
	 *
	 * @return Return the package version as a string.
	 */
	String getVersion();
}
