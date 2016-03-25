package fi.jyu.ties454.jiayang.assignment1.task5;

import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BrokerAgent extends Agent {
	String[] topicArr= { "DTIME", "P", "EXPTIME", "NTIME", "NP", "NEXPTIME", "DSPACE", "L", "PSPACE","EXPSPACE", "NSPACE", "NL", "NPSPACE","NEXPSPACE" };
	ListMultimap<String, AID> SubscriberTopic = ArrayListMultimap.create();
    
    @Override
	public void setup(){
	
	
	addBehaviour(new CyclicBehaviour(this) 
    {
         public void action() 
         {
            ACLMessage msg= receive();
            
           if (msg!=null&&msg.getPerformative()==ACLMessage.REQUEST){
            	
        	   SubscriberTopic.put(msg.getContent(),msg.getSender());
                
            }
            
            if (msg!=null&&msg.getPerformative()==ACLMessage.INFORM){
            	
            	String[] msgArr=msg.getContent().split(",", 2);
            	ACLMessage letter = new ACLMessage(ACLMessage.INFORM);
        		letter.setLanguage("English");
        		letter.setContent(msgArr[1]);
        		for(int i=0;i<SubscriberTopic.get(msgArr[0]).size();i++){
        		letter.addReceiver(SubscriberTopic.get(msgArr[0]).get(i));
        		}
        		letter.setPerformative(ACLMessage.INFORM);
        		send(letter);
                
            }

           
}

    });

}

}