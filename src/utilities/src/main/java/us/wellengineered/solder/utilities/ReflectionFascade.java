/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.MethodParameterModifier;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

public interface ReflectionFascade
{
	<TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(Object targetInstance, Class<? extends TAnnotation> annotationClass);

	String getErrors(Throwable exception, int indent);

	PropertyDescriptor getLowestLogicalProperty(Class<?> targetClass, String propertyName) throws Exception;

	<TAnnotation extends Annotation> TAnnotation getOneAnnotation(Object targetInstance, Class<? extends TAnnotation> annotationClass);

	<TAnnotation extends Annotation> void getZeroAnnotations(Object targetInstance, Class<? extends TAnnotation> annotationClass);

	Class<?> makeNonNullableClass(Class<?> conversionClass);

	Class<?> makeNullableClass(Class<?> conversionClass);

	boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue) throws Exception;

	boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue, boolean stayHard, boolean makeSoft) throws Exception;

	boolean tryGetLogicalPropertyClass(Object targetInstance, String propertyName, MethodParameterModifier.Out<Class<?>> outPropertyClass) throws Exception;

	boolean tryGetLogicalPropertyValue(Object targetInstance, String propertyName, MethodParameterModifier.Out<Object> outPropertyValue) throws Exception;
}
