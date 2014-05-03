/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.util.Date;

/**
 *
 * @author tomanlu2
 */
public class Borrowing {
    private final Date start;
    private final Date end;
    private final boolean isReturned;

    public Borrowing(Date start, Date end, boolean isReturned) {
        this.start = start;
        this.end = end;
        this.isReturned = isReturned;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isIsReturned() {
        return isReturned;
    }
    

}
