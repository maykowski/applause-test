

import dai.H2;
import model.Tester;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ArgParser;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ApplauseTest {

    @BeforeClass
    public static void beforeAllTestMethods() {
        try {
            H2.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testParserWithCorrectValues(){
        try {
            String[] args = {"country=\"US\"", "device=\"iPhone 4\" OR \"Galaxy S4\""};
            List<String> countries = ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
            List<String> devices = ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
            assertEquals(countries.size(), 1);
            assertEquals(devices.size(), 2);

            String[] args2 = {"country=\"US\" or \"GB\"", "device=\"iPhone 4\""};
            countries = ArgParser.parseCountryDeviceArg(args2, ArgParser.COUNTRY_KEY);
            devices = ArgParser.parseCountryDeviceArg(args2, ArgParser.DEVICE_KEY);
            assertEquals(countries.size(), 2);
            assertEquals(devices.size(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testParserWithWrongValues(){
        try {
            String[] args = {"country=", "device=\"iPhone 4\" OR \"Galaxy S4\""};
            ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(ArgParser.COUNTRY_KEY_EMPTY_MSG));
        }

        try {
            String[] args = {"country=\"US\""};
            ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(ArgParser.WRONG_ARGS_MSG));
        }

        try {
            String[] args = {"country=\"US\"", "device="};
            ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(ArgParser.DEVICE_KEY_EMPTY_MSG));
        }

        try {
            String[] args = {"device=\"iPhone 4\""};
            ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(ArgParser.WRONG_ARGS_MSG));
        }
    }


    @Test
    public void testInitDB(){
        try {
            assertTrue("no entries in device table", H2Test.getDevicesCount("device") > 0);
            assertTrue("no entries in tester_device table", H2Test.getDevicesCount("tester_device") > 0);
            assertTrue("no entries in bug table", H2Test.getDevicesCount("bug") > 0);
            assertTrue("no entries in tester table", H2Test.getDevicesCount("tester") > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResultForSpecificArgs(){
        try {
            final String device1 = "iPhone 4";
            final String device2 = "Galaxy S4";
            final String country = "US";
            final String arg1 = "country=" + country;
            final String arg2 = "device=" + device1 + " OR " + device2;
            String[] args = {arg1, arg2};
            List<String> countries = ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
            List<String> devices = ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
            final List<Tester> testers = H2.selectBugs(countries, devices);

            final long testCountCountry = testers.stream().filter(tester -> tester.getCountry().equalsIgnoreCase(country)).count();
            assertEquals("Could not find correct result for " + arg1, testers.size(), testCountCountry);

            final long testCountDevice = testers.stream().filter(tester -> tester.getDevice().equalsIgnoreCase(device1) || tester.getDevice().equalsIgnoreCase(device2)).count();
            assertEquals("Could not find correct result for " + arg2, testers.size(), testCountDevice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResultForAllCountries(){
        try {
            final String device1 = "iPhone 4";
            final String device2 = "Galaxy S4";
            final String country = "ALL";
            final String arg1 = "country=" + country;
            final String arg2 = "device=" + device1 + " OR " + device2;
            String[] args = {arg1, arg2};

            List<String> countries = ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
            List<String> devices = ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
            List<Tester> testers = H2.selectBugs(countries, devices);

            boolean usEntries = testers.stream().filter(tester -> tester.getCountry().equalsIgnoreCase("US")).findAny().isPresent();
            boolean gbEntries = testers.stream().filter(tester -> tester.getCountry().equalsIgnoreCase("GB")).findAny().isPresent();
            boolean jpEntries = testers.stream().filter(tester -> tester.getCountry().equalsIgnoreCase("JP")).findAny().isPresent();
            assertTrue("Could not find correct result for " + arg1, usEntries && gbEntries && jpEntries);

            final long testCountDevice = testers.stream().filter(tester -> tester.getDevice().equalsIgnoreCase(device1) || tester.getDevice().equalsIgnoreCase(device2)).count();
            assertEquals("Could not find correct result for " + arg2, testers.size(), testCountDevice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResultForAllDevices(){
        try {
            final String device = "ALL";
            final String country = "US";
            final String arg1 = "country=" + country;
            final String arg2 = "device=" + device;
            String[] args = {arg1, arg2};

            List<String> countries = ArgParser.parseCountryDeviceArg(args, ArgParser.COUNTRY_KEY);
            List<String> devices = ArgParser.parseCountryDeviceArg(args, ArgParser.DEVICE_KEY);
            List<Tester> testers = H2.selectBugs(countries, devices);

            final long testCountCountry = testers.stream().filter(tester -> tester.getCountry().equalsIgnoreCase(country)).count();
            assertEquals("Could not find correct result for " + arg1, testers.size(), testCountCountry);

            boolean iPhoneEntries = testers.stream().filter(tester -> tester.getDevice().equalsIgnoreCase("iPhone 4")).findAny().isPresent();
            boolean galaxyEntries = testers.stream().filter(tester -> tester.getDevice().equalsIgnoreCase("Galaxy S3")).findAny().isPresent();
            boolean nexusEntries = testers.stream().filter(tester -> tester.getDevice().equalsIgnoreCase("Nexus 4")).findAny().isPresent();
            assertTrue("Could not find correct result for " + arg2, iPhoneEntries && galaxyEntries && nexusEntries);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
