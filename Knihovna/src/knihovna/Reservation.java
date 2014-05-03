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
public class Reservation {
    private User user;
    private Book book;
    private Date interestTo;
    private Date ends;
    private int standing;

    public Reservation(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    @Override
    public String toString() {
        return book.toString();
    }

    public Reservation(User user, Book book, Date interestTo, int standing) {
        this.user = user;
        this.book = book;
        this.interestTo = interestTo;
        this.standing = standing;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getInterestTo() {
        return interestTo;
    }

    public Date getEnds() {
        return ends;
    }

    public int getStanding() {
        return standing;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setInterestTo(Date interestTo) {
        this.interestTo = interestTo;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public void setStanding(int standing) {
        this.standing = standing;
    }
    
    

}
