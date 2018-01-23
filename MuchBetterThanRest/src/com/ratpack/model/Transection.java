package com.ratpack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transection
{
	@JsonProperty
    private Double amount;
	@JsonProperty
    private String description;
	@JsonProperty
    private String date;
	@JsonProperty
    private String currency;

    
    
    public Transection(@JsonProperty("amount") Double amount, @JsonProperty("description") String description, @JsonProperty("date") String date, @JsonProperty("currency") String currency) {
		super();
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.currency = currency;
	}

	public Double getAmount ()
    {
        return amount;
    }

    public void setAmount (Double amount)
    {
        this.amount = amount;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency.toUpperCase();
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", description = "+description+", date = "+date+", currency = "+currency+"]";
    }
}