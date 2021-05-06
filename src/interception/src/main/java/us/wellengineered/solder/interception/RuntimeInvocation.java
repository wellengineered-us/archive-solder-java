/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.Lifecycle;

import java.lang.reflect.Method;

public interface RuntimeInvocation extends Lifecycle
{
	Object[] getInvocationArguments();

	Object getProxyInstance();

	Method getTargetMethod();

	Class<?> getTargetClass();

	Object getWrappedInstance();

	Object getInvocationReturnValue();

	void invokeIndirect() throws Exception;
}
