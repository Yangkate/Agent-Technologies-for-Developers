package fi.jyu.ties454.jiayang.assignment2.task3;

import java.util.Random;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Run {
	public static void main(String[] args) {
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);

		try {
			ac.acceptNewAgent("auction", new AuctioneerAgent()).start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random r = new Random(3456789L);
		for (int i = 0; i < 20; i++) {
			try {

				ac.acceptNewAgent("bider" + i,
						new BidderAgent(500 + r.nextInt(501))).start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
