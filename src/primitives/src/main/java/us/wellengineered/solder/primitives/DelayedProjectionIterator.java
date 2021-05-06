/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import java.util.Iterator;

public final class DelayedProjectionIterator<TInputItem, TOutputItem> extends AbstractForEachYieldIterator<TInputItem, TOutputItem>
{
	public DelayedProjectionIterator(Iterator<TInputItem> baseIterator, ConvertorDelegate<TInputItem, TOutputItem> itemConverter)
	{
		this(ITERATOR_BEFORE_STATE, baseIterator, itemConverter);
	}

	public DelayedProjectionIterator(int state, Iterator<TInputItem> baseIterator, ConvertorDelegate<TInputItem, TOutputItem> itemConverter)
	{
		super(state, baseIterator, itemConverter);
	}

	@Override
	protected LifecycleIterator<TOutputItem> newLifecycleIterator(int state)
	{
		return new DelayedProjectionIterator<TInputItem, TOutputItem>(state, this.getBaseIterator(), this.getItemConverter());
	}
}
