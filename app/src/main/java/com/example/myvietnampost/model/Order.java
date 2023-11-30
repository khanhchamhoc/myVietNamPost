package com.example.myvietnampost.model;

public class Order {
    private String id;
    private String imageUrl;
    private String address;
    private boolean crossCheck;
    private String district;
    private String name;
    private String payment;
    private String phone;
    private String province;
    private String service;
    private String subDistrict;
    private int weight;

    public Order() {
    }

    public Order(String id, String imageUrl, String address, boolean crossCheck, String district, String name, String payment, String phone, String province, String service, String subDistrict, int weight) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.address = address;
        this.crossCheck = crossCheck;
        this.district = district;
        this.name = name;
        this.payment = payment;
        this.phone = phone;
        this.province = province;
        this.service = service;
        this.subDistrict = subDistrict;
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCrossCheck() {
        return crossCheck;
    }

    public void setCrossCheck(boolean crossCheck) {
        this.crossCheck = crossCheck;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", address='" + address + '\'' +
                ", crossCheck=" + crossCheck +
                ", district='" + district + '\'' +
                ", name='" + name + '\'' +
                ", payment='" + payment + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", service='" + service + '\'' +
                ", subDistrict='" + subDistrict + '\'' +
                ", weight=" + weight +
                '}';
    }
}
