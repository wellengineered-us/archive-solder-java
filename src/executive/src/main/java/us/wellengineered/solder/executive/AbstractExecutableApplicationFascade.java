/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.MethodParameterModifier;
import us.wellengineered.solder.primitives.Utils;
import us.wellengineered.solder.primitives.Message;
import us.wellengineered.solder.primitives.MessageImpl;
import us.wellengineered.solder.primitives.Severity;
import us.wellengineered.solder.utilities.AppConfigFascade;
import us.wellengineered.solder.utilities.DataTypeFascade;
import us.wellengineered.solder.utilities.PackageInfoFascade;
import us.wellengineered.solder.utilities.ReflectionFascade;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static us.wellengineered.solder.primitives.Utils.failFastOnlyWhen;

public abstract class AbstractExecutableApplicationFascade extends AbstractLifecycle<Exception, Exception> implements ExecutableApplicationFascade
{
	protected AbstractExecutableApplicationFascade(DataTypeFascade dataTypeFascade, AppConfigFascade appConfigFascade, ReflectionFascade reflectionFascade, PackageInfoFascade packageInfoFascade)
	{
		if (dataTypeFascade == null)
			throw new ArgumentNullException("dataTypeFascade");

		if (appConfigFascade == null)
			throw new ArgumentNullException("appConfigFascade");

		if (reflectionFascade == null)
			throw new ArgumentNullException("reflectionFascade");

		if (packageInfoFascade == null)
			throw new ArgumentNullException("packageInfoFascade");

		this._hcf = new Thread(this::_haltAndCatchFire);

		this.dataTypeFascade = dataTypeFascade;
		this.appConfigFascade = appConfigFascade;
		this.reflectionFascade = reflectionFascade;
		this.packageInfoFascade = packageInfoFascade;
	}

	private final Thread _hcf;
	private static final String APPCONFIG_ID_REGEX_UNBOUNDED = "[a-zA-Z_\\.][a-zA-Z_\\.0-9]";
	private static final String APPCONFIG_ARGS_REGEX = "-(" + APPCONFIG_ID_REGEX_UNBOUNDED + "{0,63}):(.{0,})";
	private static final String APPCONFIG_PROPS_REGEX = "(" + APPCONFIG_ID_REGEX_UNBOUNDED + "{0,63})=(.{0,})";
	private static final String SOLDER_HOOK_UNHANDLED_EXCEPTIONS = "SOLDER_HOOK_UNHANDLED_EXCEPTIONS";
	private static final String SOLDER_LAUNCH_DEBUGGER_ON_ENTRY_POINT = "SOLDER_LAUNCH_DEBUGGER_ON_ENTRY_POINT";

	private final AppConfigFascade appConfigFascade;
	private final PackageInfoFascade packageInfoFascade;
	private final DataTypeFascade dataTypeFascade;
	private final ReflectionFascade reflectionFascade;

	private static String getAppConfigArgsRegexPattern()
	{
		return APPCONFIG_ARGS_REGEX;
	}

	private static String getAppConfigIdRegexUnboundedPattern()
	{
		return APPCONFIG_ID_REGEX_UNBOUNDED;
	}

	private static String getAppConfigPropsRegexPattern()
	{
		return APPCONFIG_PROPS_REGEX;
	}

	private AppConfigFascade getAppConfigFascade()
	{
		return this.appConfigFascade;
	}

	private DataTypeFascade getDataTypeFascade()
	{
		return this.dataTypeFascade;
	}

	private PackageInfoFascade getPackageInfoFascade()
	{
		return this.packageInfoFascade;
	}

	private ReflectionFascade getReflectionFascade()
	{
		return this.reflectionFascade;
	}

	private static String getHookUnhandledExceptionsEnvKey()
	{
		return SOLDER_HOOK_UNHANDLED_EXCEPTIONS;
	}

	private static String getLaunchDebuggerOnEntryPointEnvKey()
	{
		return SOLDER_LAUNCH_DEBUGGER_ON_ENTRY_POINT;
	}

	@Override
	protected void create(boolean creating) throws Exception
	{
		if (creating)
		{
			Runtime.getRuntime().addShutdownHook(this._hcf);
		}
	}

	@Override
	protected void dispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			Runtime.getRuntime().removeShutdownHook(this._hcf);
		}
	}

	private final void _haltAndCatchFire()
	{
		// this method gets called on another thread from main()...
		try
		{
			this.onShutdown();
		}
		catch (Exception ex)
		{
			Utils.failFastWithException(ex);
		}
	}

	protected static boolean isRunningDebugMode()
	{
		final boolean isDebug =
				java.lang.management.ManagementFactory.getRuntimeMXBean().
						getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

		return isDebug;
	}

	protected void onShutdown() throws Exception
	{
		// this method gets called on another thread from main()...
		// do nothing
	}

	protected void displayArgumentErrorMessage(Iterable<Message> argumentMessages)
	{
		if (argumentMessages == null)
			throw new ArgumentNullException("argumentMessages");

		System.out.print(System.lineSeparator());
		for (Message argumentMessage : argumentMessages)
			System.out.println(argumentMessage.getDescription());
	}

	protected void displayArgumentMapMessage(Map<String, ExecutableArgument<?>> argumentMap)
	{
		String argumentTokens;

		if (argumentMap == null)
			throw new ArgumentNullException("argumentMap");

		argumentTokens = Utils.EMPTY_STRING;

		for (Map.Entry<String, ExecutableArgument<?>> argumentEntry : argumentMap.entrySet())
		{
			if (argumentEntry == null)
				continue;

			final ExecutableArgument<?> executableArgument = argumentEntry.getValue();

			if (executableArgument == null)
				continue;

			argumentTokens += " " + (!executableArgument.isRequired() ? "[" : Utils.EMPTY_STRING) +
					String.format("-%s:value%s", executableArgument.getName(), !executableArgument.isBounded() ? "(s)" : Utils.EMPTY_STRING) +
					(!executableArgument.isRequired() ? "]" : Utils.EMPTY_STRING);
		}

		System.out.print(System.lineSeparator());
		System.out.println(String.format("USAGE: %s %s", this.getPackageInfoFascade().getName(), argumentTokens));
	}

	protected void displayBannerMessage()
	{
		System.out.println(String.format("%s v%s",
				this.getPackageInfoFascade().getName(),
				this.getPackageInfoFascade().getVersion()));
	}

	protected void displayFailureMessage(Exception exception)
	{
		if (exception == null)
			throw new ArgumentNullException("exception");

		System.out.print(System.lineSeparator());
		System.out.println(exception != null ? this.getReflectionFascade().getErrors(exception, 0) : "<unknown>");

		System.out.print(System.lineSeparator());
		System.out.println(String.format("The operation failed to complete."));
	}

	protected void displayRawArgumentsMessage(String[] args, Iterable<String> arguments)
	{
		if (args == null)
			throw new ArgumentNullException("args");

		if (arguments == null)
			throw new ArgumentNullException("arguments");

		System.out.print(System.lineSeparator());
		System.out.println(String.format("RAW CMDLN: %s", String.join(" ", args)));
		System.out.print(System.lineSeparator());
		System.out.println(String.format("RAW ARGS:"));

		int index = 0;
		for (String argument : arguments)
			System.out.println(String.format("[%s] => %s", index++, argument));
	}

	protected void displaySuccessMessage(double durationSeconds)
	{
		System.out.print(System.lineSeparator());
		System.out.println(String.format("Operation completed successfully; duration: '%s'.", durationSeconds));
	}

	protected boolean shouldHookUnhandledExceptions()
	{
		String svalue;
		boolean ovalue;

		svalue = System.getenv(getHookUnhandledExceptionsEnvKey());

		if (svalue == null)
			return false;

		final MethodParameterModifier.Out<Boolean> outValue = new MethodParameterModifier.Out<>(Boolean.class);

		if (!this.getDataTypeFascade().tryParse(Boolean.class, svalue, outValue))
			return false;

		ovalue = Utils.getValueOrDefault(outValue.getValue(), false);

		return !isRunningDebugMode() && ovalue;
	}

	protected boolean shouldLaunchDebuggerOnEntryPoint()
	{
		String svalue;
		boolean ovalue;

		svalue = System.getenv(getLaunchDebuggerOnEntryPointEnvKey());

		if (svalue == null)
			return false;

		final MethodParameterModifier.Out<Boolean> outValue = new MethodParameterModifier.Out<>(Boolean.class);

		if (!this.getDataTypeFascade().tryParse(Boolean.class, svalue, outValue))
			return false;

		ovalue = Utils.getValueOrDefault(outValue.getValue(), false);

		return !isRunningDebugMode() && ovalue;
	}

	protected abstract Map<String, ExecutableArgument<?>> getArgumentMap();

	protected final void maybeLaunchDebugger()
	{
		if (this.shouldLaunchDebuggerOnEntryPoint())
			Utils.nop();
	}

	private Map<String, List<String>> parseCommandLineArguments(String[] args)
	{
		Map<String, List<String>> arguments;
		Pattern pattern;
		Matcher matcher;
		String key, value;
		List<String> argumentValues;

		if (args == null)
			throw new ArgumentNullException("args");

		arguments = new LinkedHashMap<>();
		pattern = Pattern.compile(getAppConfigArgsRegexPattern(), Pattern.CASE_INSENSITIVE);

		failFastOnlyWhen(pattern == null, "pattern == null");

		for (String arg : args)
		{
			matcher = pattern.matcher(arg);

			if (matcher == null)
				continue;

			if (!matcher.matches())
				continue;

			if (matcher.groupCount() != (3 - 1))
				continue;

			key = matcher.group(1).toString();
			value = matcher.group(2).toString();

			// key is required
			if (this.getDataTypeFascade().isNullOrWhiteSpace(key))
				continue;

			// val is required
			if (this.getDataTypeFascade().isNullOrWhiteSpace(value))
				continue;

			if (!arguments.containsKey(key))
				arguments.put(key, new ArrayList<>());

			argumentValues = arguments.get(key);

			// duplicate values are ignored
			if (argumentValues.contains(value))
				continue;

			argumentValues.add(value);
		}

		return arguments;
	}

	public final int showNestedExceptionsAndThrowBrickAtProcess(Exception ex)
	{
		if (ex == null)
			throw new ArgumentNullException("ex");

		this.displayFailureMessage(ex);

		return Utils.failFastWithException(ex);
	}

	private boolean tryParseCommandLineArgumentProperty(String arg, MethodParameterModifier.Out<String> outKey, MethodParameterModifier.Out<String> outValue)
	{
		Pattern pattern;
		Matcher matcher;
		String key, value;

		if (arg == null)
			throw new ArgumentNullException("arg");

		if (outKey == null)
			throw new ArgumentNullException("outKey");

		if (outValue == null)
			throw new ArgumentNullException("outValue");

		pattern = Pattern.compile(getAppConfigPropsRegexPattern(), Pattern.CASE_INSENSITIVE);

		failFastOnlyWhen(pattern == null, "pattern == null");

		matcher = pattern.matcher(arg);

		if (matcher == null)
			return false;

		if (!matcher.matches())
			return false;

		if (matcher.groupCount() != (3 - 1))
			return false;

		key = matcher.group(1).toString();
		value = matcher.group(2).toString();

		// key is required
		if (this.getDataTypeFascade().isNullOrWhiteSpace(key))
			return false;

		// val is required
		if (this.getDataTypeFascade().isNullOrWhiteSpace(value))
			return false;

		outKey.setValue(key);
		outValue.setValue(value);

		return true;
	}

	public final int entryPoint(String[] args)
	{
		this.maybeLaunchDebugger();

		if (this.shouldHookUnhandledExceptions())
			return this.tryStartup(args);
		else
			return this.startup(args);
	}

	protected abstract int onStartup(String[] args, Map<String, List<Object>> arguments);

	private int startup(String[] args)
	{
		int returnCode;
		Map<String, ExecutableArgument<?>> argumentMap;
		List<Message> argumentValidationMessages;

		List<String> argumentValues;
		Map<String, List<String>> arguments;

		Map<String, List<Object>> finalArguments;
		List<Object> finalArgumentValues;
		Object finalArgumentValue;

		final Instant startInstant = Instant.now();

		this.displayBannerMessage();

		arguments = this.parseCommandLineArguments(args);
		argumentMap = this.getArgumentMap();

		finalArguments = new LinkedHashMap<>();
		argumentValidationMessages = new ArrayList<>();

		if (argumentMap != null)
		{
			for (String argumentToken : argumentMap.keySet())
			{
				boolean argumentExists;
				int argumentValueCount = 0;
				ExecutableArgument<?> argumentSpec;

				final MethodParameterModifier.Out<List<String>> outArgumentValues = new MethodParameterModifier.Out<>();

				if (argumentExists = Utils.MapExtensionMethods.tryMapGetValue(arguments, argumentToken, outArgumentValues))
				{
					if ((argumentValues = outArgumentValues.getValue()) != null)
						argumentValueCount = argumentValues.size();
				}
				else
					argumentValues = null;

				final MethodParameterModifier.Out<ExecutableArgument<?>> outArgumentSpec = new MethodParameterModifier.Out<>();

				if (!Utils.MapExtensionMethods.tryMapGetValue(argumentMap, argumentToken, outArgumentSpec))
					continue;

				if ((argumentSpec = outArgumentSpec.getValue()) == null)
					continue;

				if (argumentSpec.isRequired() && !argumentExists)
				{
					argumentValidationMessages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("A required argument was not specified: '%s'.", argumentToken), Severity.ERROR));
					continue;
				}

				if (argumentSpec.isBounded() && argumentValueCount > 1)
				{
					argumentValidationMessages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("A bounded argument was specified more than once: '%s'.", argumentToken), Severity.ERROR));
					continue;
				}

				if (argumentValues != null)
				{
					finalArgumentValues = new ArrayList<>();

					if (argumentSpec.getValueClass() != null)
					{
						for (String argumentValue : argumentValues)
						{
							// explicit generic type erasure here
							final Class clazz = argumentSpec.getValueClass();
							final MethodParameterModifier.Out outFinalArgumentValue = new MethodParameterModifier.Out(clazz);

							if (!this.getDataTypeFascade().tryParse(argumentSpec.getValueClass(), argumentValue, outFinalArgumentValue))
								argumentValidationMessages.add(new MessageImpl(Utils.EMPTY_STRING, String.format("An argument '%s' value '%s' was specified that failed to parse to the target type '%s'.", argumentToken, argumentValue, argumentSpec.getValueClass().getName()), Severity.ERROR));
							else
								finalArgumentValues.add((finalArgumentValue = outFinalArgumentValue.getValue()));
						}
					}
					else
					{
						for (String argumentValue : argumentValues)
							finalArgumentValues.add(argumentValue);
					}

					finalArguments.put(argumentToken, finalArgumentValues);
				}
			}
		}

		if (!argumentValidationMessages.isEmpty())
		{
			this.displayArgumentErrorMessage(argumentValidationMessages);
			this.displayArgumentMapMessage(argumentMap);
			//this.displayRawArgumentsMessage(args);

			returnCode = -1;
		}
		else
			returnCode = this.onStartup(args, finalArguments);

		final Instant endInstant = Instant.now();
		final Double durationSeconds = ChronoUnit.MILLIS.between(startInstant, endInstant) / 1000.0;

		this.displaySuccessMessage(durationSeconds);

		return returnCode;
	}

	private int tryStartup(String[] args)
	{
		try
		{
			return this.startup(args);
		}
		catch (Exception ex)
		{
			return this.showNestedExceptionsAndThrowBrickAtProcess(new Exception("Main", ex));
		}
	}

	public static <TConsoleApp extends ExecutableApplicationFascade> int run(String[] args, Class<? extends TConsoleApp> clazz) throws Exception
	{
		if (args == null)
			throw new ArgumentNullException("args");

		if (clazz == null)
			throw new ArgumentNullException("clazz");

		try (TConsoleApp program = Utils.newObjectFromClass(clazz))
		{
			program.create();
			return program.entryPoint(args);
		}
	}
}
