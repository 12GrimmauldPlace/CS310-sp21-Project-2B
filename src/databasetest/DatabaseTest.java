package databasetest;

import java.sql.*;
import java.util.LinkedHashMap;
import org.json.simple.*;

public class DatabaseTest {

    public static void main(String[] args) {
        DatabaseTest t = new DatabaseTest();
        t.getJSONData();
        
    }
    
    public JSONArray getJSONData(){
        
        JSONArray jsonArray = new JSONArray();                                  //to store each hashmap
        LinkedHashMap<String, String> records;                                  //to store each row
        Connection conn = null;                                                 //Connectino object
        PreparedStatement pstSelect = null;                                     // pstUpdate = null; //precompiled and stored query
        ResultSet resultset = null;                                             //holds tables (set of data returned by executing statement)
        ResultSetMetaData metadata = null;                                      //info about returned data
        
        String query, key, value;                                               //query holds the sql statement
        //String newFirstName = "Alfred", newLastName = "Neuman";               //USED TO UPDATE TABLE
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/p2_test");                 //ID data source; changed db_test to p2_test
            String username = "root";
            String password = "000414807";                                      //sign in with password
            System.out.println("Connecting to " + server + "...");
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();               //LOAD THE JDBC DRIVER   (Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'.)          
            
            /* Open Connection */
                                                                                //cxn btw app & db
            conn = DriverManager.getConnection(server, username, password);     //Connection object; drivermanager loads db drivers, manages cxn btw app & driver

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                
                // Prepare Update Query
                
               //query = "INSERT INTO people (firstname, lastname) VALUES (?, ?)"; //holds SQL statement to insert a new row into table "people"
                //pstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); //initialize preparedstatement object
                //pstUpdate.setString(1, newFirstName); //are these the values to insert for the question marks in query above?
                //pstUpdate.setString(2, newLastName);
                
                
                // Execute Update Query

                //updateCount = pstUpdate.executeUpdate(); //execute a query using the PreparedStatement object
                
                // Get New Key; Print To Console
                
                //if (updateCount > 0) {
            
                    //resultset = pstUpdate.getGeneratedKeys();

                    //if (resultset.next()) {

                        //System.out.print("Update Successful!  New Key: ");
                        //System.out.println(resultset.getInt(1));

                    //}

                //}
                
                //(SELECT QUERY is used to fetch the data from a database table 
                // which returns this data in the form of a result table. )
                
                /* Prepare Select Query */ 
                
                query = "SELECT * FROM people";                                 //selects all the columns from the table
                pstSelect = conn.prepareStatement(query);                       //initialize prepared statement object
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");                     //line executes
                
                hasresults = pstSelect.execute();                               //hasresults is boolean           
                
                /* Get Results */
                
                System.out.println("Getting Results ...");                      //line executes
                
                                   
                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {
                                                                  
                    
                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();                   //gets the table
                        metadata = resultset.getMetaData();                     //gets the table's metadata
                        columnCount = metadata.getColumnCount();                //gets column count (metadata)

                        /* Get Column Names; Print as Table Header */
                        
                        //for (int i = 2; i <= columnCount; i++) {

                            //key = metadata.getColumnLabel(i); //key value

                            //System.out.format("%20s", key); //output key 
                            
                            //keys.add(key);                                      //stores each key (column heading) as a string except for
                            

                        //}
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {                               //while there is a next row
                            
                            records = new LinkedHashMap<>();                           //to store key:value pairs for next row
                            
                            /* Begin Next ResultSet Row */

                            //System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                            for (int i = 2; i <= columnCount; i++) {            //for each column

                                value = resultset.getString(i);                 //gets the value for each column for current row
                               
                                if (resultset.wasNull()) {                      //determines whether the last column read had a Null value
                                    //System.out.format("%20s", "NULL");        //prints NULL
                                    records.put(metadata.getColumnLabel(i), "NULL");            //sets value to NULL
                                }

                                else {
                                    //System.out.format("%20s", value);         //print value  
                                    records.put(metadata.getColumnLabel(i), value);             //sets key-value pair
                                }

                            }
                            
                           jsonArray.add(records);

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {  
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }
                
            }
            
            
            
            System.out.println();
            
            /* Close Database Connection */
            
            conn.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            //if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
        
        return jsonArray;
  
    }
    
}