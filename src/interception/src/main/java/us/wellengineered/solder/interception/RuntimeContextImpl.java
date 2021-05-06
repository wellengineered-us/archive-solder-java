/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.exceptions.InvalidOperationException;

public final class RuntimeContextImpl implements RuntimeContext
{
	public RuntimeContextImpl()
	{
	}

	private boolean continueInterception;
	private int interceptionCount;
	private int interceptionIndex;

	private void setInterceptionCount(int interceptionCount)
	{
		this.interceptionCount = interceptionCount;
	}

	private void setInterceptionIndex(int interceptionIndex)
	{
		this.interceptionIndex = interceptionIndex;
	}

	private void setContinueInterception(boolean continueInterception)
	{
		this.continueInterception = continueInterception;
	}

	@Override
	public boolean shouldContinueInterception()
	{
		return this.continueInterception;
	}

	@Override
	public int getInterceptionCount()
	{
		return this.interceptionCount;
	}

	@Override
	public int getInterceptionIndex()
	{
		return this.interceptionIndex;
	}

	@Override
	public void abortInterceptionChain()
	{
		if (!this.shouldContinueInterception())
			throw new InvalidOperationException(String.format("Cannot abort interception chain; the chain has was either previously aborted or pending graceful completion."));

		this.setContinueInterception(false);
	}
}
