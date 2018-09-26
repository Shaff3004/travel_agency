package db.entity;

public class OrderStatistics {
    private String customer;
    private int customerId;
    private int orderCount;
    private int sum;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "OrderStatistics{" +
                "customer='" + customer + '\'' +
                ", customerId=" + customerId +
                ", orderCount=" + orderCount +
                ", sum=" + sum +
                '}';
    }
}
