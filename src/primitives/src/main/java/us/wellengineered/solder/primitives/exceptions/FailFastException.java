/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public final class FailFastException extends SystemException
{
	public FailFastException()
	{
		super();
	}

	public FailFastException(String message)
	{
		super(message);
	}

	public FailFastException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailFastException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
