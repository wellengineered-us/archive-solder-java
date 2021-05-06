/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class ObjectDisposedException extends InvalidOperationException
{
	public ObjectDisposedException()
	{
		super();
	}

	public ObjectDisposedException(String message)
	{
		super(message);
	}

	public ObjectDisposedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ObjectDisposedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
