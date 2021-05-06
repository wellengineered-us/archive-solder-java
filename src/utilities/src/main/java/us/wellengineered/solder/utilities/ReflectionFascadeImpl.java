/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.exceptions.InvalidOperationException;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.Utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionFascadeImpl implements ReflectionFascade
{
	public ReflectionFascadeImpl(DataTypeFascade dataTypeFascade)
	{
		if (dataTypeFascade == null)
			throw new ArgumentNullException("dataTypeFascade");

		this.dataTypeFascade = dataTypeFascade;
	}

	private final DataTypeFascade dataTypeFascade;

	private DataTypeFascade getDataTypeFascade()
	{
		return this.dataTypeFascade;
	}

	@Override
	public <TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(Object targetInstance, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetInstance == null)
			throw new ArgumentNullException("targetInstance");

		if (annotationClass == null)
			throw new ArgumentNullException("annotationClass");

		final Class<?> targetClass = targetInstance.getClass();

		if (targetClass == null)
			return null;

		annotations = targetClass.getAnnotationsByType(annotationClass);

		return annotations;
	}

	@Override
	public String getErrors(Throwable exception, int indent)
	{
		String message = Utils.EMPTY_STRING;
		final String INDENT_CHAR = "\t";

		while (exception != null)
		{
			message += INDENT_CHAR.repeat(indent) + "+++ BEGIN EXECPTION +++" + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Exception class: " + exception.getClass().getName() + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Error message: " + exception.getMessage() + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Stack trace:" + System.lineSeparator();

			final StackTraceElement[] frames = exception.getStackTrace();

			if (frames != null)
			{
				for (StackTraceElement frame : frames)
					message += INDENT_CHAR.repeat(indent + 1) + frame.toString() + System.lineSeparator();
			}

			message += INDENT_CHAR.repeat(indent) + "+++ END EXECPTION +++" + System.lineSeparator();

			exception = exception.getCause();
			indent++;
		}

		return message;
	}

	@Override
	public PropertyDescriptor getLowestLogicalProperty(Class<?> targetClass, String propertyName) throws Exception
	{
		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (propertyName == null)
			throw new ArgumentNullException("propertyName");

		while (targetClass != null)
		{
			final BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);

			if (beanInfo == null)
				return null;

			final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			if (propertyDescriptors == null)
				return null;

			for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
			{
				if (propertyName.equalsIgnoreCase(propertyDescriptor.getName()))
					return propertyDescriptor;
			}

			targetClass = targetClass.getSuperclass();
		}

		return null;
	}

	@Override
	public <TAnnotation extends Annotation> TAnnotation getOneAnnotation(Object targetInstance, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetInstance == null)
			throw new ArgumentNullException("targetInstance");

		final Class<?> targetClass = targetInstance.getClass();

		if (targetClass == null)
			return null;

		annotations = this.getAllAnnotations(targetInstance, annotationClass);

		if (annotations == null || annotations.length == 0)
			return null;
		else if (annotations.length > 1)
			throw new InvalidOperationException(String.format("Multiple custom attributes of type '%s' are defined on type '%s'.", annotationClass.getName(), targetClass.getName()));
		else
			return annotations[0];
	}

	@Override
	public <TAnnotation extends Annotation> void getZeroAnnotations(Object targetInstance, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetInstance == null)
			throw new ArgumentNullException("targetInstance");

		final Class<?> targetClass = targetInstance.getClass();

		if (targetClass == null)
			return;

		annotations = this.getAllAnnotations(targetInstance, annotationClass);

		if (annotations == null || annotations.length == 0)
			return;
		else
			throw new InvalidOperationException(String.format("Some custom attributes of type '%s' are defined on type '%s'.", annotationClass.getName(), targetClass.getName()));
	}

	@Override
	public Class<?> makeNonNullableClass(Class<?> conversionClass)
	{
		if (conversionClass == Byte.class)
			return byte.class;
		else if (conversionClass == Short.class)
			return byte.class;
		else if (conversionClass == Integer.class)
			return int.class;
		else if (conversionClass == Long.class)
			return long.class;
		else if (conversionClass == Character.class)
			return char.class;
		else if (conversionClass == Float.class)
			return float.class;
		else if (conversionClass == Double.class)
			return double.class;
		else if (conversionClass == Boolean.class)
			return boolean.class;
		else
			return null;
	}

	@Override
	public Class<?> makeNullableClass(Class<?> conversionClass)
	{
		if (conversionClass == byte.class)
			return Byte.class;
		else if (conversionClass == short.class)
			return Short.class;
		else if (conversionClass == int.class)
			return Integer.class;
		else if (conversionClass == long.class)
			return Long.class;
		else if (conversionClass == char.class)
			return Character.class;
		else if (conversionClass == float.class)
			return Float.class;
		else if (conversionClass == double.class)
			return Double.class;
		else if (conversionClass == boolean.class)
			return Boolean.class;
		else
			return conversionClass;
	}

	@Override
	public boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue) throws Exception
	{
		return this.setLogicalPropertyValue(targetInstance, propertyName, propertyValue, false, true);
	}

	@Override
	public boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue, boolean stayHard, boolean makeSoft) throws Exception
	{
		Class<?> targetClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			Method method;
			targetClass = targetInstance.getClass();

			if (targetClass == null)
				return false;

			propertyDescriptor = this.getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null &&
					(method = propertyDescriptor.getWriteMethod()) != null)
			{
				final Class<?> propertyClass = propertyDescriptor.getPropertyType();

				if (propertyClass == null)
					return false;

				propertyValue = this.getDataTypeFascade().changeClass(propertyClass, propertyValue);
				method.invoke(targetInstance, propertyValue);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null)
			{
				if (!makeSoft && !targetMap.containsKey(propertyName))
					return false;

				if (targetMap.containsKey(propertyName))
					targetMap.remove(propertyName);

				targetMap.put(propertyName, propertyValue);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean tryGetLogicalPropertyClass(Object targetInstance, String propertyName, MethodParameterModifier.Out<Class<?>> outPropertyClass) throws Exception
	{
		Class<?> targetClass, propertyClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;
		Object propertyValue;
		final boolean stayHard = false;
		final boolean makeSoft = true;

		if (outPropertyClass == null)
			throw new InvalidOperationException("outPropertyClass");

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			targetClass = targetInstance.getClass();

			propertyDescriptor = this.getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null)
			{
				propertyClass = propertyDescriptor.getPropertyType();
				outPropertyClass.setValue(propertyClass);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null && makeSoft &&
					targetMap.containsKey(propertyName))
			{
				propertyValue = targetMap.get(propertyName);

				if (propertyValue != null)
				{
					propertyClass = propertyValue.getClass();
					outPropertyClass.setValue(propertyClass);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean tryGetLogicalPropertyValue(Object targetInstance, String propertyName, MethodParameterModifier.Out<Object> outPropertyValue) throws Exception
	{
		Class<?> targetClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;
		Object propertyValue;
		final boolean stayHard = false;
		final boolean makeSoft = true;

		if (outPropertyValue == null)
			throw new InvalidOperationException("outPropertyValue");

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			Method method;
			targetClass = targetInstance.getClass();

			if (targetClass == null)
				return false;

			propertyDescriptor = this.getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null &&
					(method = propertyDescriptor.getReadMethod()) != null)
			{
				final Class<?> propertyClass = propertyDescriptor.getPropertyType();

				if (propertyClass == null)
					return false;

				propertyValue = method.invoke(targetInstance, null);
				propertyValue = this.getDataTypeFascade().changeClass(propertyClass, propertyValue);
				outPropertyValue.setValue(propertyValue);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null && makeSoft &&
					targetMap.containsKey(propertyName))
			{
				propertyValue = targetMap.get(propertyName);
				outPropertyValue.setValue(propertyValue);
				return true;
			}
		}

		return false;
	}
}
