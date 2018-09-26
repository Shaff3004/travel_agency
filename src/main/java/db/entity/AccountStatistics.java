package db.entity;

public class AccountStatistics {
    private String customer;
    private int customerId;
    private int accountsCount;
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

    public int getAccountsCount() {
        return accountsCount;
    }

    public void setAccountsCount(int accountsCount) {
        this.accountsCount = accountsCount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "AccountStatistics{" +
                "customer='" + customer + '\'' +
                ", customerId='" + customerId + '\'' +
                ", accountsCount=" + accountsCount +
                ", sum=" + sum +
                '}';
    }
}
