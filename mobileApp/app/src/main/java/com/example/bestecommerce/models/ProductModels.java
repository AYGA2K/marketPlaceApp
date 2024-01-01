package com.example.bestecommerce.models;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductModels implements Item, Serializable {
	private String name ,desc, image;
	private double price;
	private boolean isAvailable;
	private int sellerId;
	private int id;

	public ProductModels( String name ,String desc, String image, double price, boolean isAvailable, int sellerId, int id) {
		this.name=name;
		this.desc = desc;
		this.image = image;
		this.price = price;
		this.isAvailable = isAvailable;
		this.sellerId = sellerId;
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public BigDecimal getItemPrice() {
		return BigDecimal.valueOf(price);
	}

	@Override
	public String getItemName() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
