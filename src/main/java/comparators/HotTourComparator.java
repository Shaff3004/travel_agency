package comparators;

import db.entity.Tour;

import java.util.Comparator;


/**
 * Compare tours by hot value
 */
public class HotTourComparator implements Comparator<Tour> {

    @Override
    public int compare(Tour o1, Tour o2) {
        if(o1.getHot() > o2.getHot()){
            return -1;
        }
        else if(o1.getHot() < o2.getHot()){
            return 1;
        }
        else {
            return 0;
        }
    }
}
