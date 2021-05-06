/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import org.junit.jupiter.api.Test;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractForEachYieldIteratorTest
{
	public AbstractForEachYieldIteratorTest()
	{
	}

	@Test
	public void shouldTestAbstractForEachYieldIterator()
	{
		List<Integer> items;

		items = new ArrayList<>();
		items.add(1);
		items.add(10);
		items.add(100);
		items.add(1000);
		items.add(10000);
		items.add(100000);
		items.add(1000000);
		items.add(10000000);

		final Iterator<Integer> iterator = new AbstractForEachYieldIterator<Integer, Integer>(items.iterator(), (index, item) -> item * -1) {};

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