package mas.cv4.onto;

import jade.content.Predicate;
import jade.content.onto.annotations.AggregateSlot;
import jade.content.onto.annotations.Slot;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 16.4.14.
 *
 * List of offer from which the buyer can choose
 */
public class ChooseFrom implements Predicate {

    ArrayList<Offer> offers;

    @Slot(mandatory = true)
    public ArrayList<BookInfo> getWillSell() {
        return willSell;
    }

    public void setWillSell(ArrayList<BookInfo> willSell) {
        this.willSell = willSell;
    }

    ArrayList<BookInfo> willSell;

    @AggregateSlot(cardMin = 1)
    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

}
