package com.example.bestecommerce.models;

import java.util.List;

public class CategoriesModels {
	private int id;
	private String name;
	private List<ProductModels> products;

	public  CategoriesModels(int id, String name) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductModels> getProducts() {
		return products;
	}

	public void setProducts(List<ProductModels> products) {
		this.products = products;
	}
}
