package com.example.myvietnampost.model.apiModel;

import java.util.List;

public class Division {
    private String name;
    private int code;
    private String division_type;
    private String code_name;
    private String phone_code;

    public Division(String name, int code, String division_type, String code_name, String phone_code, List<District> districts) {
        this.name = name;
        this.code = code;
        this.division_type = division_type;
        this.code_name = code_name;
        this.phone_code = phone_code;
        this.districts = districts;
    }

    private List<District> districts;

    public Division(String name, int code, String division_type, String code_name, String phone_code) {
        this.name = name;
        this.code = code;
        this.division_type = division_type;
        this.code_name = code_name;
        this.phone_code = phone_code;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDivision_type() {
        return division_type;
    }

    public void setDivision_type(String division_type) {
        this.division_type = division_type;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
}
