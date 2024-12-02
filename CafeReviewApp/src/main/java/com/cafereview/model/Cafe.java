// Cafe.java
package com.cafereview.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cafe {
    private int cafeId;
    private String name;
    private String Address;
    private String phone;
    private boolean wifiAvailable;

    public int getCafeId() {
        return cafeId;
    }

    public void setCafeId(int cafeId) {
        this.cafeId = cafeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isWifiAvailable() {
        return wifiAvailable;
    }

    public void setWifiAvailable(boolean wifiAvailable) {
        this.wifiAvailable = wifiAvailable;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cafe cafe = (Cafe) o;
        return cafeId == cafe.cafeId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(cafeId);
    }
}
