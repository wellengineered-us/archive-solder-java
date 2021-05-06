/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

/**
 * The exception thrown when a application configuration error occurs.
 */
public class AppConfigException extends RuntimeException
{
	/**
	 * Initializes a new instance of the AppConfigException class.
	 */
	public AppConfigException()
	{
		super();
	}

	/**
	 * Initializes a new instance of the AppConfigException class.
	 *
	 * @param message The message that describes the error.
	 */
	public AppConfigException(String message)
	{
		super(message);
	}

	/**
	 * Initializes a new instance of the AppConfigException class.
	 *
	 * @param message The message that describes the error.
	 * @param cause   The inner exception.
	 */
	public AppConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Initializes a new instance of the AppConfigException class.
	 *
	 * @param cause The inner exception.
	 */
	public AppConfigException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
