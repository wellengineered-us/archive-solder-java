/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class ArgumentNullException extends ArgumentException
{
	public ArgumentNullException()
	{
		super();
	}

	public ArgumentNullException(String parameterName)
	{
		super(parameterName);
	}

	public ArgumentNullException(String parameterName, String message)
	{
		super(parameterName, message);
	}

	public ArgumentNullException(String message, Throwable cause)
	{
		super(null, message, cause);
	}

	public ArgumentNullException(String parameterName, String message, Throwable cause)
	{
		super(parameterName, message, cause);
	}

	public ArgumentNullException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
