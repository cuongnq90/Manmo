package vn.manmo.search.object;

import java.io.Serializable;

/**
 * Created by NguyenQuocCuong on 11/14/2017.
 */

public class HotelAroundObject implements Serializable{
    private String name;
    private String address;
    private String phone;
    private String coordinates;
    private String id;
    private String price;
    private String pricehour;
    private String image;
    private String point;
    private String statusRoom;
    private String best;

    public HotelAroundObject() {
    }

    public HotelAroundObject(String name, String address, String phone, String coordinates, String id, String price, String pricehour, String image, String point, String statusRoom, String best) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.coordinates = coordinates;
        this.id = id;
        this.price = price;
        this.pricehour = pricehour;
        this.image = image;
        this.point = point;
        this.statusRoom = statusRoom;
        this.best = best;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricehour() {
        return pricehour;
    }

    public void setPricehour(String pricehour) {
        this.pricehour = pricehour;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getStatusRoom() {
        return statusRoom;
    }

    public void setStatusRoom(String statusRoom) {
        this.statusRoom = statusRoom;
    }

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }
}
