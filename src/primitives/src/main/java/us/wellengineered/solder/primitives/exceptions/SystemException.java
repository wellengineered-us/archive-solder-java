/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class SystemException extends RuntimeException
{
	public SystemException()
	{
		super();
	}

	public SystemException(String message)
	{
		super(message);
	}

	public SystemException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SystemException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
