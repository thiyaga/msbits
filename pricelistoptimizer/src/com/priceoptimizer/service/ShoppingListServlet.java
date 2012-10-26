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
import com.priceoptimizer.model.pricelist;
import com.priceoptimizer.model.Combinations;
import com.google.gson.stream.JsonWriter;
import com.google.gson.Gson;


@Path("/shoppinglist")
public class ShoppingListServlet {
	
	private static final long serialVersionUID = 1L;
	private DAO dao = new DAO();
	private pricelist pricelist =  new pricelist();
	private Combinations combinations = new Combinations();
	
	@POST
    @Path("new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newShoppingList(Shoppinglist shoppinglist )
	{
		StatusType statusCode = null;
		String msg = null;
		long key = 0;
		
				
		//StringWriter sw = new StringWriter();
		//JsonWriter writer = new JsonWriter(sw);
		Gson gson = new Gson();		
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
				dao.retreive_all_pricelist(pricelist,shoppinglist);
				priceoptimizer.generate_pricelist_selected(pricelist, shoppinglist);
				priceoptimizer.retreive_retialers_all(pricelist);
				priceoptimizer.retreive_retailers_best(pricelist);
				try
				{
				priceoptimizer.generate_all_combinations(combinations,pricelist);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				priceoptimizer.generate_all_combinations_pricelist(combinations, pricelist, shoppinglist);
				priceoptimizer.validate_combinations(combinations);
				priceoptimizer.set_total_msrsp(combinations);
				priceoptimizer.set_total_marsp(combinations);
				priceoptimizer.generate_optimized_combinations(combinations, pricelist);
				//writer.beginObject();
			   // writer.name("shoppinglistid").value(Long.toString(key));
				//writer.endObject();
				//writer.close();
				String json_output = gson.toJson(combinations);
				statusCode = Response.Status.OK;
				//msg = sw.toString();
				msg = json_output.toString();
			}
		} 
		catch (Exception e) {
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
