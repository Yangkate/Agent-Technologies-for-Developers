package fi.jyu.ties454.jiayang.assignment2.task2;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Run {
	public static void main(String[] args) {
		//main container
	    Properties pp = new Properties();
	    pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
	    Profile p = new ProfileImpl(pp);
	    AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);
	    // non-main container 1
	    Properties pp2 = new Properties();
	    pp2.setProperty(Profile.MAIN, Boolean.FALSE.toString());
	    pp2.setProperty(Profile.MAIN_HOST, "127.0.0.1");
	    pp2.setProperty(Profile.MAIN_PORT, Integer.toString(1099));
	    Profile p2 = new ProfileImpl(pp2);
	    AgentContainer ac2 = jade.core.Runtime.instance().createAgentContainer(p2);
	    
	    try {
	        ac.acceptNewAgent("JumpingAgent", new JumpingAgent()).start();
	        
	    } catch (StaleProxyException e) {
	        throw new Error(e);
	    }
	   
	    
	    
	}
}
