package ru.ivansmurygin.ocp.jdbc;

import javax.sql.RowSet;
import javax.sql.rowset.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by SmuryginIM on 19.03.2016.
 */
public class Runner {

    public final static String SELECT_ALL = "select * FROM FRIENDS";
    public final static String SELECT_ALL_COMP = "select * FROM COMPANIES";
    public final static String URL = "jdbc:derby://localhost:1527/"; // here we use
    // jdbc - stands for protocol
    // derby - subprotocol
    // localhost:1527 - address of the server DB with the local port
    public final static String DATABASE = "C:/dbs/tmpdb"; // DB name
    public final static String USER = "tmpuser";
    public final static String PW = "tmpuser";

    public static void main(String[] args) throws SQLException {
        joinRowSet();
    }

    public static Connection connectToDb() throws SQLException {
        return DriverManager.getConnection(URL + DATABASE, USER, PW);
    }

    public static void simpleExample(){
        try (Connection connection = connectToDb();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)){
            System.out.println("DB connection: successful ");
            System.out.println("ID \t Name");
            while (resultSet.next()){
                System.out.println(resultSet.getString(0) + "\t" +
                        resultSet.getString("Name"));
            }
        } catch (SQLException e) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(e);
        }

    }

    public static void scrollableExample(){

        try (Connection connection = connectToDb();
             Statement statement = connection.
                     createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)){
            System.out.println("DB connection: successful ");
            System.out.println("ID \t Name");
            resultSet.beforeFirst(); // here we can set the pointer to first or to last item
            // and fetch the data in the different order
            while (resultSet.next()){
                System.out.println(resultSet.getString("ID") + "\t" +
                        resultSet.getString("Name"));
            }

            resultSet.last();
            if ( resultSet.getInt(1) != 5){
                resultSet.afterLast();
                resultSet.moveToInsertRow();
                resultSet.updateString("ID", "5");
                resultSet.updateString("Name", "Darshan");
                resultSet.insertRow();
            }
            resultSet.close();

            try(ResultSet resultSet2 = statement.executeQuery(SELECT_ALL)){
                resultSet2.absolute(5);
                System.out.println("Result after update is");
                System.out.println(resultSet2.getInt(1) + "\t" + resultSet2.getString(2));
                resultSet2.deleteRow();
            }

            try (ResultSet resultSet3 = statement.executeQuery("select COUNT(*) FROM FRIENDS")){
                resultSet3.first();
                System.out.println("Number of strings after delete = " + resultSet3.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(e);
        }

    }

    public static final String GET_FRIEND_BY_ID = "select * from friends where id = ?";

    static void preparedStatement(){
        try (Connection connection = connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_FRIEND_BY_ID)) {
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("Friend was retrieved with name = %s", resultSet.getString("Name"));
        } catch (SQLException e) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(e);
        }
    }

    static void callableStatement(){
        try (Connection connection = connectToDb();
        CallableStatement cs = connection.prepareCall("{ call power (?, ?) }");) {
            cs.setDouble(1, 2);
            cs.setDouble(2, 2);
            boolean hasResults = cs.execute();

            while (hasResults) {
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    System.out.println("Result is " + rs.getInt(1));
                }
                rs.close();
                hasResults = cs.getMoreResults();
            }

        } catch (SQLException e) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(e);
        }
    }

    //////ROW set section
    static void filteredRowSet() throws SQLException{
        FilteredRowSet filteredRowSet = RowSetProvider.newFactory().createFilteredRowSet();
        filteredRowSet.setUrl(URL + DATABASE);
        filteredRowSet.setUsername(USER);
        filteredRowSet.setPassword(PW);

        filteredRowSet.setCommand(SELECT_ALL);
        filteredRowSet.execute();
        filteredRowSet.setFilter(new SimpleFilter("^[A-Z]{1}[a-z]{1,15}$"));
        while (filteredRowSet.next()){
            System.out.printf("User with name %s was retrieved \n", filteredRowSet.getString(2));
        }
    }

    static void rowSetWithListener() throws SQLException {
        //Creating and Executing RowSet
        JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
        rowSet.setUrl(URL + DATABASE);
        rowSet.setUsername(USER);
        rowSet.setPassword(PW);

        rowSet.setCommand(SELECT_ALL);
        rowSet.execute();

        //Adding Listener and moving RowSet
        rowSet.addRowSetListener(new MyListener());

        while (rowSet.next()) {
            // Generating cursor Moved event
            System.out.println("Id: " + rowSet.getString(1));
            System.out.println("Name: " + rowSet.getString(2));
        }

    }

    static void transactionDemonstration () throws SQLException {
        Connection connection = connectToDb();
        ResultSet resultSet = null;
        try {
            connection.setAutoCommit(false); // by default it is true
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet1 = statement.executeQuery("SELECT COUNT(*) FROM  friends");
            if (resultSet1.next()){
                System.out.println("Count before insert is " + resultSet1.getInt(1));
            }

            resultSet = statement.executeQuery(SELECT_ALL);
            resultSet.moveToInsertRow();
            resultSet.updateString("id", "6");
            resultSet.updateString("Name", "ABC");
            resultSet.insertRow();
            Savepoint savepoint1 = connection.setSavepoint("1");

            resultSet.moveToInsertRow();
            resultSet.updateString("id", "7");
            resultSet.updateString("Name", "Inconsistent data");
            resultSet.insertRow();

            connection.rollback(savepoint1);
            connection.commit();
            resultSet1 = statement.executeQuery("SELECT COUNT(*) FROM  friends");
            if (resultSet1.next()){
                System.out.println("Count after rollback is " + resultSet1.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(ex);
        }
    }

    static void retrieveMetaData(){
        try (Connection connection = connectToDb();
             Statement st = connection.createStatement();){
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("Useful metadata information is printed below");
            System.out.println("DB name = " + metaData.getDatabaseProductName());
            System.out.println("URL = " + metaData.getURL());
            System.out.println("User name = " + metaData.getUserName());
            System.out.println("\n");

            ResultSet resultSet = st.executeQuery(SELECT_ALL);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            System.out.println("Useful metadata about friends table is printed below");
            System.out.println("Columns count = " + rsMetaData.getColumnCount());
            System.out.println("Column 1 type = " + rsMetaData.getColumnTypeName(1));
            System.out.println("Column 2 type = " + rsMetaData.getColumnTypeName(2));

        } catch (SQLException ex) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(ex);
        }
    }

    static void prepareDelete(){
        try (Connection connection = connectToDb();
             Statement st = connection.createStatement();){
            int i = st.executeUpdate("delete from friends where id = 6");
            System.out.println("Number of rows affected is" + i);
        } catch (SQLException ex) {
            System.out.println("DB connection: Fail");
            throw new RuntimeException(ex);
        }
    }

    static void joinRowSet() throws SQLException {
        JoinRowSet joinRowSet = RowSetProvider.newFactory().createJoinRowSet();
        setRowSetConnectionParameters(joinRowSet);

        CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
        setRowSetConnectionParameters(cachedRowSet);
        cachedRowSet.setCommand(SELECT_ALL);
        cachedRowSet.execute();
        cachedRowSet.setMatchColumn(3);
        joinRowSet.addRowSet(cachedRowSet);

        CachedRowSet cachedRowSet2 = RowSetProvider.newFactory().createCachedRowSet();
        setRowSetConnectionParameters(cachedRowSet2);
        cachedRowSet2.setCommand(SELECT_ALL_COMP);
        cachedRowSet2.execute();
        cachedRowSet2.setMatchColumn(1);
        joinRowSet.addRowSet(cachedRowSet2);

        ResultSetMetaData metaData = joinRowSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%15s \t", metaData.getColumnName(i));
        }
        System.out.println();

        while (joinRowSet.next()){
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%15s \t", joinRowSet.getString(i));
            }
            System.out.println();
        }


    }

    static void setRowSetConnectionParameters(RowSet rowSet) throws SQLException {
        rowSet.setUrl(URL + DATABASE);
        rowSet.setUsername(USER);
        rowSet.setPassword(PW);
    }


}
