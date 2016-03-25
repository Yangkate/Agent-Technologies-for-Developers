package fi.jyu.ties454.jiayang.assignment2.task3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

public class AuctioneerAgent extends Agent {

	private List<AID> bidderArr = new ArrayList<AID>();
	private AID bidderWinner;
	private int curentHighBid = 1;

	@Override
	public void setup() {

		// Register the autionInfor service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("EnglishAuction");
		sd.setName("AuctioneerAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new Behaviour() {
			private boolean auctionEnded = false;

			@Override
			public boolean done() {
				return auctionEnded;
			}

			@Override
			public void action() {
				// TODO Auto-generated method stub

				ACLMessage msg = receive();

				if (msg == null) {
					block();
					return;
				}

				if (msg.getContent().contains("Register")) {

					bidderArr.add(msg.getSender());
					if (bidderArr.size() == 20) {
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						for (int i = 0; i < bidderArr.size(); ++i) {
							msg2.addReceiver(bidderArr.get(i));
						}
						msg2.setLanguage("English");
						msg2.setContent("Auctioner:start,price from 0!");
						msg2.setReplyWith("forBidFromOffical");// encrypt

						send(msg2);
					
					}
				} else {

					final int newBid = Integer.parseInt(msg.getContent()
							.replaceAll("[^0-9]", ""));

					if (newBid > curentHighBid) {
						curentHighBid = newBid;

						bidderWinner = msg.getSender();

						ACLMessage msg3 = new ACLMessage(ACLMessage.INFORM);
						for (int i = 0; i < bidderArr.size(); ++i) {
							msg3.addReceiver(bidderArr.get(i));
						}
						msg3.setPerformative(ACLMessage.INFORM);
						msg3.setLanguage("English");
						String contentRe = Integer.toString(newBid) + "#"
								+ msg.getSender().getLocalName();
						msg3.setContent(contentRe);
						System.out.println(myAgent.getLocalName()
								+ "------_||:" + newBid);
						send(msg3);

						// --
						addBehaviour(new WakerBehaviour(AuctioneerAgent.this,
								10000) {
							protected void onWake() {
								if (curentHighBid == newBid) {
									auctionEnded = true;
									
									String contentR = "Auction is done and the final Price is:"
											+ Integer.toString(curentHighBid)
											+ "from Agent:"
											+ bidderWinner
													.getLocalName()
											+ "Congratulations!";
									
									System.out.println(myAgent.getLocalName()
											+ "$$_$$:" + contentR);
								}
							}

						});

						

					}
				}


			}

			public void sendMsg(String Content) {
				ACLMessage msg4 = new ACLMessage(ACLMessage.INFORM);
				for (int i = 0; i < bidderArr.size(); ++i) {
					msg4.addReceiver(bidderArr.get(i));
				}
				msg4.setPerformative(ACLMessage.INFORM);
				msg4.setLanguage("English");

				msg4.setContent(Content);
				msg4.setInReplyTo("forBidFromOffical");

				send(msg4);

			}

		});

	}

}