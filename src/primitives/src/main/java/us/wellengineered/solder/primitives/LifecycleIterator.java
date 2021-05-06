/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import java.util.Iterator;

public interface LifecycleIterator<TItem> extends Iterator<TItem>, Lifecycle
{
}
