package edu.wgu.zamzow.medalert.objects;

public class Med {
    private int applNo;
    private int prodNo;
    private String form;
    private String strength;
    private String DrugName;

    public int getApplNo() {
        return applNo;
    }

    public int getProdNo() {
        return prodNo;
    }

    public String getDrugName() {
        return DrugName;
    }

    public String getForm() {
        return form;
    }

    public String getStrength() {
        return strength;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public void setApplNo(int applNo) {
        this.applNo = applNo;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setProdNo(int prodNo) {
        this.prodNo = prodNo;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
