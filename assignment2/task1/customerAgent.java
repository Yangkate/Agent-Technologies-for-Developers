package fi.jyu.ties454.jiayang.assignment2.task1;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class customerAgent extends Agent {

	private String city = "London";;

	@Override
	protected void setup() {

		// for search service from DF for weatherInfor and clothInfor
		addBehaviour(new Behaviour() {

			private boolean found = false;

			@Override
			public boolean done() {
				return this.found;
			}

			@Override
			public void action() {

				block(1000);
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("weatherInfor");
				template.addServices(sd);
				AID weatherAgent = null;
				try {
					DFAgentDescription[] result = DFService.search(myAgent,
							template);
					if (result.length > 0) {
						weatherAgent = result[0].getName();

					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}

				sd.setType("clothInfor");
				template.addServices(sd);
				AID clothAgent = null;
				try {
					DFAgentDescription[] result2 = DFService.search(myAgent,
							template);
					if (result2.length > 0) {
						clothAgent = result2[0].getName();

					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				if (weatherAgent != null && clothAgent != null) {
					addBehaviour(new customerAgent.QueryWeather(city,
							weatherAgent, clothAgent));
					found = true;

				}
			}
		});

		//System.out.println("--customerAgent done---");

	}

	class QueryWeather extends OneShotBehaviour {
		// --

		String city;
		AID weatherAgent;
		AID clothAgent;

		public QueryWeather(String city, AID weatherAgent, AID clothAgent) {
			super();
			this.city = city;
			this.weatherAgent = weatherAgent;
			this.clothAgent = clothAgent;
		}
		
		

		@Override
		public void action() {
			quryWeather();
		}

		public void quryWeather() {

			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(weatherAgent);

			msg.setLanguage("English");
			msg.setContent(city);
			msg.setReplyWith("toweatherAgent");// encrypt

			send(msg);

			MessageTemplate mt = MessageTemplate
					.MatchInReplyTo("toweatherAgent");
			addBehaviour(new Behaviour() {
				private boolean received = false;

				@Override
				public boolean done() {
					return received;
				}

				@Override
				public void action() {

					ACLMessage rec = receive(mt);
					if (rec != null) {
						received = true;
						String cityWeather = rec.getContent();
						addBehaviour(new ClothesQueryBehaviour(cityWeather,
								clothAgent));
					} else {
						block();
					}

				}

			});
		}
	}

	public class ClothesQueryBehaviour extends OneShotBehaviour {

		private String cityWeather;
		AID clothAgent;

		public ClothesQueryBehaviour(String cityWeather, AID clothAgent) {

			this.cityWeather = cityWeather;
			this.clothAgent = clothAgent;
		}

		@Override
		public void action() {
			quryCloth();
		}

		public void quryCloth() {

			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);

			msg.addReceiver(clothAgent);

			msg.setLanguage("English");
			msg.setContent(cityWeather);
			msg.setReplyWith("toclothAgent");// encrypt

			send(msg);

			addBehaviour(new ReceiveWeatherInfo());

			// System.out.println(cityWeather + "->_->:" + weatherCloth);
		}
	}

	private class ReceiveWeatherInfo extends Behaviour {
		String weatherCloth;

		private boolean received = false;

		@Override
		public boolean done() {
			return received;
		}

		@Override
		public void action() {

			MessageTemplate mt = MessageTemplate.MatchInReplyTo("toclothAgent");

			ACLMessage rec = receive(mt);
			if (rec != null) {
				System.out.println("->_->:" + rec.getContent());
			} else {
				block();
			}

		}

	}
}
