package com.priceoptimizer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
			if(pricelist.pricelist_selected.get(i).get_min_selling_price() == pricelist.pricelist_selected.get(i).get_selling_price() && pricelist.pricelist_selected.get(i).get_min_selling_price() < pricelist.pricelist_selected.get(i).get_mrp())
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
		
		if(k==0)
		{
			k=1;
		}
		
		Map<String,Integer> retailer_index_map = new HashMap<String,Integer>();
		
		for(int i=0;i<n;i++)
		{
			retailer_index_map.put(String.valueOf(i), pricelist.retailers_all.get(i));
		}
		
		
		
		for(int i=1;i<=k;i++)
		{
			int num_of_combinations;
			CombinationGenerator combination =new CombinationGenerator(n,i);
			num_of_combinations = combination.num_of_combinations(n,i);
			
			for(int j=0;j<num_of_combinations;++j)
			{
				combination.add_combination(combinations,i,retailer_index_map);
				combination = combination.Successor();
			}
		}

		
	}
	
	public static void generate_all_combinations_pricelist(Combinations combinations,pricelist pricelist,Shoppinglist shoppinglist )
	{
		pricelist optimized_pricelist=null;;
		
		for(int i =0;i<combinations.all_combinations.size();i++)
		{
			int prod_id;
			
			for(int j=0;j<shoppinglist.shoppingitems.size();j++)
			{
				prod_id = shoppinglist.shoppingitems.get(j).getprodid();
				boolean first_time=true;
				for(int k=0;k<pricelist.pricelist_all.size();k++)
				{
					
					if(prod_id == pricelist.pricelist_all.get(k).get_prod_id())
					{
						int retailer_id;
						//boolean first_time=true;
						for(int l=0;l<combinations.all_combinations.get(i).retailer_combinations.size();l++)
						{
							retailer_id = combinations.all_combinations.get(i).retailer_combinations.get(l);
							if(retailer_id == pricelist.pricelist_all.get(k).get_retailer_id())
							{
								if(first_time==true)
								{
									optimized_pricelist = pricelist.pricelist_all.get(k);
									first_time = false;
								}
								if(pricelist.pricelist_all.get(k).get_selling_price() < optimized_pricelist.get_selling_price())
								{
									optimized_pricelist = pricelist.pricelist_all.get(k);
								}
								
							}
							
						}
					}
					
				}
				
				combinations.all_combinations.get(i).add_pricelist_combinations(optimized_pricelist);
				optimized_pricelist = null;
								
			}
		}
	}
	
	
	public static void validate_combinations(Combinations combinations)
	{
		boolean all_items_present;
		float total_mrp;
		float total_selling_price;
		for(int i=0;i<combinations.all_combinations.size();i++)
		{
			total_mrp=0;
			total_selling_price=0;
			all_items_present = true;
			for(int j=0;j<combinations.all_combinations.get(i).pricelist_combinations.size();j++)
			{
				if(combinations.all_combinations.get(i).pricelist_combinations.get(j)==null)
				{
					all_items_present = false;
				}
				else
				{
					total_mrp = total_mrp + combinations.all_combinations.get(i).pricelist_combinations.get(j).get_total_mrp();
					total_selling_price = total_selling_price + combinations.all_combinations.get(i).pricelist_combinations.get(j).get_total_selling_price();
				}
			}
			combinations.all_combinations.get(i).set_all_items_present(all_items_present);
			combinations.all_combinations.get(i).set_total_mrp(total_mrp);
			combinations.all_combinations.get(i).set_total_selling_price(total_selling_price);
		}
	}

	public static void set_total_msrsp(Combinations combinations)
	{
		float total_msrsp=0;
		boolean first_time=true;
		for(int i=0;i<combinations.all_combinations.size();i++)
		{
			if(combinations.all_combinations.get(i).get_number_of_retailers()==1 && combinations.all_combinations.get(i).get_all_items_present()==true)
			{
				if(first_time==true)
				{
					total_msrsp = combinations.all_combinations.get(i).get_total_selling_price();
					first_time=false;
				}
				
				if(combinations.all_combinations.get(i).get_total_selling_price() < total_msrsp)
				{
					total_msrsp = combinations.all_combinations.get(i).get_total_selling_price();
				}
				
			}

		}
		
		for(int i=0;i<combinations.all_combinations.size();i++)
		{
			combinations.all_combinations.get(i).set_total_msrsp(total_msrsp);
		}
		
	}
	
	
	public static void set_total_marsp(Combinations combinations)
	{
		float total_marsp=0;
		boolean first_time=true;
		for(int i=0;i<combinations.all_combinations.size();i++)
		{
			if(combinations.all_combinations.get(i).get_number_of_retailers()!=1 && combinations.all_combinations.get(i).get_all_items_present()==true)
			{
				if(first_time==true)
				{
					total_marsp = combinations.all_combinations.get(i).get_total_selling_price();
					first_time=false;
				}
				
				if(combinations.all_combinations.get(i).get_total_selling_price() < total_marsp)
				{
					total_marsp = combinations.all_combinations.get(i).get_total_selling_price();
				}
				
			}

		}
		
		for(int i=0;i<combinations.all_combinations.size();i++)
		{
			combinations.all_combinations.get(i).set_total_marsp(total_marsp);
		}
		
	}
	
	public static void generate_optimized_combinations(Combinations combinations,pricelist pricelist)
	{
		int max_number_retailers = pricelist.retailers_best.size();
		
		float min_total_selling_price=0;
		boolean first_time=true;
		
		if(max_number_retailers==0)
		{
			max_number_retailers=1;
		}
		
		for(int i=1;i<=max_number_retailers;i++)
		{
			
			first_time = true;
			for(int j=0;j<combinations.all_combinations.size();j++)
			{
				if(i==combinations.all_combinations.get(j).get_number_of_retailers() && combinations.all_combinations.get(j).get_all_items_present()==true)
				{
					if(first_time==true)
					{
						min_total_selling_price = combinations.all_combinations.get(j).get_total_selling_price();
						first_time = false;
					}
					if(combinations.all_combinations.get(j).get_total_selling_price() < min_total_selling_price)
					{
						min_total_selling_price = combinations.all_combinations.get(j).get_total_selling_price();
					}
				}

			}
			
			for(int j=0;j<combinations.all_combinations.size();j++)
			{
				if(i==combinations.all_combinations.get(j).get_number_of_retailers())
				{
					combinations.all_combinations.get(j).set_total_mwrsp(min_total_selling_price);
				}

			}
			min_total_selling_price = 0;

		}
		
		for(int i=0;i<combinations.all_combinations.size();i++)
		{	
			combinations.all_combinations.get(i).set_combination_id(i);
			if(combinations.all_combinations.get(i).get_all_items_present()==true)
			{
				if(combinations.all_combinations.get(i).get_total_mwrsp() == combinations.all_combinations.get(i).get_total_selling_price())
				{
					combinations.add_optimized_combinations(combinations.all_combinations.get(i));
				}
			}
			

		}
		
		combinations.all_combinations.clear();
	}

}
