/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

public interface Validatable
{
	Iterable<Message> validate();

	Iterable<Message> validate(Object context);
}
