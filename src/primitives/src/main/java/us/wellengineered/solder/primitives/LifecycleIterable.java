/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

public interface LifecycleIterable<TItem> extends Iterable<TItem>, Lifecycle
{
	LifecycleIterator<TItem> lifecycleIterator();
}
