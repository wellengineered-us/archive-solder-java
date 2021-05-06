/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.primitives.ConvertorDelegate;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.LifecycleIterator;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.Utils;

import java.util.Iterator;

public abstract class AbstractForEachYieldIterator<TInputItem, TOutputItem> extends AbstractYieldIterator<TOutputItem>
{
	protected AbstractForEachYieldIterator(Iterator<TInputItem> baseIterator, ConvertorDelegate<TInputItem, TOutputItem> itemConverter)
	{
		this(ITERATOR_BEFORE_STATE, baseIterator, itemConverter);
	}

	protected AbstractForEachYieldIterator(int machineState, Iterator<TInputItem> baseIterator, ConvertorDelegate<TInputItem, TOutputItem> itemConverter)
	{
		super(machineState);

		if (baseIterator == null)
			throw new ArgumentNullException("baseIterator");

		if (itemConverter == null)
			throw new ArgumentNullException("itemConverter");

		this.baseIterator = baseIterator;
		this.itemConverter = itemConverter;
	}

	private final Iterator<TInputItem> baseIterator;
	private final ConvertorDelegate<TInputItem, TOutputItem> itemConverter;

	protected Iterator<TInputItem> getBaseIterator()
	{
		return this.baseIterator;
	}

	protected ConvertorDelegate<TInputItem, TOutputItem> getItemConverter()
	{
		return this.itemConverter;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (this.isCreated())
			return;

		if (creating)
		{
			if (!Utils.maybeCreate(this.getBaseIterator()))
				Utils.nop();
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			if (!Utils.maybeDispose(this.getBaseIterator()))
				Utils.hitWithBrick(this.getBaseIterator());
		}
	}

	@Override
	protected LifecycleIterator<TOutputItem> newLifecycleIterator(int state)
	{
		// simple default implementation
		return this;
	}

	@Override
	protected boolean onTryYield(MethodParameterModifier.Out<TOutputItem> outValue) throws Exception
	{
		TInputItem oldItem;
		TOutputItem newItem;
		boolean hasNext;

		if (outValue == null)
			throw new ArgumentNullException("value");

		hasNext = this.getBaseIterator().hasNext();

		if (!hasNext)
			return false;

		oldItem = this.getBaseIterator().next();
		newItem = this.getItemConverter().invoke(this.getItemIndex() + 1 /* tentative index */, oldItem);

		outValue.setValue(newItem);
		return true;
	}

	@Override
	protected void onYieldComplete() throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldFault(Exception ex)
	{
		// do nothing
	}

	@Override
	protected void onYieldResume() throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldReturn(TOutputItem value) throws Exception
	{
		// do nothing
	}

	@Override
	protected void onYieldStart() throws Exception
	{
		// do nothing
	}
}
