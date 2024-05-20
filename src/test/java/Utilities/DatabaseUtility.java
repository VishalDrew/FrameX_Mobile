package Utilities;

import Base.TestSetup;

import java.sql.*;
import java.util.*;

import static Utilities.Constants.*;


/**
 * DBConfig class.
 */
public class DatabaseUtility extends TestSetup {

    /**
     * Fetches data from the database based on the provided query.
     */
    public static List<Map<String, String>> fetchdatafromdb(String query) throws Exception {
        try (
                Connection con = DriverManager.getConnection(gettestserverurl(), LiveDbusername, LiveDbpassword);
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(query)) {

            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<Map<String, String>> dataList = new ArrayList<>();

            while (result.next()) {
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = result.getString(i);
                    rowData.put(columnName, columnValue);
                }
                dataList.add(rowData);
            }
            log.info("Data fetched successfully from the database");
            return dataList;
        } catch (SQLException  e) {
            log.error("Error fetching data from the database: " + e.getMessage());
        }
        return null;
    }


    /**
     * Retrieves a list of column names from the database based on the provided query and column name.
     *
     * @param query      the SQL query to execute
     * @param columnName the name of the column to retrieve values from
     * @return a list of column values
     * @throws Exception if an error occurs while fetching data from the database
     */
    public static List<String> getColumnNamesFromDatabase(String query, String columnName) throws Exception {
        List<String> stringMapValues = new ArrayList<>();
        try {
            List<Map<String, String>> result = fetchdatafromdb(query);
            for (Map<String, String> stringMap : result) {
                String value = stringMap.get(columnName);
                stringMapValues.add(value);
            }
            log.info("Column values fetched successfully from the database");
        } catch (Exception e) {
            log.error("Error fetching column values from the database: " + e.getMessage());
        }
        return stringMapValues;
    }



    /**
     * Retrieves data objects based on the provided query.
     *
     * @param query the SQL query to fetch data from the database
     * @return a list of data objects retrieved from the database
     * @throws Exception if an error occurs while fetching data from the database
     */
    public static List<Object> getDataObject(String query) throws Exception {
        List<Object> stringMapValues = new ArrayList<>();
        try {
            List<Map<String, String>> result = fetchdatafromdb(query);
            stringMapValues.addAll(result);
            log.info("Data objects fetched successfully from the database");
        } catch (Exception e) {
            log.error("Error fetching data objects from the database: " + e.getMessage());
        }
        return stringMapValues;
    }

    /**
     * Retrieves data from the database based on the provided query and column name.
     *
     * @param query      the SQL query to execute
     * @param columnName the name of the column to retrieve data from
     * @return the value of the specified column from the first row of the query result, or null if the result is empty
     * @throws Exception if an error occurs while fetching data from the database
     */
    public static String getdatafromdatabase(String query, String columnName) throws Exception {
        try {
            List<Map<String, String>> result = fetchdatafromdb(query);
            if (!result.isEmpty()) {
                Map<String, String> firstRow = result.get(0); // Assuming there's only one row
                String data = firstRow.get(columnName);
                log.info("Data retrieved successfully from the database");
                return data;
            } else {
                log.warn("No data found for the query: " + query);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching data from the database: " + e.getMessage());
            throw e;
        }
    }



    /*==============================FRAMENEW_MAIN PROJECTMASTER CONNECTION SETUP=====================================================*/


    public static String getProjectDataFromDatabase(String query, String columnName) throws Exception {
        try {
            List<Map<String, String>> result = executeframeQuery(query);
            if (!result.isEmpty()) {
                Map<String, String> firstRow = result.get(0); // Assuming there's only one row
                String data = firstRow.get(columnName);
                return data;
            } else {
                log.warn("No data found for the query: " + query);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching data from the database: " + e.getMessage());
            throw e;
        }
    }


    public static List<Map<String, String>> executeframeQuery(String query) throws Exception {
        try (
                Connection con = DriverManager.getConnection(framenewmainURL, LiveDbusername, LiveDbpassword);
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(isShopFrontPhotoRequired())) {

            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<Map<String, String>> dataList = new ArrayList<>();

            while (result.next()) {
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = result.getString(i);
                    rowData.put(columnName, columnValue);
                }

                dataList.add(rowData);
            }
            return dataList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}




