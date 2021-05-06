/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

/**
 * Context storage scopes.
 */
public enum ContextScope
{
	/**
	 * Unknown, undefined, or invalid scope.
	 */
	UNKNOWN(0),

	/**
	 * Globally static visible and implicitly NOT thread-safe.
	 */
	GLOBAL_STATIC_UNSAFE(1),

	/**
	 * Local thread visible and thread-safe obviously.
	 */
	LOCAL_THREAD_SAFE(2),

	/**
	 * Local async visible and async-safe (and perhaps thread-safe).
	 */
	LOCAL_ASYNC_SAFE(3),

	/**
	 * Local request visible, context-agility-safe, and NOT thread-safe.
	 * Commonly used with high performance (web) server frameworks that are not guaranteed
	 * to be exhibit thread affinity (e.g. context can be fork-lifted thread to thread under load).
	 */
	LOCAL_REQUEST_SAFE(4);

	ContextScope(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
