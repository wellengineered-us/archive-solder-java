/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.Utils;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.ObjectCreatedException;
import us.wellengineered.solder.primitives.exceptions.ObjectDisposedException;
import us.wellengineered.solder.utilities.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static us.wellengineered.solder.primitives.Utils.failFastOnlyWhen;
import static us.wellengineered.solder.primitives.Utils.failFastWithException;

/**
 * Serves as a logical run-time boundary for dependencies.
 */
public final class DependencyDomain extends AbstractLifecycle<Exception, Exception>
{
	public DependencyDomain()
	{
		this._hcf = new Thread(this::_haltAndCatchFire);

		try
		{
			this.create();
		}
		catch (Exception ex)
		{
			failFastWithException(ex);
		}
	}

	private final Thread _hcf;
	private final String APP_CONFIG_FILE_NAME = "appconfig.json";
	private final DependencyManager dependencyManager = new DependencyManagerImpl();
	private final Map<DependencyName, Dependency> knownDependencies = new LinkedHashMap<>();
	private final ReadWriteLock readerWriterLock = new ReentrantReadWriteLock();

	private DependencyManager getDependencyManager()
	{
		return this.dependencyManager;
	}

	private Map<DependencyName, Dependency> getKnownDependencies()
	{
		return this.knownDependencies;
	}

	private ReadWriteLock getReaderWriterLock()
	{
		return this.readerWriterLock;
	}

	public static DependencyDomain getDefault()
	{
		DependencyDomain instance = LazySingleton.__;

		// the only way this is null is if this accessor is invoked AGAIN while the lazy singleton is being constructed
		failFastOnlyWhen(instance == null, "Not re-entrant; property is invoked AGAIN while the lazy singleton is being contructed.");

		return instance;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		Iterable<Dependency> dependencies;

		if (!creating)
			return;

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isCreated())
				throw new ObjectCreatedException(DependencyDomain.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyDomain.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				// add trusted dependencies
				addTrustedDependencies(this.getDependencyManager());

				// hook JVM events ???
				Runtime.getRuntime().addShutdownHook(this._hcf);
				// JVM does not have a way to notify on class load ???

				// probe known assemblies at run-time
				dependencies = new ArrayList<>(); // not possible in JVM ???
				this.scanDependencies(dependencies);

				// special case here since this class wil execute under multi-threaded scenarios
				this.explicitSetCreated();

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (!disposing)
			return;

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isDisposed())
				return;

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				final DependencyManager dependencyManager = this.getDependencyManager();

				if (dependencyManager != null)
					dependencyManager.dispose();

				// unhook JVM events
				Runtime.getRuntime().removeShutdownHook(this._hcf);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// special case here since this class wil execute under multi-threaded scenarios
				this.explicitSetDisposed();

				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private final void _haltAndCatchFire()
	{
		// this method gets called on another thread from main()...
		try
		{
			this.onProcessExit();
		}
		catch (Exception ex)
		{
			Utils.failFastWithException(ex);
		}
	}

	@Override
	protected void maybeSetCreated()
	{
		// do nothing??
	}

	@Override
	protected void maybeSetDisposed()
	{
		// do nothing??
	}

	private static void addTrustedDependencies(DependencyManager dependencyManager) throws DependencyException
	{
		DataTypeFascade dataTypeFascade;
		ReflectionFascade reflectionFascade;
		AppConfigFascade appConfigFascade;
		PackageInfoFascade packageInfoFascade;

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		dataTypeFascade = new DataTypeFascadeImpl();
		reflectionFascade = new ReflectionFascadeImpl(dataTypeFascade);
		appConfigFascade = new AppConfigFascadeImpl(dataTypeFascade);
		packageInfoFascade = new PackageInfoFascadeImpl(dependencyManager.getClass());

		//dependencyManager.addResolution(DataTypeFascade.class, Utils.EMPTY_STRING, false, new SingletonWrapperDependencyResolution<DataTypeFascade>(new InstanceDependencyResolution<DataTypeFascade>(dataTypeFascade)));
		//dependencyManager.addResolution(ReflectionFascade.class, Utils.EMPTY_STRING, false, new SingletonWrapperDependencyResolution<ReflectionFascade>(new InstanceDependencyResolution<ReflectionFascade>(reflectionFascade)));
		//dependencyManager.addResolution(AppConfigFascade.class, Utils.EMPTY_STRING, false, new SingletonWrapperDependencyResolution<AppConfigFascade>(new InstanceDependencyResolution<AppConfigFascade>(appConfigFascade)));
		//dependencyManager.addResolution(PackageInfoFascade.class, Utils.EMPTY_STRING, false, new SingletonWrapperDependencyResolution<PackageInfoFascade>(new InstanceDependencyResolution<PackageInfoFascade>(packageInfoFascade)));
	}

	private void onDependencyLoaded(Dependency dependency) throws DependencyException
	{
		if (dependency == null)
			throw new ArgumentNullException("dependency");

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isCreated())
				throw new ObjectCreatedException(DependencyDomain.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyDomain.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				// probe implicit dynamically loaded dependencies
				this.scanDependency(dependency);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private void onProcessExit() throws Exception
	{
		this.dispose();
	}

	private static void fireDependencyMagicMethods(Class<?> dependencyClass, DependencyManager dependencyManager) throws DependencyException
	{
		Method[] methods;
		DependencyMagicMethod dependencyMagicMethodAnnotation;
		ReflectionFascade reflectionFascade;
		Object retval;

		if (dependencyClass == null)
			throw new ArgumentNullException("dependencyClass");

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		reflectionFascade = dependencyManager.resolveDependency(ReflectionFascade.class, Utils.EMPTY_STRING, false);

		failFastOnlyWhen(reflectionFascade == null, "reflectionFascade == null");

		methods = dependencyClass.getMethods();

		if (methods != null)
		{
			for (Method method : methods)
			{
				// annotated?
				dependencyMagicMethodAnnotation = reflectionFascade.getOneAnnotation(method, DependencyMagicMethod.class);

				if (dependencyMagicMethodAnnotation == null)
					continue;

				// static and public?
				if (!method.canAccess(null))
					continue;

				// void?
				if (method.getReturnType() != Void.class)
					continue;

				// (DependencyManager)?
				if (method.getParameterCount() != 1 ||
						method.getParameterTypes()[0] != DependencyManager.class)
					continue;

				try
				{
					retval = method.invoke(null, new Object[] { dependencyManager });
				}
				catch (Exception ex)
				{
					throw new DependencyException(ex);
				}

				if (retval == null ||
						retval.getClass() != Void.class)
					continue;
			}
		}
	}

	public Dependency loadDependency(DependencyName dependencyName) throws DependencyException
	{
		Dependency dependency;

		if (dependencyName == null)
			throw new ArgumentNullException("dependencyName");

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isCreated())
				throw new ObjectCreatedException(DependencyDomain.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyDomain.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				dependency = new Dependency()
				{
					@Override
					public DependencyName getName()
					{
						return dependencyName;
					}

					@Override
					public Iterable<Class<?>> getExportedClasses()
					{
						return Collections.emptyList();
					}
				};

				// probe explicit dynamically loaded dependencies
				if (dependency != null)
					this.scanDependency(dependency);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();

				return dependency;
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private void scanDependencies(Iterable<Dependency> dependencies) throws DependencyException
	{
		if (dependencies != null)
		{
			for (Dependency dependency : dependencies)
				this.scanDependency(dependency);
		}
	}

	private void scanDependency(Dependency dependency) throws DependencyException
	{
		DependencyName dependencyName;
		Iterable<Class<?>> dependencyClasses;

		if (dependency == null)
			throw new ArgumentNullException("dependency");

		dependencyName = dependency.getName();

		if (this.getKnownDependencies().containsKey(dependencyName))
			return; // already seen this before, skip

		// track which ones we have seen
		this.getKnownDependencies().put(dependencyName, dependency);
		dependencyClasses = dependency.getExportedClasses();

		if (dependencyClasses != null)
		{
			for (Class<?> dependencyClass : dependencyClasses)
			{
				final DependencyManager dependencyManager = this.getDependencyManager();
				// TODO: in the future we could support real auto-wire via type probing
				fireDependencyMagicMethods(dependencyClass, dependencyManager);
			}
		}
	}

	/**
	 * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 */
	private static class LazySingleton
	{
		static
		{
		}

		private static final DependencyDomain __ = new DependencyDomain();

		public static DependencyDomain get()
		{
			return __;
		}
	}
}
