package dai;

import model.Tester;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class H2 {

    public static final String H2_URL = "jdbc:h2:file:~/applause;IGNORECASE=TRUE";
    public static final String H2_LOGIN = "sa";
    public static final String H2_PASSWORD = "";


    public static final String DEVICES_SQL = "devices.csv";
    private static final String devicesSql = "CREATE TABLE Device " +
            "(ID integer NOT NULL, " +
            "name VARCHAR NOT NULL, " +
            "PRIMARY KEY ( id )) " +
            "AS " +
            "SELECT * FROM CSVREAD('" + DEVICES_SQL + "');";

    private static final String testersSql = "CREATE TABLE Tester " +
            "(ID integer NOT NULL, " +
            "first_name VARCHAR NOT NULL, " +
            "last_name VARCHAR NOT NULL, " +
            "country VARCHAR NOT NULL, " +
            "last_login TIMESTAMP,  " +
            "PRIMARY KEY ( id )) " +
            "AS " +
            "SELECT * FROM CSVREAD('testers.csv');";

    private static final String bugsSql = "CREATE TABLE bug " +
            "(ID integer NOT NULL, " +
            "DEVICE_ID integer NOT NULL, " +
            "TESTER_ID integer NOT NULL, " +
            "PRIMARY KEY ( id )," +
            "FOREIGN KEY (DEVICE_ID) REFERENCES DEVICE(ID), " +
            "FOREIGN KEY (TESTER_ID) REFERENCES TESTER(ID)) " +
            "AS " +
            "SELECT * FROM CSVREAD('bugs.csv');";


    private static final String testerDeviceSql = "CREATE TABLE tester_device " +
            "(TESTER_ID integer NOT NULL, " +
            "DEVICE_ID integer NOT NULL, " +
            "FOREIGN KEY (TESTER_ID) REFERENCES TESTER(ID), " +
            "FOREIGN KEY (DEVICE_ID) REFERENCES DEVICE(ID)) " +
            "AS " +
            "SELECT * FROM CSVREAD('tester_device.csv');";

    /**
     * Initialized H2 table and fills with data proviced in csv file
     * @throws SQLException
     */
    public static void init() throws SQLException {
        Statement st = prepareStatement();
        st.executeUpdate("drop table IF EXISTS bug");
        st.executeUpdate("drop table IF EXISTS tester_device");
        st.executeUpdate("drop table IF EXISTS device");
        st.executeUpdate("drop table IF EXISTS tester");
        st.executeUpdate(devicesSql);
        st.executeUpdate(testersSql);
        st.executeUpdate(bugsSql);
        st.executeUpdate(testerDeviceSql);
        closeStatement(st);
    }

    private static final String coreSql = "select t.id, t.FIRST_NAME, t.LAST_NAME, t.country, d.name device, count(b.id) experience " +
            "from TESTER t join tester_device td on t.id = td.TESTER_ID " +
            "         join device d on td.DEVICE_ID = d.ID " +
            "         left join bug b on b.tester_id = t.id and b.device_id = d.ID " +
            "         where 1=1 ";

    private static String tail = " group by t.id, d.id " +
            "order by experience desc;";

    private static final String ALL_PARAM = "all";


    /**
     * Searches Tester records with given parameters
     * @param countries List of tester's countries
     * @param devices  List of tester's devices
     * @return List of Testers
     * @throws SQLException if H2 database does accept provided SQL
     */
    public static synchronized List<Tester> selectBugs(List<String> countries, List<String> devices) throws SQLException {

        StringBuilder whereSql = new StringBuilder("");

        Function<String, String> addQuotes = s -> "\'" + s + "\'";
        if (!devices.contains(ALL_PARAM)) {
            whereSql.append(" and D.NAME in (");
            String result = devices.stream().map(addQuotes).collect(Collectors.joining(", "));
            whereSql.append(result);
            whereSql.append(")");
        }
        if (!countries.contains(ALL_PARAM)) {
            whereSql.append(" and T.COUNTRY in (");
            String result = countries.stream().map(addQuotes).collect(Collectors.joining(", "));
            whereSql.append(result);
            whereSql.append(")");
        }

        StringBuilder selectBugsSqlCore = new StringBuilder(coreSql);

        selectBugsSqlCore.append(whereSql);
        selectBugsSqlCore.append(tail);

        System.out.println(selectBugsSqlCore.toString());

        Statement st = prepareStatement();
        ResultSet rs = st.executeQuery(selectBugsSqlCore.toString());
        List<Tester> testers = new ArrayList<>();
        while (rs.next()) {
            testers.add(new Tester(
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("country"),
                    rs.getString("device"),
                    rs.getInt("experience")
            ));
        }
        rs.close();
        closeStatement(st);

        return testers;
    }

    /**
     * Init statement and connection
     * @return open statement
     * @throws SQLException if H2 database does accept provided SQL
     */
    private static Statement prepareStatement() throws SQLException {
        Connection conn = DriverManager.getConnection(H2_URL, H2_LOGIN, H2_PASSWORD);
        return conn.createStatement();
    }


    /**
     * Closing statement and connections
     * @param st Statement to be closed
     * @throws SQLException if H2 database does accept provided SQL
     */
    private static void closeStatement(Statement st) throws SQLException {
        st.getConnection().close();
        st.close();
    }

}
