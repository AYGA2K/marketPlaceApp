package com.example.bestecommerce;

public class Constants {
	public static final String API_BASE_URL = "http://localhost:8080";
	public static final String GET_CATEGORIES_URL = API_BASE_URL + "/category/list";
	public static final String CREATE_CATEGORY_URL = API_BASE_URL + "/category/create";
	public static final String GET_CATEGORY_URL = API_BASE_URL + "/category/";
	public static final String UPDATE_CATEGORY_URL = API_BASE_URL + "/category/";
	public static final String DELETE_CATEGORY_URL = API_BASE_URL + "/category/";

	public static final String GET_PRODUCTS_URL = API_BASE_URL + "/product/list";
	public static final String CREATE_PRODUCT_URL = API_BASE_URL + "/product/create";
	public static final String GET_PRODUCT_URL = API_BASE_URL + "/product/";
	public static final String UPDATE_PRODUCT_URL = API_BASE_URL + "/product/";
	public static final String DELETE_PRODUCT_URL = API_BASE_URL + "/product/";
	public static final String TOGGLE_PRODUCT_AVAILABILITY_URL = API_BASE_URL + "/product/toggleavailability";
	public static final String GET_AVAILABLE_PRODUCTS_URL = API_BASE_URL + "/product/available";

	public static final String CATEGORIES_IMAGE_URL = API_BASE_URL+"/product/image";
	public static final String PRODUCTS_IMAGE_URL = API_BASE_URL+"/" ;
}
