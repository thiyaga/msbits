package com.priceoptimizer.service;

import com.priceoptimizer.model.Combinations;

public class CombinationGenerator {

	private int n = 0;
	private int k = 0;
	int[] data = null;
	
	public CombinationGenerator(int n, int k) throws Exception
	{
		if (n < 0 || k < 0) // normally n >= k
			throw new Exception("Negative parameter in constructor");

		this.n = n;
		this.k = k;
		 this.data = new int[k];
		    for (int i = 0; i < k; ++i)
		      this.data[i] = i;
	} 

	public  int num_of_combinations(int n, int k) throws Exception
	{
		if (n < 0 || k < 0)
			throw new Exception("Invalid negative parameter in Choose()"); 
		if (n < k)
			return 0;  // special case
		if (n == k)
			return 1;

		int delta, iMax;

		if (k < n-k) // ex: num_of_combinations(100,3)
		{
			delta = n-k;
			iMax = k;
		}
		else         // ex: num_of_combinations(100,97)
		{
			delta = k;
			iMax = n-k;
		}

		int ans = delta + 1;

		for (int i = 2; i <= iMax; ++i)
		{
			ans = (ans * (delta + i)) / i;  
		}

		return ans;
	}


	  public void add_combination(Combinations combination,int num_of_retailers)
	  {
		  Combinations generated_combinations = new Combinations();
		  generated_combinations.set_number_of_retailers(num_of_retailers);
		  for(int i=0;i<this.k;++i)
		  {
			  generated_combinations.add_retailer_combination(this.data[i]);
			  
		  }
		  combination.add_all_combinations(generated_combinations);
	  }
	  

	  public CombinationGenerator Successor() throws Exception
	  {
	    if (this.data[0] == this.n - this.k)
	      return null;

	    CombinationGenerator ans = new CombinationGenerator(this.n, this.k);

	    int i;
	    for (i = 0; i < this.k; ++i)
	      ans.data[i] = this.data[i];
	 
	    for (i = this.k - 1; i > 0 && ans.data[i] == this.n - this.k + i; --i)
	      ;
	 
	    ++ans.data[i];

	    for (int j = i; j < this.k - 1; ++j)
	      ans.data[j+1] = ans.data[j] + 1;

	    return ans;
	  }

}
