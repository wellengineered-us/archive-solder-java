/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;

import static us.wellengineered.solder.primitives.Utils.failFastOnlyWhen;

public final class MethodParameterModifier
{
	public final static class In<TValue>
	{
		public In(Class<? extends TValue> valueClass, TValue value)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.valueClass = valueClass;
			this.value = value;

			if (value != null)
				failFastOnlyWhen(!this.getValueClass().isAssignableFrom(value.getClass()), String.format("Input parameter type '%s' is not compatible with value type '%s'.", this.getValueClass().getName(), value == null ? null : value.getClass().getName()));
		}

		private final Class<? extends TValue> valueClass;
		private final TValue value;

		public TValue getValue()
		{
			return this.value;
		}

		public Class<? extends TValue> getValueClass()
		{
			return this.valueClass;
		}
	}

	public final static class Out<TValue>
	{
		public Out()
		{
			this(Object.class);
		}

		public Out(Class<? super TValue> valueClass)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.value = null;
			this.set = false;
			this.valueClass = valueClass;
		}

		private final Class<? super TValue> valueClass;
		private boolean set;
		private TValue value;

		public TValue getValue()
		{
			failFastOnlyWhen(!this.isSet(), "Output parameter was not set.");
			return this.value;
		}

		public void setValue(TValue value)
		{
			failFastOnlyWhen(this.isSet(), "Output parameter has been set previously.");

			if (value != null)
				failFastOnlyWhen(!this.getValueClass().isAssignableFrom(value.getClass()), String.format("Output parameter type '%s' is not compatible with value type '%s'.", this.getValueClass().getName(), value == null ? null : value.getClass().getName()));

			this.value = value;
			this.setSet(true);
		}

		public boolean isSet()
		{
			return this.set;
		}

		private void setSet(boolean set)
		{
			this.set = set;
		}

		public Class<? super TValue> getValueClass()
		{
			return this.valueClass;
		}
	}

	public final static class Ref<TValue>
	{
		public Ref(Class<? extends TValue> valueClass)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.value = null;
			this.set = false;
			this.valueClass = valueClass;
		}

		public Ref(Class<? extends TValue> valueClass, TValue value)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.valueClass = valueClass;
			this.value = value;
			this.set = true;
		}

		private final Class<? extends TValue> valueClass;
		private boolean set;
		private TValue value;

		public TValue getValue()
		{
			failFastOnlyWhen(!this.isSet(), "Ref parameter was not set.");
			return this.value;
		}

		public void setValue(TValue value)
		{
			if (value != null)
				failFastOnlyWhen(!this.getValueClass().isAssignableFrom(value.getClass()), String.format("Ref parameter type '%s' is not compatible with value type '%s'.", this.getValueClass().getName(), value == null ? null : value.getClass().getName()));

			this.value = value;
			this.setSet(true);
		}

		public boolean isSet()
		{
			return this.set;
		}

		private void setSet(boolean set)
		{
			this.set = set;
		}

		public Class<? extends TValue> getValueClass()
		{
			return this.valueClass;
		}
	}
}
