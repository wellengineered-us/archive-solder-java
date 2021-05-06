/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class ObjectCreatedException extends SystemException
{
	public ObjectCreatedException()
	{
		super();
	}

	public ObjectCreatedException(String message)
	{
		super(message);
	}

	public ObjectCreatedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ObjectCreatedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
