package com.example.listycitylab3;

import java.io.Serializable;

// City class for holding name and province
// Added setters to update the city when editing
public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String province;

    public City(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
