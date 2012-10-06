package com.priceoptimizer.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Shoppinglist {
	private int id;
	private String user;	
	@XmlElement(name="items")
	public List<ShoppingItem> shoppingitems;
	
	public void setid(int id)
	{
		this.id = id;
	}
	
	public void setuser(String user)
	{
		this.user = user;
	}
	public int getid()
	{
		return id;
	}
	public String getuser()
	{
		return user;
	}

}
