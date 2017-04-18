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

    private DFAgentDescription agent;
    private ArrayList<Pair<Offer, Boolean>> sold;
    private ArrayList<Pair<Offer, Boolean>> bought;

    public TradingHistory(DFAgentDescription agent) {
        this.agent = agent;
        sold = new ArrayList<Pair<Offer, Boolean>>();
        bought = new ArrayList<Pair<Offer, Boolean>>();
    }

    public void logSellTo(Offer offer, Boolean accepted) {
        sold.add(new Pair<Offer, Boolean>(offer, accepted));
    }

    
    public void logBuyFrom(Offer offer, Boolean accepted) {
        bought.add(new Pair<Offer, Boolean>(offer, accepted));
    }

    /**
     * Buy some book(s) from the other agent who requested them fo rsome price which would be a nice profit for me.
     * - I can be sure that I own all of the books in the request at the moment
     */
    public Offer makeOffer(SellMeBooks request, List<Goal> goals) {
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