package fi.jyu.ties454.jiayang.assignment1.task3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;


public class HelloAgent extends Agent{
	@Override
	public void setup(){		
		 Behaviour loop = new TickerBehaviour( this, 5000 )
	      {
	         protected void onTick() {
	        	 ACLMessage letter= new ACLMessage(ACLMessage.INFORM);
	     		letter.setLanguage("English");
	     		letter.setContent("Hello");
	     		AID reciever = new AID("Hi",AID.ISLOCALNAME);
	     		letter.addReceiver(reciever);
	     		send(letter);
	     		addBehaviour(new CyclicBehaviour() 
				{
					public void action() 
				    {
				       ACLMessage msg= receive();
				       if (msg!=null)
				           System.out.println( " - " +
				              myAgent.getLocalName() + " <- " +
				              msg.getContent() );
				       
				    
				       block();
				}
				});
	     		}
	      };
	      
	      
	   addBehaviour(loop);
	
	}
}






