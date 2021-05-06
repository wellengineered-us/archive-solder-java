/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.AbstractYieldIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class AbstractYieldIteratorTest
{
	public AbstractYieldIteratorTest()
	{
	}

	@Test
	public void shouldTestAbstractYieldIterator()
	{
		final int lb = 0;
		final int ub = 10;

		final Iterator<Integer> iterator = new AbstractYieldIterator<Integer>()
		{
			private int value;

			@Override
			protected void create(boolean creating) throws Exception
			{
				System.out.println("create");
			}

			@Override
			protected void dispose(boolean disposing) throws Exception
			{
				System.out.println("dispose");
			}

			@Override
			protected LifecycleIterator<Integer> newLifecycleIterator(int state)
			{
				// do nothing
				return this;
			}

			@Override
			protected boolean onTryYield(MethodParameterModifier.Out<Integer> value) throws Exception
			{
				if (value == null)
					throw new ArgumentNullException("value");

				System.out.println("try");

				if (this.value < ub)
				{
					//if(this.value == 5)
					//throw new InvalidOperationException();

					value.setValue(this.value);
					return true;
				}

				return false;
			}

			@Override
			protected void onYieldComplete() throws Exception
			{
				System.out.println("complete");
				this.value = -1; // }
			}

			@Override
			protected void onYieldFault(Exception ex)
			{
				System.out.println("fault");
			}

			@Override
			protected void onYieldResume() throws Exception
			{
				System.out.println("resume");
			}

			@Override
			protected void onYieldReturn(Integer value) throws Exception
			{
				System.out.println("return");
				this.value++; // for(..., ..., value++)
			}

			@Override
			protected void onYieldStart() throws Exception
			{
				System.out.println("start");
				this.value = lb; // for(int value = lb; ...
			}
		};

		/*while(true)
		{
			final Integer value = iterator.next();
			System.out.println(value);
		}*/

		/*for(Integer i : ((Iterable<Integer>)iterator))
		{
			System.out.println(i);
		}*/

		while (iterator.hasNext())
		{
			final Integer value = iterator.next();
			System.out.println(value);
		}
	}
}