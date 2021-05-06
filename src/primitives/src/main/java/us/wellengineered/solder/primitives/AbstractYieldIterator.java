/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.LifecycleIterable;
import us.wellengineered.solder.primitives.LifecycleIterator;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.exceptions.FailFastException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static us.wellengineered.solder.primitives.Utils.failFastOnlyWhen;

/**
 * Inspired by reverse-engineered C# compiler yield machine state machine enumerables/enumerators.
 * NOTE: in this 'yield' implementation, hasNext() is not idempotent because we are dealing with non-buffered/streaming sources.
 * We do cache a single non-null currentItem value, which is used as a machine state modifier to drive functionality.
 * Special thanks to http://csharpindepth.com/Articles/Chapter6/IteratorBlockImplementation.aspx
 * <p>
 * An attempt to generalize the problem for a certain class of "pedestrian" iterators is the goal.
 * For really complex implementations, multiple states may be required and are not supported (yet) by this class.
 * NOTE: The running, after, and fault states have the same integer value by design.
 *
 * @param <TItem>
 */
public abstract class AbstractYieldIterator<TItem> extends AbstractLifecycle<Exception, Exception> implements LifecycleIterator<TItem>, LifecycleIterable<TItem>
{
	protected AbstractYieldIterator()
	{
		this(ITERATOR_BEFORE_STATE);
	}

	protected AbstractYieldIterator(int machineState)
	{
		this.setMachineState(machineState);
		this.initialThreadId = Thread.currentThread().getId();
	}

	protected static final int ITERABLE_BEFORE_ITERATOR_STATE = -2; // before the first call to iterator() from the creating thread
	protected static final int ITERATOR_AFTER_STATE = -1; // "After" - the iterator has finished, either by reaching the end of the method or by hitting yield break
	protected static final int ITERATOR_BEFORE_STATE = 0; // next() hasn't been called yet
	private static final int ITERATOR_FAULT_STATE = -1; // "Fault" - the iterator threw an exception - internal use only
	protected static final int ITERATOR_RESUME_STATE = 1; // it's yielded at least one value, and there's possibly more to come
	protected static final int ITERATOR_RUNNING_STATE = -1; // "Running" - the iterator is currently executing code
	private final long initialThreadId;
	private TItem currentItem;
	private boolean itemCached;
	private long itemIndex;
	private int machineState;

	private TItem getCurrentItem()
	{
		return this.currentItem;
	}

	private void setCurrentItem(TItem currentItem)
	{
		this.currentItem = currentItem;
	}

	protected final long getInitialThreadId()
	{
		return this.initialThreadId;
	}

	public long getItemIndex()
	{
		return this.itemIndex;
	}

	private void setItemIndex(long itemIndex)
	{
		this.itemIndex = itemIndex;
	}

	protected final int getMachineState()
	{
		return this.machineState;
	}

	protected final void setMachineState(int machineState)
	{
		this.machineState = machineState;
	}

	public final boolean isAbandonedIfNewlyIterated()
	{
		return this.getMachineState() != ITERATOR_BEFORE_STATE;
	}

	private boolean isItemCached()
	{
		return this.itemCached;
	}

	private void setItemCached(boolean itemCached)
	{
		this.itemCached = itemCached;
	}

	@Override
	public final boolean hasNext()
	{
		if (this.isItemCached()) // is there a cached current item value ready to yield?
			return true;
		else if (this.getMachineState() == ITERATOR_AFTER_STATE ||
				this.getMachineState() == ITERATOR_RUNNING_STATE)
			return false;
		else if (this.getMachineState() == ITERATOR_BEFORE_STATE ||
				this.getMachineState() == ITERATOR_RESUME_STATE)
		{
			try
			{
				final boolean result = this.moveNext(); // hasNext --> moveNext: not idempotent by design
				return result;
			}
			catch (Exception ex)
			{
				throw new FailFastException(ex);
			}
		}
		else
			throw new NoSuchElementException(String.format("Invalid iterator machine state: %s", this.getMachineState()));
	}

	private void incrementItemIndex()
	{
		this.setItemIndex(this.getItemIndex() + 1);
	}

	@Override
	public final Iterator<TItem> iterator()
	{
		if (Thread.currentThread().getId() == this.getInitialThreadId() &&
				this.getMachineState() == ITERABLE_BEFORE_ITERATOR_STATE)
		{
			this.setMachineState(ITERATOR_BEFORE_STATE);
			return this;
		}

		return this.newLifecycleIterator(ITERATOR_BEFORE_STATE);
	}

	@Override
	public final LifecycleIterator<TItem> lifecycleIterator()
	{
		if (Thread.currentThread().getId() == this.getInitialThreadId() &&
				this.getMachineState() == ITERABLE_BEFORE_ITERATOR_STATE)
		{
			this.setMachineState(ITERATOR_BEFORE_STATE);
			return this;
		}

		return this.newLifecycleIterator(ITERATOR_BEFORE_STATE);
	}

	protected void maybeCreateBeforeYield() throws Exception
	{
		this.create();
	}

	protected void maybeDisposeAfterFault() throws Exception
	{
		this.dispose();
	}

	protected void maybeDisposeAfterYield() throws Exception
	{
		this.dispose();
	}

	protected boolean moveNext() throws Exception
	{
		boolean result;
		MethodParameterModifier.Out<TItem> outValue;

		try
		{
			while (this.getMachineState() == ITERATOR_BEFORE_STATE ||
					this.getMachineState() == ITERATOR_RESUME_STATE)
			{
				switch (this.getMachineState())
				{
					case ITERATOR_BEFORE_STATE:
					{
						this.setMachineState(ITERATOR_RUNNING_STATE);

						this.maybeCreateBeforeYield();

						this.setItemIndex(-1);
						this.onYieldStart();

						this.setMachineState(ITERATOR_RESUME_STATE);
						break;
					}
					case ITERATOR_RESUME_STATE:
					{
						this.setMachineState(ITERATOR_RUNNING_STATE);

						failFastOnlyWhen(this.isItemCached(), "this.isItemCached()");

						if (this.getItemIndex() > -1) // ???
							this.onYieldResume();

						outValue = new MethodParameterModifier.Out<>(Object.class);

						result = this.onTryYield(outValue);

						if (result)
						{
							failFastOnlyWhen(!outValue.isSet(), "!outValue.isSet()");

							this.incrementItemIndex();
							final TItem value = outValue.getValue();
							this.onYieldReturn(value);

							// we should not have a previously cached value, so this makes absolute sense...
							this.setItemCached(true);
							this.setCurrentItem(value);

							this.setMachineState(ITERATOR_RESUME_STATE);
							return true;
						}
						else
						{
							failFastOnlyWhen(outValue.isSet(), "outValue.isSet()");

							this.onYieldComplete();

							this.setItemCached(false);
							this.setCurrentItem(null);
							this.maybeDisposeAfterYield();

							this.setMachineState(ITERATOR_AFTER_STATE);
							return false;
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			this.onYieldFault(ex);

			this.setItemCached(false);
			this.setCurrentItem(null);
			this.setMachineState(ITERATOR_FAULT_STATE);
			this.maybeDisposeAfterFault();

			throw ex; // re-throw
		}

		throw new NoSuchElementException(String.format("Invalid iterator machine state: %s", this.getMachineState()));
	}

	protected abstract LifecycleIterator<TItem> newLifecycleIterator(int state);

	@Override
	public final TItem next()
	{
		if (!this.isItemCached())
		{
			if (!this.hasNext()) // next --> hasNext --> moveNext: not idempotent by design
				throw new NoSuchElementException(String.format("%s::hasNext() returned false.", this.getClass().getName()));
		}

		failFastOnlyWhen(!this.isItemCached(), "!this.isItemCached()");

		final TItem currentItem = this.getCurrentItem();

		this.setItemCached(false);
		this.setCurrentItem(null);

		return currentItem;
	}

	protected abstract boolean onTryYield(MethodParameterModifier.Out<TItem> outValue) throws Exception;

	protected abstract void onYieldComplete() throws Exception;

	protected abstract void onYieldFault(Exception ex);

	protected abstract void onYieldResume() throws Exception;

	protected abstract void onYieldReturn(TItem value) throws Exception;

	protected abstract void onYieldStart() throws Exception;
}
