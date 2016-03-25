package fi.jyu.ties454.assignment3.group1.task2;

import java.util.Optional;
import java.util.Random;

import fi.jyu.ties454.cleaningAgents.actuators.BackwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.AreaCleaner;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.BasicWallSensor;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JackieChanRotator;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JumpForwardMover;
import jade.core.behaviours.OneShotBehaviour;
import jade.tools.introspector.Sensor;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner extends GameAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		Tracker t = new Tracker();
		// it is safe to obtain parts in setup(), but using them must be done in
		// behaviors!
		// getting the device is don using the getDevice call.
		// when this call returns true, the update method of the agent has been
		// called
		ForwardMover jump = t.registerForwardMover(this.getDevice(
				DefaultDevices.JumpForwardMover.class).get());
		Rotator fastRotator = t.registerRotator(this.getDevice(
				DefaultDevices.JackieChanRotator.class).get());
		AreaCleaner areaCleaner = this.getDevice(
				DefaultDevices.AreaCleaner.class).get();
		ForwardMover frogMover = t.registerForwardMover(this.getDevice(
				DefaultDevices.FrogHopperForwardMover.class).get());
		BackwardMover BackMover = t.registerBackwardMover(this.getDevice(
				DefaultDevices.BasicBackwardMover.class).get());
		BasicWallSensor wallSensor = this.getDevice(
				DefaultDevices.BasicWallSensor.class).get();
		this.addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				Random r = new Random();
				while (true) {
					jump.move();
					areaCleaner.clean();
					if (wallSensor.canContinueAtLeast() == 0) {
						fastRotator.rotateCW();
						fastRotator.rotateCW();
					}

					if (r.nextInt(5) == 0) {
						fastRotator.rotateCW();
					}
					if (r.nextInt(5) == 0) {
						frogMover.move();
						fastRotator.rotateCW();
						areaCleaner.clean();
					}
					if (r.nextInt(7) == 1) {
						BackMover.move();
						fastRotator.rotateCW();
						fastRotator.rotateCW();
					}
					System.out.println("Currently at " + t.getLocation() + " heading " + t.getOrientation());

				}
			}
		});
	}
}
