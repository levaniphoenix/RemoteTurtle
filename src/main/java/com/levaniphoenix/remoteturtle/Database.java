package com.levaniphoenix.remoteturtle;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Database {

    public static final Connection Instance = createConnection();

    private static Connection createConnection(){
        String jdbcUrl = "jdbc:sqlite:db\\blocksdb.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection(){
        return Instance;
    }
    public static int addBlock(String name, double x, double y, double z) throws SQLException {
        PreparedStatement preparedStatement = Instance.prepareStatement("insert into blocks values(?, ?, ?,?)");
        preparedStatement.setBytes(1,name.getBytes(StandardCharsets.UTF_8));
        preparedStatement.setDouble(2,x);
        preparedStatement.setDouble(3,y);
        preparedStatement.setDouble(4,z);
        return preparedStatement.executeUpdate();
    }
    public static ResultSet getAllBlocks() throws SQLException {
        Statement statement = Instance.createStatement();
        return statement.executeQuery("select * from blocks");
    }
}
