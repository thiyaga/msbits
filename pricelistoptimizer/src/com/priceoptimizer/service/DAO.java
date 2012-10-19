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
import com.priceoptimizer.model.pricelist;


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
   
   
 
    public void retreive_all_pricelist(pricelist pricelist,Shoppinglist shoppinglist)
    {
    	
        try{
        	ResultSet rst = null;
        	String prod_id=null;
        	for(int i=0;i<shoppinglist.shoppingitems.size();i++)
        	{
        		if(prod_id==null)
        			prod_id = "'"+shoppinglist.shoppingitems.get(i).getprodid()+"'";
        		else
        		{
        			prod_id = prod_id+","+"'"+shoppinglist.shoppingitems.get(i).getprodid()+"'";
        		}
        	}
        	String query = "select p_list.prod_id,p_list.retailer_id,prod.prod_name,ret.retailer_name,prod.mrp_price,p_list.selling_price from price_list p_list,product prod,retailer ret "
        					+ "where prod.prod_id = p_list.prod_id and "
        					+ "ret.retailer_id = p_list.retailer_id and "
        					+ "p_list.prod_id in (" + prod_id + ");";
            stmt = getConnection().createStatement();
            rst = stmt.executeQuery(query);
            
            if(rst != null)
            {
            	while(rst.next())
            	{
            		pricelist pricelist_rst = new pricelist();
            		pricelist_rst.set_prod_id(rst.getInt("prod_id"));
            		pricelist_rst.set_retailer_id(rst.getInt("retailer_id"));
            		pricelist_rst.set_retailer_name(rst.getString("retailer_name"));
            		pricelist_rst.set_prod_name(rst.getString("prod_name"));
            		pricelist_rst.set_mrp(rst.getFloat("mrp_price"));
            		pricelist_rst.set_selling_price(rst.getFloat("selling_price"));
            		pricelist.add_pricelist_all(pricelist_rst);
            	}
            }
            
        } 
        catch (Exception e){
            e.printStackTrace ();
        }
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
