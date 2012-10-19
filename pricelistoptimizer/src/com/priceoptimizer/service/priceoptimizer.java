package com.priceoptimizer.service;

import java.util.ArrayList;
import java.util.List;

import com.priceoptimizer.model.Shoppinglist;
import com.priceoptimizer.model.pricelist;

public class priceoptimizer {
	
	public static void generate_pricelist_selected(pricelist pricelist,Shoppinglist shoppinglist)
	{
		boolean less_than_mrp=false;
		//boolean equal_to_mrp=false;
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
	
	
	
		

}
