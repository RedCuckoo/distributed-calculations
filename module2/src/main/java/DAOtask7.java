import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOtask7 {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/module_2_RO";

    static final String USER = "postgres";
    static final String PASS = "postgres";

    Connection conn = null;
    Statement stmt = null;

    public DAOtask7() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Type> getAllTypesByProductId(int productId) {
        List<Type> typeList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT *  FROM product WHERE id= ?");
            preparedStatement.setInt(1, productId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");

                typeList.add(new Type(id, name, productId));
            }
            return typeList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeList;
    }

}
