package edu.wgu.zamzow.medalert.objects;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

public class Med implements Serializable {
    private int applNo;
    private int prodNo;
    private String form;
    private String strength;
    private String DrugName;
    private int freqType;
    private int freqNo;
    private Date startDate;
    private Time startTime;

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

    public Date getStartDate() {
        return startDate;
    }

    public int getFreqNo() {
        return freqNo;
    }

    public int getFreqType() {
        return freqType;
    }

    public Time getStartTime() {
        return startTime;
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

    public void setFreqNo(int freqNo) {
        this.freqNo = freqNo;
    }

    public void setFreqType(int freqType) {
        this.freqType = freqType;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
}
