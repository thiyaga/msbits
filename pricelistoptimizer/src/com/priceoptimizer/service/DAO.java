package com.priceoptimizer.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.sql.DataSource;
import java.sql.DriverManager;


public class DAO {

    private Connection conn;
    private boolean isConnected;
    private Statement stmt;

    public DAO() {
    	isConnected = false;
    }

    /*public void connect() {
    	String url = "java:comp/env/jdbc/pricingdb";
        
    	try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(url);
            conn = ds.getConnection();
            isConnected = true;   
        } 
    	catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    //To connect from local machine
    public void connect() 
    {
    	String dbUrl = "jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com/pricingdb";
    	
    
	try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection (dbUrl,"thiyaga","thiyaga");        
        isConnected = true;   
    } 
	catch (Exception e) {
        e.printStackTrace();
    }
}


   public long newShoppingList (String user){
        long key = 0;
        String sql = "insert into shopping_list (USER)"
                   + " values (\""+ user + "\")";                    				   
        
        try{
            stmt = getConnection().createStatement();
        	int rows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        	if (rows == 0) return key;
         	ResultSet rs = stmt.getGeneratedKeys();
         	if (rs.next()) {
         		key = rs.getLong(1);
         	}
        } 
        catch (Exception e){
            e.printStackTrace ();
        } 
        return key; 
    }  
   
       
    public void disconnect (){
        try{
            if (conn != null) {
                if (stmt != null) stmt.close ();
                conn.close ();
                isConnected = false;
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }

	public Connection getConnection() {
        if (isConnected == false)
        	connect();
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

}
