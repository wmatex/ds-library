/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

/**
 *
 * @author tomanlu2
 */
public class Book {
    private final String name;
    private final String author;
    private final int yearOfPublication;
    private final int borrowTime;

    public Book(String name, String author, int yearOfPublication, int borrowTime) {
        this.name = name;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.borrowTime = borrowTime;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public int getBorrowTime() {
        return borrowTime;
    }

    @Override
    public String toString() {
        return "Nazev: " + name + ", Autor: " + author;
    }
    

}
