package model;

public class BusinessPriceObject implements Comparable<BusinessPriceObject>{
	Integer quantity;
	Double sale_percentage;

	public BusinessPriceObject() {
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getSale_percentage() {
		return sale_percentage;
	}

	public void setSale_percentage(Double sale_percentage) {
		this.sale_percentage = sale_percentage;
	}

	@Override
	public int compareTo(BusinessPriceObject o) {
		return Integer.compare(this.quantity,o.quantity);
	}
}
