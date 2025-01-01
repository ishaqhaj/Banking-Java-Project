package com.Application.model;

public class Address{
    private String addressString;
    private String city;
    private String postalCode;
    private String country;
    public Address(String address,String city,String postalCode,String country) {
    	this.addressString=address;
    	this.city=city;
    	this.postalCode=postalCode;
    	this.country=country;
    }
    public String getAddress() {
    	return addressString;
    }
    public String getCity() {
    	return city;
    }
    public String getPostalCode() {
    	return postalCode;
    }
    public String getCountry() {
    	return country;
    }
       
}
