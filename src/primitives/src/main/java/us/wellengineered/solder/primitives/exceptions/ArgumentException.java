/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public abstract class ArgumentException extends SystemException
{
	protected ArgumentException()
	{
		super(getRangeMessage(null, null));
	}

	protected ArgumentException(String parameterName)
	{
		super(getRangeMessage(parameterName, null));
	}

	protected ArgumentException(String parameterName, String message)
	{
		super(getRangeMessage(parameterName, message));
	}

	protected ArgumentException(String message, Throwable cause)
	{
		super(getRangeMessage(null, message), cause);
	}

	protected ArgumentException(String parameterName, String message, Throwable cause)
	{
		super(getRangeMessage(parameterName, message), cause);
	}

	protected ArgumentException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
	private static final String RANGE_MESSAGE_FORMAT = "'%s' was out of range.";

	private static String getRangeMessage(String parameterName, String message)
	{
		return String.format(RANGE_MESSAGE_FORMAT, parameterName, message);
	}
}
