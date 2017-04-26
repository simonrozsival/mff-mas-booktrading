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

import java.awt.print.Book;
import java.util.*;

public class TradingLogic {

    // myBooks = ai.getBooks();
    // myGoal = ai.getGoals();
    // myMoney = ai.getMoney();

    /**
     * Up-to-date information about my agent's state
     */
    private AgentInfo ai;

    /**
     * The history of trading with each of the agents
     */
    private HashMap<AID, TradingHistory> traders;

    public TradingLogic(AgentInfo info, DFAgentDescription[] others) {
        updateInfo(info);        
        traders = new  HashMap<AID, TradingHistory>();
        for (DFAgentDescription trader : others) {
            traders.put(trader.getName(), new TradingHistory());
        }
    }

    /**
     * Set the up-to-data information about my agent's state in the environment.
     * - must be updated after every enclosed deal!!
     */
    public void updateInfo(AgentInfo info) {
        ai = info;
    }
    
    public void logSellTo(AID agent, Offer offer, boolean accepted) {
        traders.get(agent).logSellTo(offer, accepted);
    }

    public void logBuyFrom(AID agent, Offer offer, boolean accepted) {
        traders.get(agent).logBuyFrom(offer, accepted);
    }

    private int nextBookToSell = 0;

    /**
     * Choose book(s) which I could request from the other agents.
     */
    public SellMeBooks makeRequest() {
        //System.out.println(ai.getMoney() + "making request");
        ArrayList<BookInfo> bis = new ArrayList<BookInfo>();
        List<Goal> myGoal = ai.getGoals();

        // @todo choose a book from goals to buy
        int i = nextBookToSell++ % myGoal.size();
        BookInfo bi = new BookInfo();
        bi.setBookName(myGoal.get(i).getBook().getBookName());
        bis.add(bi);

        SellMeBooks smb = new SellMeBooks();
        smb.setBooks(bis);
        return smb;
    }

    /**
     * An agent requested some books - 
     */
    public Offer makeOffer(AID agent, SellMeBooks request) {
        if (!shouldEvenConsider(request)) {
            return null;
        }
        //System.out.println(ai.getMoney() + "making offer: ");

        return traders.get(agent).makeOffer(request, ai.getGoals());
    }

    /**
     * Look whether I even have the books which are requested and whether I can fulfill the offer.
     */
    private boolean shouldEvenConsider(SellMeBooks request) {
        //System.out.println(ai.getMoney() + "got sellmebook request (considering): " + request.toString());
        for(BookInfo b : ai.getBooks()) {
            //System.out.println(ai.getMoney() + "    my:  " + b.getBookName());
        }
        for(BookInfo b : request.getBooks()) {
            //System.out.println(ai.getMoney() + "    his: " + b.getBookName());
        }

        ArrayList<BookInfo> myBooks = ai.getBooks();
        ArrayList<BookInfo> books = request.getBooks();

        for (int i = 0; i < books.size(); i++) {
            boolean found = false;
            for (int j = 0; j < myBooks.size(); j++) {
                if (myBooks.get(j).getBookName().equals(books.get(i).getBookName())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                //System.out.println(ai.getMoney() + "  refusing");
                return false;
            }
        }

        //System.out.println(ai.getMoney() + "  considering");
        return true;
    }

    public Offer chooseBestOne(List<Offer> offers) {
        double bestScore = 0;
        Offer bestOffer = null;

        for (Offer o: offers) {
            double offerScore = calculateProfit(o);

            if(bestOffer == null || offerScore > bestScore) {
                bestScore = offerScore;
                bestOffer = o;
            }
        }

        return bestOffer;
    }

    /**
     * Choose the best offer from the list of offers.
     * @param agent The agent
     * @param offers The offers from the agent
     * @return The best offer
     */
    public Offer chooseBest(AID agent, List<Offer> offers) {
        //find out which offers we can fulfill (we have all requested books and enough money)
        List<Offer> canFulfill = canFulfill(offers);

        boolean chosen = false;
        double bestScore = 0;
        Offer bestOffer = null;

        System.out.println(ai.getMoney() + "choosing best:");
        //System.out.println(ai.getMoney() + "  out of:");
        /*for(Offer o : offers) {
            //System.out.println(ai.getMoney() + "    -");
            for(BookInfo b : o.getBooks()) {
                //System.out.println(ai.getMoney() + "      his: " + b.getBookName());
                //System.out.println(ai.getMoney() + "      for money: " + o.getMoney());
            }
        }*/


        for (Offer o: canFulfill) {
            if (o == null) continue;
            if (o.getBooks() == null) continue;
            boolean shouldTake = shouldAccept(agent, o);
            double offerScore = calculateProfit(o);
            //System.out.println(ai.getMoney() + "  considering with profit " + offerScore + " should Accept = " + shouldTake);
            //for(BookInfo b : o.getBooks())
                //System.out.println(ai.getMoney() + "    his: " + b.getBookName());
            //System.out.println(ai.getMoney() + "    for money: " + o.getMoney());

            if(shouldTake && (!chosen || offerScore > bestScore)) {
                chosen = true;
                bestScore = offerScore;
                bestOffer = o;
            }
        }

        if(bestOffer == null) {
            //System.out.println(ai.getMoney() + "  no offer chosen");
        }
        else{
            //System.out.println(ai.getMoney() + "chosen offer: " + bestOffer.toString() + " with score " + bestScore);
        }
        return bestOffer;
    }

    /**
     * From the list of offers select only those I can fullfill.
     * @param offers The received offers
     * @return The offers I can fulfill
     */
    private List<Offer> canFulfill(List<Offer> offers) {
        List<Offer> canFulfill = new ArrayList<Offer>();
        List<BookInfo> myBooks = ai.getBooks();

        //System.out.println(ai.getMoney() + "==== canFullfill");
        for (Offer o: offers) {
            if (o.getMoney() > ai.getMoney()) {
                //System.out.println(ai.getMoney() + "  not enough money");
                continue;
            }

            boolean foundAll = true;
            if (o.getBooks() != null)
                for (BookInfo bi : o.getBooks()) {
                    String bn = bi.getBookName();
                    boolean found = false;
                    for (int j = 0; j < myBooks.size(); j++) {
                        //System.out.println(ai.getMoney() + "  trying: " + myBooks.get(j).getBookName() + " == " + bn);
                        if (myBooks.get(j).getBookName().equals(bn)) {
                            //System.out.println(ai.getMoney() + "    found");
                            found = true;
                            bi.setBookID(myBooks.get(j).getBookID());
                            break;
                        }
                    }
                    if (!found) {
                        //System.out.println(ai.getMoney() + "    not found: " + bn);
                        foundAll = false;
                        break;
                    }
                }

            if (foundAll) {
                //System.out.println(ai.getMoney() + "found all");
                canFulfill.add(o);
            }
            else {
                //System.out.println(ai.getMoney() + "not found all");
            }
        }
        //System.out.println(ai.getMoney() + "---- canFullfill");

        return canFulfill;
    }

    //TODO: IMPORTANT - remove the true!

    /**
     * I requested some books and the other agent wants to sell me the books for some price.
     * Should I accept the offer??
     */
    private boolean shouldAccept(AID agent, Offer offer) {
        return traders.get(agent).shouldAccept(offer, ai.getGoals(), calculateProfit(offer));
    }

    /**
     * We can choose only one offer
     * -> choose the one with highest rating if shouldAccept returns true for more than one
     */
    private double calculateProfit(Offer o) {
        double profit = 0;
        for (BookInfo book : o.getBooks()) {
            for (Goal g : ai.getGoals()) {
                if (g.getBook().equals(book)) {
                    profit -= g.getValue();
                    break;
                }
            }

            profit += o.getMoney();
        }

        return profit;
    }

}