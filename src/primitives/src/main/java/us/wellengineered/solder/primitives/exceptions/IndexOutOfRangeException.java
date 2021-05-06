/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives.exceptions;

public class IndexOutOfRangeException extends SystemException
{
	public IndexOutOfRangeException()
	{
		super();
	}

	public IndexOutOfRangeException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
