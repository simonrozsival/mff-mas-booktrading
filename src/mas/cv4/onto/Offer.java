package mas.cv4.onto;

import jade.content.Concept;
import jade.content.onto.annotations.AggregateSlot;
import jade.content.onto.annotations.Slot;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 16.4.14.
 *
 * * An offer from the seller, must contain both money and a list of books
 */
public class Offer implements Concept {

    ArrayList<BookInfo> books;
    double money;

    @Slot(mandatory = true)
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @AggregateSlot(cardMin = 0)
    public ArrayList<BookInfo> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BookInfo> books) {
        this.books = books;
    }

}
