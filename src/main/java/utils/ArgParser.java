package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgParser {


    public static final String COUNTRY_KEY = "country=";
    public static final String DEVICE_KEY = "device=";
    private static final String OR_DELIMITER = "or";

    public static final String WRONG_ARGS_MSG = "You didn't provide arguments in correct format e.g country=\"US OR GB\" ";
    public static final String COUNTRY_KEY_EMPTY_MSG = COUNTRY_KEY + "param is empty";
    public static final String DEVICE_KEY_EMPTY_MSG = DEVICE_KEY + "param is empty";

    /**
     * Parses country and device arguments. Working in 2 modes controlled by provided {@code key}
     * @param args Array of Strings with program arguments
     * @param key accept {@link ArgParser#COUNTRY_KEY} or {@link ArgParser#DEVICE_KEY}
     * @return List of parsed argument
     * @throws Exception thrown if argument cannot be parsed.
     */
    public static List<String> parseCountryDeviceArg(String[] args, String key) throws Exception {
        String concanateArgs = Arrays.toString(args);
        System.out.println(concanateArgs);

        if (!concanateArgs.contains(COUNTRY_KEY) || !concanateArgs.contains(DEVICE_KEY)) {
            throw new Exception(WRONG_ARGS_MSG);
        }
        if (args[0].split("=").length == 1) {
            throw new Exception(COUNTRY_KEY_EMPTY_MSG);
        }
        if (args[1].split("=").length == 1) {
            throw new Exception(DEVICE_KEY_EMPTY_MSG);
        }

        List<String> result = null;

        for (String arg : args) {
            arg = arg.trim().toLowerCase();
            if (arg.startsWith(key)) {
                result = extractItemsFromArg(arg, key);
                break;
            }
        }
        return result;
    }


    /**
     * Extract arguments for single argument
     * @param arg program argument
     * @param key accept {@link ArgParser#COUNTRY_KEY} or {@link ArgParser#DEVICE_KEY}
     * @return List of parsed argument
     */
    private static List<String> extractItemsFromArg(String arg, String key) {
        List<String> devices = new ArrayList<>();
        String devicesStr = arg.split(key)[1];
        if (devicesStr.contains(OR_DELIMITER)) {
            devices = Arrays.asList(devicesStr.split(OR_DELIMITER));
            devices = devices.stream().map(String::trim).collect(Collectors.toList());
        } else {
            devices.add(devicesStr.trim());
        }
        return devices;
    }
}
