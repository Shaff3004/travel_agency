package db.entity;

import java.util.Date;

public class OrderDateStatistic {
    private Date date;
    private int countOrder;
    private int totalPrice;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderDateStatistic{" +
                "date=" + date +
                ", countOrder=" + countOrder +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
