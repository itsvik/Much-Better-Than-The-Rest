package com.ratpack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	@JsonProperty
	private Double balance;
	@JsonProperty
    private String currency;
	
	

	public User(@JsonProperty("balance") Double balance, @JsonProperty("currency") String currency) {
		super();
		this.balance = balance;
		this.currency = currency;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double d) {
		this.balance = d;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "User [balance=" + balance + ", currency=" + currency + "]";
	}

}
