package db.entity;

import java.util.Date;

public class AccountDateStatistic {
    private Date date;
    private int count;
    private int totalPrice;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "AccountDateStatistic{" +
                "date=" + date +
                ", count=" + count +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
