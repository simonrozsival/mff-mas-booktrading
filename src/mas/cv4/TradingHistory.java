package mas.cv4;

import mas.cv4.onto.*;

public class TradingHistory {

    private DFAgentDescription agent;
    private ArrayList<Pair<Offer, boolean>> sold;
    private ArrayList<Pair<Offer, boolean>> bought;

    public TradingHistory(DFAgentDescription agent) {
        this.agent = agent;
        sold = new ArrayList<Pair<Offer, boolean>>();
        bought = new ArrayList<Pair<Offer, boolean>>();
    }

    public void logSellTo(Offer offer, boolean accepted) {
        sold.push(new Pair<Offer, boolean>(offer, accepted));
    }

    
    public void logBuyFrom(Offer offer, boolean accepted) {
        bought.push(new Pair<Offer, boolean>(offer, accepted));
    }

    /**
     * Buy some book(s) from the other agent who requested them fo rsome price which would be a nice profit for me.
     * - I can be sure that I own all of the books in the request at the moment
     */
    public Offer makeOffer(SellMeBooks request) {
        // @todo
        return null;
    }

    /**
     * I requested some books and the other agent wants to sell me the books for some price.
     * Should I accept the offer??
     */
    public boolean shouldAccept(Offer offer) {
        // @todo
        return false;
    }

}