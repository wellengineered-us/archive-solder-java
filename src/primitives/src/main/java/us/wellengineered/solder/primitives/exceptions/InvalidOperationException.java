/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class InvalidOperationException extends SystemException
{
	public InvalidOperationException()
	{
		super();
	}

	public InvalidOperationException(String message)
	{
		super(message);
	}

	public InvalidOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidOperationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
