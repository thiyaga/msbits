package com.priceoptimizer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.sql.DataSource;
import java.sql.DriverManager;

import com.priceoptimizer.model.Shoppinglist;


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


   /*public long newShoppingList (String user){
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
    }  */
    
    public long newShoppingList(Shoppinglist shoppinglist ) 
    {
    	long key = 0;
    	PreparedStatement insertshoppinglist = null;
    	PreparedStatement insertshoppingitem = null;
    	
    	String insertshoppinglist_sql = "INSERT INTO shopping_list(USER)"
    							  + " VALUES(?)";
    	
    	String insertshoppingitem_sql = "INSERT INTO shopping_list_items(SHOP_LIST_ID,PROD_ID,QTY)"
    								  + " VALUES(?,?,?)";
    		
    	try {
    		getConnection().setAutoCommit(false);
    		insertshoppinglist = getConnection().prepareStatement(insertshoppinglist_sql,PreparedStatement.RETURN_GENERATED_KEYS);
    		insertshoppingitem = getConnection().prepareStatement(insertshoppingitem_sql);
    		
    		
    		insertshoppinglist.setString(1,shoppinglist.getuser());
    		insertshoppinglist.executeUpdate();
    		ResultSet rs = insertshoppinglist.getGeneratedKeys();
    		if (rs != null && rs.next()) {
    		    key = rs.getLong(1);
    		}
    		
    		
    		for(int i=0;i<shoppinglist.shoppingitems.size();i++)
    		{
    			insertshoppingitem.setLong(1,key);
    			insertshoppingitem.setInt(2,shoppinglist.shoppingitems.get(i).getprodid());
    			insertshoppingitem.setInt(3,shoppinglist.shoppingitems.get(i).getquantity());
    			insertshoppingitem.executeUpdate();
    			
    		}
    		
    		getConnection().commit();
    		
    	}
    	catch (SQLException e ){
    		e.printStackTrace ();
    		if (getConnection() != null)
    		{
	    			try {
						getConnection().rollback();
					} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
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
