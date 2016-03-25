package fi.jyu.ties454.jiayang.assignment1.task2;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HiAgent extends Agent {
	@Override
public void setup(){
		ACLMessage letter= new ACLMessage(ACLMessage.INFORM);
		letter.setLanguage("English");
		letter.setContent("Hi,I'm HI");
		AID reciever = new AID("Hello",AID.ISLOCALNAME);
		letter.addReceiver(reciever);
		send(letter);
		
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg= receive();
                if (msg!=null){
                    System.out.println( " - " +
                       myAgent.getLocalName() + " <- " +
                       msg.getContent() );
                }
                block();
	}

        });
	}
		
}