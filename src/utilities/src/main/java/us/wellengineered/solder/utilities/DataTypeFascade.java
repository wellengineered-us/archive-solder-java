/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.MethodParameterModifier;

public interface DataTypeFascade
{
	<TValue> TValue changeClass(Class<? extends TValue> valueClass, Object value);

	<TValue> TValue defaultValue(Class<? extends TValue> valueClass);

	Object inferDbTypeFromClass(Class<?> valueClass);

	boolean isNullOrEmpty(String value);

	boolean isNullOrWhiteSpace(String value);

	boolean isWhiteSpace(String value);

	boolean objectsEqualValueSemantics(Object objA, Object objB);

	<TValue> String safeToString(TValue value, String format, String defaultValue);

	<TValue> boolean tryParse(Class<? extends TValue> valueClass, String value, MethodParameterModifier.Out<TValue> outResult);
}
