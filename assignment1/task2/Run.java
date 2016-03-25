package fi.jyu.ties454.jiayang.assignment1.task2;

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
	        ac.acceptNewAgent("Hello", new HelloAgent()).start();
	        ac.acceptNewAgent("Hi", new HiAgent()).start();
	    } catch (StaleProxyException e) {
	        throw new Error(e);
	    }
	}
}