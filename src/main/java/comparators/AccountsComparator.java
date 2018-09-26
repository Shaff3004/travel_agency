package comparators;

import db.entity.Account;

import java.util.Comparator;

public class AccountsComparator implements Comparator<Account> {

    @Override
    public int compare(Account o1, Account o2) {
        return -(o1.getStatus().compareTo(o2.getStatus()));
    }
}
