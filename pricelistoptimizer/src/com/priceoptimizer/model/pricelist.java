package com.priceoptimizer.model;

import java.util.ArrayList;
import java.util.List;



public class pricelist {
	
	public int prod_id;
	public int retailer_id;
	public String prod_name;
	public String retailer_name;
	public float mrp;
	public float selling_price;
	public float min_selling_price;
	public float quantity;
	public float total_mrp; //mrp*quantity
	public float total_selling_price;//selling_price*quantity
	public List<Integer> retailers_all = new ArrayList<Integer> ();
	public List<Integer> retailers_best = new ArrayList<Integer> ();
	public List<pricelist> pricelist_all = new ArrayList<pricelist>(); 
	public List<pricelist> pricelist_less_mrp = new ArrayList<pricelist>();
	public List<pricelist> pricelist_equal_mrp = new ArrayList<pricelist>();
	public List<pricelist> pricelist_selected = new ArrayList<pricelist>();
	
	public void set_prod_id(int prod_id)
	{
		this.prod_id = prod_id;
	}
	
	public int get_prod_id()
	{
		return prod_id;
	}
	
	public void set_retailer_id(int retailer_id)
	{
		this.retailer_id = retailer_id;
	}
	
	public int get_retailer_id()
	{
		return retailer_id;
	}
	
	public void set_prod_name(String prod_name)
	{
		this.prod_name = prod_name;
	}
	
	public String get_prod_name()
	{
		return prod_name;
	}
	
		
	public void set_retailer_name(String retailer_name)
	{
		this.retailer_name = retailer_name;
	}
	
	public String get_retailer_name()
	{
		return retailer_name;
	}
	
	public void set_mrp(float mrp)
	{
		this.mrp = mrp;
	}
	
	public float get_mrp()
	{
		return mrp;
	}
	
		
	public void set_selling_price(float selling_price)
	{
		this.selling_price = selling_price;
	}
	
	
	public float get_selling_price()
	{
		return selling_price;
	}
	
	public void set_min_selling_price(float min_selling_price)
	{
		this.min_selling_price = min_selling_price;
	}
	
	public float get_min_selling_price()
	{
		return min_selling_price;
	}
	
	public void set_total_mrp(float total_mrp)
	{
		this.total_mrp = total_mrp;
	}
	
	public float get_total_mrp()
	{
		return total_mrp;
	}
	
	public void set_total_selling_price(float total_selling_price)
	{
		this.total_selling_price = total_selling_price;
	}
	
	public float get_total_selling_price()
	{
		return total_selling_price;
	}
	
	public void set_quantity(float quantity)
	{
		this.quantity = quantity;
	}
	
		
	public void add_pricelist_all(pricelist pricelist)
	{
		this.pricelist_all.add(pricelist);
	}
	
	public void add_pricelist_less_mrp(pricelist pricelist)
	{
		this.pricelist_less_mrp.add(pricelist);
	}
	
	public void add_pricelist_equal_mrp(pricelist pricelist)
	{
		this.pricelist_equal_mrp.add(pricelist);
	}
	
	public void add_pricelist_selected(pricelist pricelist)
	{
		this.pricelist_selected.add(pricelist);
	}
	
	public void add_retailers_all(int retailer_id)
	{
		this.retailers_all.add(Integer.valueOf(retailer_id));
		
	}
	
	public void add_retailers_best(int retailer_id)
	{
		this.retailers_best.add(Integer.valueOf(retailer_id));
	}
}

