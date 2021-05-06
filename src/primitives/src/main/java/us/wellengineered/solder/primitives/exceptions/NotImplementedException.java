/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class NotImplementedException extends SystemException
{
	public NotImplementedException()
	{
		super();
	}

	public NotImplementedException(String message)
	{
		super(message);
	}

	public NotImplementedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotImplementedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
