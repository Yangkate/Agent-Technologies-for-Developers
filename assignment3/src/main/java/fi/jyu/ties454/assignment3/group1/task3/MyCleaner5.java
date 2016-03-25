package fi.jyu.ties454.assignment3.group1.task3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JackieChanRotator;
import fi.jyu.ties454.cleaningAgents.infra.Location;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner5 extends GameAgent {

	private static final long serialVersionUID = 1L;
	HashMap<String, String> status = new HashMap<String, String>();
	private ForwardMover mover;
	private Rotator rotator;
	private Cleaner cleaner;
    String agent;
	@Override
	protected void setup() {
		// it is safe to obtain parts in setup(), but using them must be done in
		// behaviors!
		// getting the device is don using the getDevice call.
		// This call returns an Optional<E extends Device> which is present if
		// the device was successfully obtained
		status.put("1", "free");
		status.put("2", "free");
		addBehaviour(new Behaviour() {
			private boolean received = false;
			HashMap<String, AID> hmap = new HashMap<String, AID>();
			
			List<Location> dirtList = new ArrayList<Location>();
			@Override
			public boolean done() {
				return this.received;
			}

			public void action() {
				
				ACLMessage msg = receive();
				if (msg != null) {

					if (msg.getPerformative() == ACLMessage.INFORM) {
						
                        try {
							dirtList.add((Location)msg.getContentObject());
						} catch (UnreadableException e) {
							throw new Error(e);
						}
                        
						ACLMessage toSubscriber = new ACLMessage(ACLMessage.INFORM);
						if(status.get("1").equals("free")&&status.get("2").equals("busy")){
							status.put("1", "busy");
							System.out.println("case1 1 to busy "+status.get("1"));
						    toSubscriber.addReceiver(hmap.get("1")); 
						    toSubscriber.setLanguage("English");
                            try {
								toSubscriber.setContentObject(dirtList.get(0));
								dirtList.remove(0);
							} catch (IOException e) {
								throw new Error(e);
							}
                            send(toSubscriber);}
						
						if(status.get("1").equals("busy")&&status.get("2").equals("free")){
							status.put("2", "busy");
							System.out.println("case2 2 to busy "+status.get("2"));
							toSubscriber.addReceiver(hmap.get("2")); 
							toSubscriber.setLanguage("English");
							try {
								toSubscriber.setContentObject(dirtList.get(0));
								dirtList.remove(0);
							} catch (IOException e) {
								throw new Error(e);
							}
							
	                        send(toSubscriber);}
						
						if(status.get("1").equals("free")&&status.get("2").equals("free")){
							status.put("1", "busy");
							System.out.println("case3 1 to busy "+status.get("1"));
							toSubscriber.addReceiver(hmap.get("1")); 
							toSubscriber.setLanguage("English");
							try {
								toSubscriber.setContentObject(dirtList.get(0));
								dirtList.remove(0);
							} catch (IOException e) {
								throw new Error(e);
							}
	                        send(toSubscriber);}
						if(status.get("1").equals("busy")&&status.get("2").equals("busy")){
							System.out.println("case4 1 and 2to busy "+status.get("1")+status.get("2"));
							block(2000);
					
						    }
						
					} 
					else if (msg.getPerformative() == ACLMessage.REQUEST)//return cleaner cleans
					{
						System.out.println("reply from"+msg.getSender());
					 if(msg.getSender()==hmap.get("1")){
					 status.put("1", "free");
					 }
					 else{
						 status.put("2", "free"); 
					 }
					}
					else {
                         agent=msg.getContent();
						 hmap.put(agent, msg.getSender());
						
						
					}
				}
			}
		});
	}

	

}
