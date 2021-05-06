/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class OperationCanceledException extends SystemException
{
	public OperationCanceledException()
	{
		super();
	}

	public OperationCanceledException(String message)
	{
		super(message);
	}

	public OperationCanceledException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public OperationCanceledException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
