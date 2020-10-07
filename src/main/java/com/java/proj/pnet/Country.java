package com.java.proj.pnet;

import javax.persistence.*;

// Create table annotation
@Entity
public class Country {
	@Id
	private String alpha2Code;
	private String name;
	//private String callingCodes;
	private String capital;
	private String region;
	private String subregion;

	public String getAlpha2Code() {
		return alpha2Code;
	}

	public void setAlpha2Code(String alpha2Code) {
		this.alpha2Code = alpha2Code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSubregion() {
		return subregion;
	}

	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}
	 
	
/*	public Country(String code, String name, String callingCodes, 
			String capital, String region, String subregion) {
		this.code = code;
		this.name = name;
		this.capital = capital;
		this.callingCodes = callingCodes;
		this.region = region;
		this.subregion = subregion;
		
	}  */
 


}