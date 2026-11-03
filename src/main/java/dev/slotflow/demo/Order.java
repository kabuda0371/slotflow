package dev.slotflow.demo;

/**
 * 领域对象：订单（纯演示，无框架依赖）。
 */
public class Order {

    private final String id;
    private String country;
    private double amount;
    private Double taxRate;   // 税率，由补全卡槽填充
    private String status = "NEW";
    private String discountLevel = "NONE";

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountLevel() {
        return discountLevel;
    }

    public void setDiscountLevel(String discountLevel) {
        this.discountLevel = discountLevel;
    }
}
