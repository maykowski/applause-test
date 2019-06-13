import dai.H2;
import java.sql.*;


public class H2Test {

    private static Statement prepareStatement() throws SQLException {
        Connection conn = DriverManager.getConnection(H2.H2_URL, H2.H2_LOGIN, H2.H2_PASSWORD);
        return conn.createStatement();
    }


    public static int getDevicesCount(String tableName) throws SQLException{
        int count = 0;
        Statement st = prepareStatement();
        final ResultSet rs = st.executeQuery("select count(*) cnt from " + tableName);
        while (rs.next()){
            count = rs.getInt("cnt");
        }
        st.close();
        return count;
    }



}
