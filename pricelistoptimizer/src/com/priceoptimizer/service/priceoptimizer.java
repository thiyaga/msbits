package com.priceoptimizer.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.priceoptimizer.model.Shoppinglist;
import com.priceoptimizer.model.pricelist;
import com.priceoptimizer.model.Combinations;
import com.priceoptimizer.service.CombinationGenerator;

public class priceoptimizer {
	
	public static void generate_pricelist_selected(pricelist pricelist,Shoppinglist shoppinglist)
	{
		boolean less_than_mrp=false;
		int prod_id;
		float min_selling_price;
		
		for(int i=0;i<shoppinglist.shoppingitems.size();i++)
		{	
			prod_id = shoppinglist.shoppingitems.get(i).getprodid();
			for(int j=0;j<pricelist.pricelist_all.size();j++)
			{
				if(prod_id == pricelist.pricelist_all.get(j).get_prod_id())
				{
					if(pricelist.pricelist_all.get(j).get_selling_price() < pricelist.pricelist_all.get(j).get_mrp())
					{
						less_than_mrp = true;
						pricelist.add_pricelist_less_mrp(pricelist.pricelist_all.get(j));
					}
					else
					{
						pricelist.add_pricelist_equal_mrp(pricelist.pricelist_all.get(j));
					}
				}
			}
			if(less_than_mrp == true)
			{
				min_selling_price = pricelist.pricelist_less_mrp.get(0).get_selling_price();
				for(int k=0;k<pricelist.pricelist_less_mrp.size();k++)
				{
					pricelist.add_pricelist_selected(pricelist.pricelist_less_mrp.get(k));					
					if(pricelist.pricelist_less_mrp.get(k).get_selling_price() < min_selling_price)
					{
						min_selling_price = pricelist.pricelist_less_mrp.get(k).get_selling_price();
					}
				}
			}
			else
			{
				min_selling_price = pricelist.pricelist_equal_mrp.get(0).get_selling_price();				
				for(int k=0;k<pricelist.pricelist_equal_mrp.size();k++)
				{
					pricelist.add_pricelist_selected(pricelist.pricelist_equal_mrp.get(k));
				}
				
			}
			
			for(int j=0;j<pricelist.pricelist_all.size();j++)
			{
				if(prod_id == pricelist.pricelist_all.get(j).get_prod_id())
				{
					pricelist.pricelist_all.get(j).set_min_selling_price(min_selling_price);

				}
			}
			
			less_than_mrp = false;
			min_selling_price = 0;
			pricelist.pricelist_less_mrp.clear();
			pricelist.pricelist_equal_mrp.clear();
		}		
		
	}
	
	public static void retreive_retialers_all(pricelist pricelist)
	{
		for(int i=0;i< pricelist.pricelist_selected.size();i++)
		{
			pricelist.add_retailers_all(pricelist.pricelist_selected.get(i).get_retailer_id());
		}
		
		//Remove the duplicate retailers using hash set
		HashSet<Integer> hs = new HashSet<Integer>();
		hs.addAll(pricelist.retailers_all);
		pricelist.retailers_all.clear();
		pricelist.retailers_all.addAll(hs);
		
	}
	
	public static void retreive_retailers_best(pricelist pricelist)
	{
		for(int i=0;i< pricelist.pricelist_selected.size();i++)
		{
			if(pricelist.pricelist_selected.get(i).get_min_selling_price() == pricelist.pricelist_selected.get(i).get_selling_price())
			{
				pricelist.add_retailers_best(pricelist.pricelist_selected.get(i).get_retailer_id());
			}
			
		}
		//Remove the duplicate retailers using hash set
		HashSet<Integer> hs = new HashSet<Integer>();
		hs.addAll(pricelist.retailers_best);
		pricelist.retailers_best.clear();
		pricelist.retailers_best.addAll(hs);
		
	}
	
	public static void generate_all_combinations(Combinations combinations,pricelist pricelist) throws Exception
	{
		int n = pricelist.retailers_all.size();
		int k = pricelist.retailers_best.size();
		
		for(int i=1;i<=k;i++)
		{
			int num_of_combinations;
			CombinationGenerator combination =new CombinationGenerator(n,i);
			num_of_combinations = combination.num_of_combinations(n,i);
			
			for(int j=0;j<num_of_combinations;++j)
			{
				combination.add_combination(combinations,i);
				combination = combination.Successor();
			}
		}

		
	}
		

}
