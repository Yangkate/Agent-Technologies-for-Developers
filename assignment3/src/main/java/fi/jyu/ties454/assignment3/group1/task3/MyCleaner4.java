package fi.jyu.ties454.assignment3.group1.task3;

import java.io.IOException;
import java.util.Random;

import com.google.common.base.Optional;

import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.Dirtier;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.FloorState;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.BasicDirtSensor;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JackieChanRotator;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JumpForwardMover;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner4 extends GameAgent {

	private static final long serialVersionUID = 1L;

//	private ForwardMover mover;
//	private Rotator rotator;
//	private Cleaner cleaner;

	@Override
	protected void setup() {
		Tracker t = new Tracker();
	    BasicDirtSensor Dirtsensor = this.getDevice(DefaultDevices.BasicDirtSensor.class).get();
		ForwardMover jump = t.registerForwardMover(this.getDevice(
				DefaultDevices.JumpForwardMover.class).get());
		Rotator rotator = t.registerRotator(this.getDevice(
				DefaultDevices.BasicRotator.class).get());
		this.addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				Random rand = new Random();
				
				while (true) {
					int distance = jump.move();
					if (distance > 0) {
						FloorState status=Dirtsensor.inspect();
						if(status== FloorState.DIRTY){
							ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
							AID dest = new AID("Cleaner5", AID.ISLOCALNAME);
							msg.addReceiver(dest);
							msg.setLanguage("English");
							msg.setPerformative(ACLMessage.INFORM);
							
								try {
									msg.setContentObject(t.getLocation());
									
								} catch (IOException e) {
									throw new Error(e);
								}
						
							send(msg);
						}
						
						if (rand.nextInt(5) == 0) {
							rotator.rotateCW();
						}
					} else {
						rotator.rotateCW();
					}
				}
			}
		});
	}

	

}
