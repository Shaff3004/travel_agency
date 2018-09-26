package db.entity;

import java.util.Date;

/**
 * Order entity
 */
public class Order {
    private int id;
    private Tour tour;
    private User customer;
    private Date date;
    private String status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", tour=" + tour +
                ", customer=" + customer +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
