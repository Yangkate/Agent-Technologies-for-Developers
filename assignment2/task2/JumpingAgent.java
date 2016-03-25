package fi.jyu.ties454.jiayang.assignment2.task2;

import java.util.ArrayList;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.*;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;


public class JumpingAgent extends Agent{
	@Override
	public void setup(){	
		
		Location des1;
		Location des2;
		ArrayList<Location> locations =new ArrayList<Location>();
		locations = this.getLocations();
		des1=locations.get(0);
		des2=locations.get(1);
	
		addBehaviour(new CyclicBehaviour() 
		{
			private int step = 0;
			public void action() 
		    {
				

				AgentContainer container = getContainerController();
                
                switch (step) {
                case 0:
                // perform operation X
                	doWait(1000);
                    doMove(des1);
                    System.out.println(" - " + myAgent.getLocalName() + " <- "
							+ "now I jump from"+des2+" to "+des1);
                step++;
                break;
                case 1:
                // perform operation Y
                	doWait(1000);
                    doMove(des2);
                    System.out.println(" - " + myAgent.getLocalName() + " <- "
							+ "now I jump from"+des1+" to "+des2);
                    step=0;
                break;
               
                }
                
	}
			
}
		);
		
	}
	ArrayList<Location> getLocations() {
	    // adapted from :
	    // http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility/ControllerAgent.java
	    getContentManager().registerLanguage(new SLCodec());
	    getContentManager().registerOntology(MobilityOntology.getInstance());

	    // Get available locations with AMS
	    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	    request.setLanguage(new SLCodec().getName());
	    request.setOntology(MobilityOntology.getInstance().getName());
	    Action action = new Action(getAMS(), new QueryPlatformLocationsAction());
	    try {
	        getContentManager().fillContent(request, action);
	    } catch (CodecException | OntologyException e) {
	        throw new Error(e);
	    }
	    request.addReceiver(action.getActor());
	    send(request);

	    // Receive response from AMS
	    MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()),
	            MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	    ACLMessage resp = blockingReceive(mt);
	    ContentElement ce;
	    try {
	        ce = getContentManager().extractContent(resp);
	    } catch (CodecException | OntologyException e) {
	        throw new Error(e);
	    }
	    Result result = (Result) ce;
	    jade.util.leap.Iterator it = result.getItems().iterator();
	    ArrayList<Location> locations1 = new ArrayList<Location>();
	    while (it.hasNext()) {
	        Location loc = (Location) it.next();
	        locations1.add(loc);
	    }
	    return locations1;
	}
}





