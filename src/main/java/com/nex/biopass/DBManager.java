/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nex.biopass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author M
 */
public class DBManager {
//    // JDBC driver name and database URL
//    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//    static final String DB_URL = "jdbc:mysql://localhost/biopass";
//    
//    //  Database credentials
//    static final String USER = "root";
//    static final String PASS = "kumite2";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.11.36.130:3306/biopass";
    
    //  Database credentials
    static final String USER = "adminuTI1g75";
    static final String PASS = "SVczzGD1Vi1n";
    
    // This is a singleton class
    static DBManager singletonInstance = null;
    static Connection conn = null;
    
    DBManager(){}
    
    public static DBManager getInstance() throws SQLException, ClassNotFoundException
    {
        if(singletonInstance == null)
        {
            startConnection();
            singletonInstance = new DBManager();
        }
        return singletonInstance;
    }
    
    public static void startConnection() throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    public ResultSet execute(String query) throws SQLException
    {
        System.out.println("Creating statement...["+query+"]");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }
    
    public void executeUpdate(String query) throws SQLException
    {
        System.out.println("Creating update statement...["+query+"]");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }
    
    public Boolean usernameExists(String username) throws SQLException
    {
        Boolean exists = true;
        String query = "SELECT id FROM user WHERE username = \""+username+"\"";
        ResultSet rs = execute(query);
        if(!rs.next())
        {
            exists = false;
        }
        return exists;
    }
    
    public Boolean passwordMatch(String username, String password) throws SQLException
    {
        Boolean match = false;
        String query = "SELECT password FROM user WHERE username = \""+username+"\"";
        ResultSet rs = execute(query);
        while(rs.next())
        {
            if(rs.getString("password").equals(password))
            {
                match = true;
            }
        }
        return match;
    }
    
    public void insertUser(String username, String password) throws SQLException
    {
        System.out.println("Preparing statement... [Insert INTO user VALUES (" +
                username + ", " + password + ", 1.8) ");
        String query = "INSERT INTO user (username, password, dev) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setDouble(3, 1.8);
        statement.execute();
    }
    
    public void insertGrouping(String username, String message) throws SQLException
    {
        String query = "SELECT id FROM user WHERE username = \""+username+"\"";
        ResultSet rs = execute(query);
        Integer userId = -1;
        while(rs.next())
        {
            userId = rs.getInt("id");
        }
        query = "INSERT INTO grouping (user_id, message) VALUES ("+userId+",\""+message+"\")";
        System.out.println("Preparing statement... ["+query+"]");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet key = stmt.getGeneratedKeys();
        int lastId = 0;
        if (key.next())
        {
            lastId = key.getInt(1);
        }
        
        insertCaptures(new Grouping(message).getCaptures(), lastId);
    }
    
    public void insertCaptures(List<Capture> captures, int id) throws SQLException
    {
        String query = "";
        for(int i = 0; i < captures.size(); i++)
        {
            int ascii = captures.get(i).getAscii();
            int time  = captures.get(i).getTime();
            long start = captures.get(i).getStart();
            query = "INSERT INTO capture (grouping_id, ascii, start, time) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, ascii);
            statement.setLong(3, start);
            statement.setInt(4, time);
            statement.execute();
        }
    }
    
    public void close()
    {
        try {
            System.out.println("Closing Conneciton");
            conn.close();
            singletonInstance = null;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
