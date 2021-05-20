package com.woodchat.server.dataBase;

import com.woodchat.server.StorageData;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class DataTable implements Closeable {
    Connection connection;
    String name;

    DataTable(String name) throws SQLException{
        this.connection = StorageData.getConnection();
        this.name = name;
    }

    @Override
    public void close() throws IOException {
        try {
            if(connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            StorageData.loggerDB.log(Level.WARNING,"Ошибка закрытия соединения: "+e);
        }
    }
    void executeStatement(String sqlQuery) throws SQLException{
        reopenConnection();
        Statement statement = connection.createStatement();
        statement.execute(sqlQuery);
    }

    void reopenConnection() throws SQLException{
        if(connection != null && !connection.isClosed())
            connection = StorageData.getConnection();
    }
}
