package fi.jyu.ties454.jiayang.assignment2.task1;

import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class weatherAgent extends Agent {

	String city;
	String weather;

	@Override
	public void setup() {

		// Register the weatherInfor service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("weatherInfor");
		sd.setName("weatherAgent");
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
				ACLMessage rec = blockingReceive();
				if (rec != null) {
					city = rec.getContent();
					openWeather w = new openWeather();
					try {
						weather = w.getWeather(city);
					} catch (IOException e) {

						e.printStackTrace();
					}

					ACLMessage reply = rec.createReply();

					reply.setInReplyTo(rec.getReplyWith());
					reply.setContent(weather);

					send(reply);
					received = true;

				} else {

					block();
				}

			}

		});

	}

}
