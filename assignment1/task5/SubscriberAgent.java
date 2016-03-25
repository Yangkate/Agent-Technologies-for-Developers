package fi.jyu.ties454.jiayang.assignment1.task5;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SubscriberAgent extends Agent {
	String[] topicArr= { "DTIME", "P", "EXPTIME", "NTIME", "NP", "NEXPTIME", "DSPACE", "L", "PSPACE","EXPSPACE", "NSPACE", "NL", "NPSPACE","NEXPSPACE" };
    String latestContent;
	@Override
	public void setup(){
		
		ACLMessage letter = new ACLMessage(ACLMessage.REQUEST);
		letter.setLanguage("English");
		int random = (int )(Math. random()*topicArr.length);
		letter.setContent(topicArr[random]);
		AID reciever = new AID("Broker", AID.ISLOCALNAME);
		letter.addReceiver(reciever);
		letter.setPerformative(ACLMessage.REQUEST);
		send(letter);
			
			addBehaviour(new CyclicBehaviour(this) 
	        {
	             public void action() 
	             {
	                ACLMessage msg= receive();
	                if (msg!=null){
	                	latestContent=msg.getContent();
	                    
	                }
	                //block();
		}

	        });
			
			Behaviour loop = new TickerBehaviour(this, 1000) {
				protected void onTick() {
					System.out.println( " - " +
		                       myAgent.getLocalName() + " <- " +
		                       latestContent);
					
				}
			};
			addBehaviour(loop);
			
		}

}
