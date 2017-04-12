package mas.cv4.onto;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 16.4.14.
 *
 * A request to sell books
 */
public class SellMeBooks implements AgentAction {

    public ArrayList<BookInfo> getBooks() {
        return books;
    }

    @Slot(mandatory = true)
    public void setBooks(ArrayList<BookInfo> books) {
        this.books = books;
    }

    ArrayList<BookInfo> books;

}
