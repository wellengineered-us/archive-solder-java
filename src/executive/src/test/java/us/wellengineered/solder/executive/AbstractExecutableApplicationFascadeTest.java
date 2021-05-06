package us.wellengineered.solder.executive;

import us.wellengineered.solder.primitives.exceptions.InvalidOperationException;
import us.wellengineered.solder.utilities.AppConfigFascadeImpl;
import us.wellengineered.solder.utilities.DataTypeFascadeImpl;
import us.wellengineered.solder.utilities.PackageInfoFascade;
import us.wellengineered.solder.utilities.ReflectionFascadeImpl;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbstractExecutableApplicationFascadeTest
{
	public AbstractExecutableApplicationFascadeTest()
	{
	}

	private final String CMDLN_DEBUGGER_LAUNCH = "debug";
	private final String CMDLN_TOKEN_BASEDIR = "basedir";
	private final String CMDLN_TOKEN_PROPERTY = "property";
	private final String CMDLN_TOKEN_SOURCEFILE = "sourcefile";
	private final String CMDLN_TOKEN_SOURCESTRATEGY_AQTN = "sourcestrategy";
	private final String CMDLN_TOKEN_STRICT = "strict";
	private final String CMDLN_TOKEN_TEMPLATEFILE = "templatefile";

	public static class Model
	{
		public Model()
		{}

		private String value;

		public String getValue()
		{
			return this.value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	@Test
	public void shouldTestEasy() throws Exception
	{
		Boolean x = new DataTypeFascadeImpl().defaultValue(Boolean.class);

		final Map<String, Object> map = new LinkedHashMap<>();
		final Model model = new Model();

		boolean result = new ReflectionFascadeImpl(new DataTypeFascadeImpl()).setLogicalPropertyValue(model, "value", "zzz");

		System.out.println(result);
		System.out.println(model.getValue());

		result = new ReflectionFascadeImpl(new DataTypeFascadeImpl()).setLogicalPropertyValue(map, "value", "zzz");

		System.out.println(result);
		System.out.println(map.get("value"));


		PropertyDescriptor pd = new ReflectionFascadeImpl(new DataTypeFascadeImpl()).getLowestLogicalProperty(Model.class, "value");

		System.out.println(pd.getName());
		System.out.println(pd.getPropertyType());
		System.out.println(pd.getWriteMethod().getName());


		String z = new ReflectionFascadeImpl(new DataTypeFascadeImpl()).getErrors(new InvalidOperationException("xxx", new InvalidOperationException("yyy")), 0);
		System.out.println(z);

		final AbstractExecutableApplicationFascade executableApplicationFascade =
				new AbstractExecutableApplicationFascade(new DataTypeFascadeImpl(),
						new AppConfigFascadeImpl(new DataTypeFascadeImpl()), new ReflectionFascadeImpl(new DataTypeFascadeImpl()),
						new PackageInfoFascade()
						{
							@Override
							public String getName()
							{
								return "Test";
							}

							@Override
							public String getVersion()
							{
								return "1.0";
							}

							@Override
							public String getCompany()
							{
								return null;
							}

							@Override
							public String getConfiguration()
							{
								return null;
							}

							@Override
							public String getCopyright()
							{
								return null;
							}

							@Override
							public String getDescription()
							{
								return null;
							}

							@Override
							public String getProduct()
							{
								return null;
							}

							@Override
							public String getTitle()
							{
								return null;
							}

							@Override
							public String getTrademark()
							{
								return null;
							}
						})
				{
					@Override
					protected Map<String, ExecutableArgument<?>> getArgumentMap()
					{
						Map<String, ExecutableArgument<?>> argumentMap;

						argumentMap = new LinkedHashMap<>();
						argumentMap.put(CMDLN_TOKEN_TEMPLATEFILE, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_TEMPLATEFILE, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_SOURCEFILE, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_SOURCEFILE, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_BASEDIR, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_BASEDIR, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_SOURCESTRATEGY_AQTN, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_SOURCESTRATEGY_AQTN, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_STRICT, new ExecutableArgumentImpl<Boolean>(CMDLN_TOKEN_STRICT, true, true, Boolean.class, null));
						argumentMap.put(CMDLN_TOKEN_PROPERTY, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_PROPERTY, false, false, String.class, null));
						argumentMap.put(CMDLN_DEBUGGER_LAUNCH, new ExecutableArgumentImpl<Boolean>(CMDLN_DEBUGGER_LAUNCH, false, true, Boolean.class, null));

						return argumentMap;
					}

					@Override
					protected int onStartup(String[] args, Map<String, List<Object>> arguments)
					{
						return 0;
					}
				};

		executableApplicationFascade.entryPoint(new String[] { "-basedir:xxx", "-strict:123" });
	}
}
