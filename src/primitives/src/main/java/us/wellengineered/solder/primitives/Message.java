/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

public interface Message
{
	String getCategory();

	String getDescription();

	Severity getSeverity();
}
