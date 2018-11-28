package model;

import com.google.gson.Gson;

import java.util.TreeSet;

public class BusinessPrice extends TreeSet<BusinessPriceObject> {

	@Override
	public String toString(){
		Gson gson = new Gson();
		return this == null ? null : gson.toJson(this);
	}
}

