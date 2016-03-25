package fi.jyu.ties454.jiayang.assignment1.task4;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.hash.Hashing;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class HelloAgent extends Agent {

	int times = 0;

	@Override
	public void setup() {
		Behaviour loop = new TickerBehaviour(this, 5000) {
			protected void onTick() {
				ACLMessage letter = new ACLMessage(ACLMessage.INFORM);
				letter.setLanguage("English");
				letter.setContent("Hello for the " + times + "-th time");
				AID reciever = new AID("Hi", AID.ISLOCALNAME);
				letter.addReceiver(reciever);

				String uuid = UUID.randomUUID().toString();// generate a random
															// alpha-numeric
															// string
				String tp = DigestUtils.md5Hex(uuid);// for security, actually
														// it may has no need
														// for MD5
				letter.setReplyWith(tp);
				send(letter);

				MessageTemplate mt = MessageTemplate.MatchInReplyTo(tp);
				addBehaviour(new Behaviour() {
					boolean received = false;

					public void action() {

						ACLMessage msg = receive(mt);
						if (msg != null) {
							System.out.println(" - " + myAgent.getLocalName()
									+ " <- " + msg.getContent());
							received = true;
							// myAgent.doDelete();
						} else {

							block();
						}
					}

					public boolean done() {
						return this.received;
					}
				});

				times++;
			}

		};

		addBehaviour(loop);

	}
}
