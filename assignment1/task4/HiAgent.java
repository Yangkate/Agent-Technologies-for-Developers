package fi.jyu.ties454.jiayang.assignment1.task4;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class HiAgent extends Agent {
	@Override
	public void setup() {
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					System.out.println(" - " + myAgent.getLocalName() + " <- "
							+ msg.getContent());

					addBehaviour(new WakerBehaviour(HiAgent.this, 10000) {
						protected void handleElapsedTimeout() {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.INFORM);
							reply.setInReplyTo(msg.getReplyWith());
							reply.setContent(" Hi!"+msg.getContent().replaceAll("[^0-9]", ""));//Extract digits from stringContent
							send(reply);
						}
					});

				}
				block();
			}

		});
	}
}
