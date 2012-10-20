package com.priceoptimizer.model;

import java.util.ArrayList;
import java.util.List;

public class Combinations {
	
	public int number_of_retailers;
	public float total_mrp;
	public float total_selling_price;
	public float total_msrsp; //MSRSP - Minimum of Single Retailer Selling Price
	public float total_marsp; //MARSP - Minimum of All Retailer Selling Price 
	public List<Integer> retailer_combinations = new ArrayList<Integer>();
	public List<pricelist> pricelist_combinations = new ArrayList<pricelist>();
	public List<Combinations> all_combinations = new ArrayList<Combinations>();
	public List<Combinations> optimized_combinations = new ArrayList<Combinations>();
	
	public void set_number_of_retailers(int number_of_retailers)
	{
		this.number_of_retailers = number_of_retailers;
	}
	
	public void set_total_mrp(float total_mrp)
	{
		this.total_mrp = total_mrp;
	}
	
	public void set_total_selling_price(float total_selling_price)
	{
		this.total_selling_price = total_selling_price;
	}
	
	public void set_total_msrsp(float total_msrsp)
	{
		this.total_msrsp = total_msrsp;
	}
	
	public void set_total_marsp(float total_marsp)
	{
		this.total_marsp = total_marsp;
	}
	
	public void add_retailer_combination(int retailer_id)
	{
		this.retailer_combinations.add(Integer.valueOf(retailer_id));
	}
	
	public void add_pricelist_combinations(pricelist pricelist)
	{
		this.pricelist_combinations.add(pricelist);
	}
	
	public void add_all_combinations(Combinations combinations)
	{
		this.all_combinations.add(combinations);
	}
	
	public void add_optimized_combinations(Combinations combinations)
	{
		this.optimized_combinations.add(combinations);
	}

}
