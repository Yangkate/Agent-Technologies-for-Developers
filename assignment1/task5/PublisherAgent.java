package fi.jyu.ties454.jiayang.assignment1.task5;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class PublisherAgent extends Agent {
	
	String[] topicArr= { "DTIME", "P", "EXPTIME", "NTIME", "NP", "NEXPTIME", "DSPACE", "L", "PSPACE","EXPSPACE", "NSPACE", "NL", "NPSPACE","NEXPSPACE" };

	@Override
	public void setup() {
	
	Behaviour loop = new TickerBehaviour(this, 1000) {
		protected void onTick() {
			ACLMessage letter = new ACLMessage(ACLMessage.INFORM);
			letter.setLanguage("English");
			int random = (int )(Math. random()*topicArr.length);
			letter.setContent(topicArr[random]+","+ 2*random);
			AID reciever = new AID("Broker", AID.ISLOCALNAME);
			letter.addReceiver(reciever);
			letter.setPerformative(ACLMessage.INFORM);
			send(letter);
		
		}
	};
		addBehaviour(loop);
		
}
}
