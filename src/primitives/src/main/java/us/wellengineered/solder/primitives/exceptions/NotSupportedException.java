/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class NotSupportedException extends SystemException
{
	public NotSupportedException()
	{
		super();
	}

	public NotSupportedException(String message)
	{
		super(message);
	}

	public NotSupportedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotSupportedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
