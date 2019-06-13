import dai.H2;
import utils.ArgParser;

import java.util.List;

public class Execute {

    public static void main(String[] args) throws Exception {
        try {
            H2.init();
            List<String> countries = ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
            List<String> devices = ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
            H2.selectBugs(countries, devices).forEach(System.out::println);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
