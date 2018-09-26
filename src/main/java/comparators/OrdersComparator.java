package comparators;

import db.entity.Order;

import java.util.Comparator;

/**
 * Compare orders by status
 */
public class OrdersComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        return -(o1.getStatus().compareTo(o2.getStatus()));
    }
}
