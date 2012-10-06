package com.priceoptimizer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import com.priceoptimizer.model.Shoppinglist;
import com.google.gson.stream.JsonWriter;

@Path("/shoppinglist")
public class ShoppingListServlet {
	
	private static final long serialVersionUID = 1L;
	private DAO dao = new DAO();
	
	@POST
    @Path("new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newShoppingList(Shoppinglist shoppinglist )
	{
		StatusType statusCode = null;
		String msg = null;
		long key = 0;
		
				
		StringWriter sw = new StringWriter();
		JsonWriter writer = new JsonWriter(sw);
		DAO dao = getDAO();
		
		try {			
		    dao.connect();
		    
		    
		    // Create a new shopping list (key = shopping list id)
			key = dao.newShoppingList(shoppinglist);
			if (key == 0) {
    			// Return 400 Bad Request
	    		statusCode = Response.Status.BAD_REQUEST;
			} 
			
			
			else {	
				writer.beginObject();
				writer.name("shoppinglistid").value(Long.toString(key));
				writer.endObject();
				writer.close();
				
				statusCode = Response.Status.OK;
				msg = sw.toString();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			// Return 500 Internal Server Error
    		statusCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		finally {
			dao.disconnect();
		}

		if (statusCode != Response.Status.OK)
			return Response.status(statusCode).build();
		else
			return Response.status(statusCode).entity(msg).build();	
		
	}
	
	protected DAO getDAO() {
		return dao;
	}

	protected void setDAO(DAO dao) {
		this.dao = dao;
	}

}
