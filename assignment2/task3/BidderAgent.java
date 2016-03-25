package fi.jyu.ties454.jiayang.assignment2.task3;

import java.util.HashMap;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BidderAgent extends Agent {

	private AID[] auctionAgents;
	private String bidContent;
	final int max;

	public BidderAgent(int max) {
		super();
		this.max = max;
		System.out.println("max:" + max);

	}

	@Override
	public void setup() {

		addBehaviour(new Behaviour() {
			private boolean received = false;

			@Override
			public boolean done() {
				return this.received;
			}

			@Override
			public void action() {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("EnglishAuction");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent,
							template);
					auctionAgents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						auctionAgents[i] = result[i].getName();

					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}

				// System.out.println("auctionAgents[0]"+auctionAgents[0]);
				boolean registerTime = false;
				if (auctionAgents.length > 0 && registerTime == false) {
					//

					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					for (int i = 0; i < auctionAgents.length; ++i) {
						msg.addReceiver(auctionAgents[i]);
					}
					msg.setLanguage("English");
					msg.setContent("RegisterEnglishAuction");
					msg.setReplyWith("forBid");// encrypt
					// System.out.println("Bidderregister:"+msg);
					send(msg);
					registerTime = true;
					received = true;

				}
			}

		});

		Random ran = new Random();

		// --
		// bid rounds
		addBehaviour(new Behaviour() {

			private boolean received = false;

			@Override
			public boolean done() {
				return this.received;
			}

			@Override
			public void action() {
				ACLMessage rec = receive();
				if (rec == null) {
					block();
					return;
				}


				int myBid;
				if (rec.getContent().contains("start")) {
					myBid = 1 + ran.nextInt(100);
				} else {
					String[] parts = rec.getContent().split("#");
					int bidResult = Integer.parseInt(parts[0]);
					String currentHighestBidder = parts[1];
					//System.out.println(currentHighestBidder + myAgent.getLocalName());
					
					if (currentHighestBidder.equals(myAgent.getLocalName())){
						return;
					}
					if (bidResult > max){
						return;
					}
					myBid = bidResult + 1;
					if (myBid > max) {
						myBid = max;
					}
				}

				ACLMessage reply = rec.createReply();
				// reply.setInReplyTo(rec.getReplyWith());

				reply.setPerformative(ACLMessage.INFORM);

				reply.setContent(Integer.toString(myBid));

				System.out.println(myAgent.getLocalName() + "-_:" + myBid);
				send(reply);

			}

		});
		// --

	}
}
