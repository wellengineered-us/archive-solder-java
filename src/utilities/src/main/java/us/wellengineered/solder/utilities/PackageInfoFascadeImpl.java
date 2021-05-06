/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

import static us.wellengineered.solder.primitives.Utils.failFastOnlyWhen;

public final class PackageInfoFascadeImpl implements PackageInfoFascade
{
	public PackageInfoFascadeImpl(Class<?> targetClass)
	{
		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		failFastOnlyWhen(classLoader == null, "classLoader == null");

		final Package _package = classLoader.getDefinedPackage("");

		failFastOnlyWhen(_package == null, "_package == null");

		this.name = _package.getName();
		this.title = _package.getSpecificationTitle();
		this.company = _package.getSpecificationVendor();
		this.version = _package.getSpecificationVersion();
		this.product = _package.getImplementationTitle();
		this.trademark = _package.getImplementationVendor();
		this.configuration = _package.getImplementationVersion();
		this.copyright = null;
		this.description = null;
	}

	private final String company;
	private final String configuration;
	private final String copyright;
	private final String description;
	private final String name;
	private final String product;
	private final String title;
	private final String trademark;
	private final String version;

	@Override
	public String getCompany()
	{
		return this.company;
	}

	@Override
	public String getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public String getCopyright()
	{
		return this.copyright;
	}

	@Override
	public String getDescription()
	{
		return this.description;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getProduct()
	{
		return this.product;
	}

	@Override
	public String getTitle()
	{
		return this.title;
	}

	@Override
	public String getTrademark()
	{
		return this.trademark;
	}

	@Override
	public String getVersion()
	{
		return this.version;
	}
}
