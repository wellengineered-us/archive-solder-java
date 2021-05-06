/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.Disposable;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.NotImplementedException;
import us.wellengineered.solder.primitives.exceptions.ObjectDisposedException;

public abstract class AbstractRuntimeInterception extends AbstractLifecycle<Exception, Exception> implements RuntimeInterception
{
	protected AbstractRuntimeInterception()
	{
	}

	public static <TTarget> TTarget createProxy(Class<? extends TTarget> targetClass, RuntimeInterception[] runtimeInterceptionChain)
	{
		TTarget proxyInstance;

		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (runtimeInterceptionChain == null)
			throw new ArgumentNullException("runtimeInterceptionChain");

		throw new NotImplementedException();
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

	@Override
	public final void invoke(RuntimeInvocation runtimeInvocation, RuntimeContext runtimeContext) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (runtimeContext == null)
			throw new ArgumentNullException("runtimeContext");

		this.onInvoke(runtimeInvocation, runtimeContext);
	}

	protected void onAfterInvoke(boolean proceedWithInvocation, RuntimeInvocation runtimeInvocation, MethodParameterModifier.Ref<Exception> outThrownException)
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outThrownException == null)
			throw new ArgumentNullException("outThrownException");
	}

	protected void onBeforeInvoke(RuntimeInvocation runtimeInvocation, MethodParameterModifier.Out<Boolean> outProceedWithInvocation)
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outProceedWithInvocation == null)
			throw new ArgumentNullException("outProceedWithInvocation");

		outProceedWithInvocation.setValue(true);
	}

	protected void onInvoke(RuntimeInvocation runtimeInvocation, RuntimeContext runtimeContext) throws Exception
	{
		final MethodParameterModifier.Out<Boolean> outProceedWithInvocation = new MethodParameterModifier.Out<>(Boolean.class);
		final MethodParameterModifier.Out<Exception> outThrownException = new MethodParameterModifier.Out<>(Exception.class);

		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (runtimeContext == null)
			throw new ArgumentNullException("runtimeContext");

		if (!(runtimeInvocation.getTargetMethod() != null &&
				runtimeInvocation.getTargetMethod().getDeclaringClass() == Disposable.class) &&
				this.isDisposed()) // always forward dispose invocations
			throw new ObjectDisposedException(this.getClass().getName());

		this.onBeforeInvoke(runtimeInvocation, outProceedWithInvocation);

		if (outProceedWithInvocation.getValue())
			this.onProceedInvoke(runtimeInvocation, outThrownException);

		final MethodParameterModifier.Ref<Exception> refThrownException = new MethodParameterModifier.Ref<>(Exception.class, outThrownException.getValue());
		this.onAfterInvoke(outProceedWithInvocation.getValue(), runtimeInvocation, refThrownException);

		final Exception thrownException = refThrownException.getValue();

		if (thrownException != null)
		{
			runtimeContext.abortInterceptionChain();
			throw thrownException;
		}
	}

	protected void onMagicalSpellInvoke(RuntimeInvocation runtimeInvocation) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		runtimeInvocation.invokeIndirect(); // use reflection over wrapped instance
	}

	protected void onProceedInvoke(RuntimeInvocation runtimeInvocation, MethodParameterModifier.Out<Exception> outThrownException)
	{
		Exception exception;

		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outThrownException == null)
			throw new ArgumentNullException("outThrownException");

		try
		{
			exception = null;
			this.onMagicalSpellInvoke(runtimeInvocation);
		}
		catch (Exception ex)
		{
			exception = ex;
		}

		outThrownException.setValue(exception);
	}
}
