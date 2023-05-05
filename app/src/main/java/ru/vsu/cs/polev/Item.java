package ru.vsu.cs.polev;

public class Item {
    String liter = "";
    String allCost = "";
    String price = "";
    String diff = "";

    public Item(String liter, String diff, String price, String allCost) {
        this.liter = liter;
        this.allCost = allCost;
        this.price = price;
        this.diff = diff;
    }


    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }


    public String getAllCost() {
        return allCost;
    }

    public void setAllCost(String allCost) {
        this.allCost = allCost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
