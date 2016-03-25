package fi.jyu.ties454.jiayang.assignment2.task1;

import java.io.IOException;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Run {
	public static void main(String[] args) throws IOException {
		Properties pp = new Properties();
		pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
		Profile p = new ProfileImpl(pp);
		AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);
		
		try {
			ac.acceptNewAgent("weatherAgent", new weatherAgent()).start();
		} catch (StaleProxyException e) {
			
			e.printStackTrace();
		}
		try {
			ac.acceptNewAgent("clothAgent", new clothAgent()).start();
		} catch (StaleProxyException e) {
			
			e.printStackTrace();
		}
		try {
			ac.acceptNewAgent("customerAgent", new customerAgent()).start();
		} catch (StaleProxyException e) {
			
			e.printStackTrace();
		}

		
	}
}
