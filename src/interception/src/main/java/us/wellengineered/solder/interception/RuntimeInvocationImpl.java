/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

import java.lang.reflect.Method;

public final class RuntimeInvocationImpl extends AbstractLifecycle<Exception, Exception> implements RuntimeInvocation
{
	public RuntimeInvocationImpl(Object proxyInstance, Class<?> targetClass, Method targetMethod, Object[] invocationArguments, Object wrappedInstance)
	{
		if (proxyInstance == null)
			throw new ArgumentNullException("proxyInstance");

		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (targetMethod == null)
			throw new ArgumentNullException("targetMethod");

		if (invocationArguments == null)
			throw new ArgumentNullException("invocationArguments");

		//if ((object)wrappedInstance == null)
		//throw new ArgumentNullException(nameof(wrappedInstance));

		this.proxyInstance = proxyInstance;
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
		this.invocationArguments = invocationArguments;
		this.wrappedInstance = wrappedInstance;
	}

	private final Object proxyInstance;
	private final Class<?> targetClass;
	private final Method targetMethod;
	private final Object[] invocationArguments;
	private final Object wrappedInstance;
	private Object invocationReturnValue;

	@Override
	public Object[] getInvocationArguments()
	{
		return this.invocationArguments;
	}

	@Override
	public Object getProxyInstance()
	{
		return this.proxyInstance;
	}

	@Override
	public Method getTargetMethod()
	{
		return this.targetMethod;
	}

	@Override
	public Class<?> getTargetClass()
	{
		return this.targetClass;
	}

	@Override
	public Object getWrappedInstance()
	{
		return this.wrappedInstance;
	}

	@Override
	public Object getInvocationReturnValue()
	{
		return this.invocationReturnValue;
	}

	@Override
	public void invokeIndirect() throws Exception
	{
		if (this.getWrappedInstance() != null)
			this.setInvocationReturnValue(this.getTargetMethod().invoke(this.getWrappedInstance(), this.getInvocationArguments()));
	}

	private void setInvocationReturnValue(Object invocationReturnValue)
	{
		this.invocationReturnValue = invocationReturnValue;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		// do nothing
	}
}
