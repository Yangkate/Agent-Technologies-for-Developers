package fi.jyu.ties454.jiayang.assignment2.task1;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class clothAgent extends Agent {

	double weather;
	String clothAdvise;

	@Override
	public void setup() {

		// Register the weatherInfor service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("clothInfor");
		sd.setName("clothAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new Behaviour() {

			private boolean received = false;

			@Override
			public boolean done() {
				return this.received;
			}

			@Override
			public void action() {

				ACLMessage msg = receive();
				
				if (msg != null) {
					
					weather = Double.parseDouble(msg.getContent());
					
					if (0 < weather && weather < 200) {
						clothAdvise = "weatehr is cold, waer your mink coat!";
					} else if (200 <= weather && weather < 600) {
						clothAdvise = "waether is cool like morning of Spring,just take your normal T-shirk on!";
					} else if (600 <= weather && weather < 800) {
						clothAdvise = "weather is very hot like summer near beach,dont waer too much!";
					} else {
						clothAdvise = "not good weather,dont go there!";
					}

					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(clothAdvise);
					reply.setInReplyTo(msg.getReplyWith());
					
					send(reply);
					received = true;
					
				} else {
					
					block();
				}
			}

		});
		

	}

}
