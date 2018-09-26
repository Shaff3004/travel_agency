package db.entity;

import java.util.Date;


/**
 * Tour entity
 */
public class Tour {
    private int id;
    private String country;
    private String city;
    private String type;
    private int persons;
    private String hotel;
    private Date date;
    private double discountStep;
    private double maxDiscount;
    private int price;
    private int hot;
    private int sum;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDiscountStep() {
        return discountStep;
    }

    public void setDiscountStep(double discountStep) {
        this.discountStep = discountStep;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }


    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", persons=" + persons +
                ", hotel='" + hotel + '\'' +
                ", date=" + date +
                ", discountStep=" + discountStep +
                ", maxDiscount=" + maxDiscount +
                ", price=" + price +
                ", hot=" + hot +
                '}';
    }
}
