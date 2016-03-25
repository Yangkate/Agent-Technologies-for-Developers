package fi.jyu.ties454.jiayang.assignment1.task5;

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
	    	ac.acceptNewAgent("Broker", new BrokerAgent()).start();
	    	for(int i=0;i<25;i++){
	        ac.acceptNewAgent("Publisher"+i, new PublisherAgent()).start();
	    	}
	    	for(int i=0;i<50;i++){
		        ac.acceptNewAgent("Subscriber"+i, new SubscriberAgent()).start();
		    	}
	    } catch (StaleProxyException e) {
	        throw new Error(e);
	    }
	}
}
