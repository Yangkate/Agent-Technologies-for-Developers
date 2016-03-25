package fi.jyu.ties454.jiayang.assignment1.task2;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class HelloAgent extends Agent{
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
                    reply.setContent(" nice to meet you Hi!" );
                    send(reply);
                 }
                block();
             }

        
        });
	}

}
