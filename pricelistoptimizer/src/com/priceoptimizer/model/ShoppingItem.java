package com.priceoptimizer.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShoppingItem {
	public int prod_id;
	public int quantity;
	public int shoplist_id;
	
	public ShoppingItem() {}
	
	public int getprodid()
	{
		return prod_id;
	}
	
	public int getquantity()
	{
		return quantity;
	}
	
	public int getshoplistid()
	{
		return shoplist_id;
	}
	

}
