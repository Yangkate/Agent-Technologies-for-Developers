package fi.jyu.ties454.jiayang.assignment1.task3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HiAgent extends Agent {
	@Override
public void setup(){
		addBehaviour(new CyclicBehaviour(this) 
        {
			 public void action() 
             {
                ACLMessage msg = receive();
                if (msg!=null) {
                    System.out.println( " - " +
                       myAgent.getLocalName() + " <- " +
                       msg.getContent() );
                       
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative( ACLMessage.INFORM );
                    reply.setContent(" Hi!" );
                    send(reply);
                 }
                block();
             }

        
        });
	}
	}
		
