package mas.cv4;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.*;
import mas.cv4.onto.*;

import java.util.*;

public class TradingHistory {

    private ArrayList<Pair<Offer, Boolean>> sold;
    private ArrayList<Pair<Offer, Boolean>> bought;

    public TradingHistory() {
        sold = new ArrayList<>();
        bought = new ArrayList<>();
    }

    public void logSellTo(Offer offer, Boolean accepted) {
        sold.add(new Pair<>(offer, accepted));
    }

    
    public void logBuyFrom(Offer offer, Boolean accepted) {
        bought.add(new Pair<>(offer, accepted));
    }

    /**
     * Buy some book(s) from the other agent who requested them fo rsome price which would be a nice profit for me.
     * - I can be sure that I own all of the books in the request at the moment
     */
    public Offer makeOffer(SellMeBooks request, List<Goal> goals) {
        // offer as little as possible for the books
        Offer offer = new Offer();
        offer.setBooks(request.getBooks());
        offer.setMoney(calculateMinimumPriceFor(request.getBooks(), goals) * 0.9); // pay a little less than would be minimum...
        return offer;
    }

    /**
     * I requested some books and the other agent wants to sell me the books for some price.
     * Should I accept the offer??
     */
    public boolean shouldAccept(Offer offer, List<Goal> goals, double profit) {
        return profit > calculateMinimumPriceFor(offer.getBooks(), goals);
    }

    private double calculateMinimumPriceFor(List<BookInfo> books, List<Goal> goals) {
        double price = 0;

        for (BookInfo book : books) {
            Goal myGoal = null;
            for (Goal goal : goals) {
                if (goal.getBook().getBookID() == book.getBookID()) {
                    myGoal = goal;
                    break;
                }
            }

            // how much would I pay for the book?
            price += myGoal == null ? 50. : myGoal.getValue(); // @todo 50 is a weird constant
        }

        return price;
    }
}