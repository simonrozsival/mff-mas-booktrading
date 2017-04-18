package mas.cv4;

import mas.cv4.onto.*;

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
    private HashMap<DFAgentDescription, TradingHistory> traders;

    public TradingLogic(AgentInfo info, DFAgentDescription[] others) {
        updateInfo(info);        
        traders = new  HashMap<DFAgentDescription, TradingHistory>();
        for (DFAgentDescription trader : others) {
            traders.put(trader, new TradingHistory(trader));
        }
    }

    /**
     * Set the up-to-data information about my agent's state in the environment.
     * - must be updated after every enclosed deal!!
     */
    public void updateInfo(AgentInfo info) {
        ai = info;
    }
    
    public void logSellTo(DFAgentDescription agent, Offer offer, boolean accepted) {
        traders[agent].logSellTo(offer, accepted);
    }

    public void logBuyFrom(DFAgentDescription agent, Offer offer, boolean accepted) {
        traders[agent].logBuyFrom(offer, accepted);
    }

    /**
     * Choose book(s) which I could request from the other agents.
     */
    public SellMeBooks makeRequest(ArrayList<BookInfo> myBooks) {
        // @todo
        return null;
    }

    /**
     * An agent requested some books - 
     */
    public Offer makeOffer(DFAgentDescription agent, SellMeBooks request) {
        if (!shouldEvenConsider(request)) {
            return null;
        }

        ArrayList<BookInfo> myBooks = ai.getBooks();
        return traders[agent].makeOffer(request, myBooks);
    }

    /**
     * Look whether I even have the books which are requested and whether I can fulfill the offer.
     */
    private boolean shouldEvenConsider(SellMeBooks request) {
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
                return false;
            }
        }
        
        return false;
    }

}