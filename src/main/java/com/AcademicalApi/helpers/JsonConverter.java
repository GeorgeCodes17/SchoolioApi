package com.AcademicalApi.helpers;

import com.AcademicalApi.Main;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class JsonConverter {

    private final Gson gson = new Gson();

    public HashMap toHashmap(String jsonString) {
        return gson.fromJson(jsonString, HashMap.class);
    }

    public String toJson(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder jsonResult = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                jsonResult.append(columnValue);
            }
        }
        return jsonResult.toString();
    }

    public String toJson (ResultSet rs, String template) {
        StringBuilder response = new StringBuilder("[");

        try {
            final int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] currentObject = new Object[columnCount];
                for (int i = 1; i <= columnCount; ++i) {
                    currentObject[i-1] = rs.getObject(i);
                }
                response.append(String.format(template, currentObject));

                if (!rs.isLast()) {
                    response.append(",");
                }
            }
            response.append("]");
        } catch (SQLException e) {
            Main.logAll(Level.ERROR, e);
        }
        return response.toString();
    }
}
