package fi.jyu.ties454.assignment3.group1.task1;

import java.util.Optional;
import java.util.Random;

import fi.jyu.ties454.assignment3.group1.task2.MyDirtier;
import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.BasicCleaner;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.DirtExplosion;
import jade.core.behaviours.OneShotBehaviour;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner extends GameAgent {

	private static final long serialVersionUID = 1L;
	/*
	 * the following three sensors and actuators are received from the install
	 * method below.
	 *
	 * Note that sensors and actuators MUST only be used inside behaviors on the
	 * agent thread. In other words, you are not allowed to use them directly in
	 * the setup method, but have to add a behavior inside which they can be
	 * used. Further, you are also not allowed to use them from other threads
	 * than the agent's own thread.
	 */
	// The mover has a move() method. This moves the robot one cell forward.
	// Returns 0 if moving forward failed: hit a wall.
	private ForwardMover mover;
	// the rotator has a rotateCW() and rotateCCW() method for clockwise and
	// counter-clockwise rotation
	private Rotator rotator;
	// the cleaner has a clean() method, which cleans the cell under the robot
	private Cleaner cleaner;

	@Override
	protected void setup() {
		Tracker t = new Tracker();
		 mover = t.registerForwardMover(this.getDevice(DefaultDevices.JumpForwardMover.class).get());
		 rotator = t.registerRotator(this.getDevice(DefaultDevices.BasicRotator.class).get());
		// A cleaner does not move the robot, so no need to register it
		 cleaner = this.getDevice(DefaultDevices.AreaCleaner.class).get();
		

		this.addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				Random rand = new Random();
				// no money -> use free stuff
				while (true) {
					int distance = mover.move();
					if (distance > 0) {
						cleaner.clean();
						if (rand.nextInt(5) == 0) {
							rotator.rotateCW();
						}
					} else {
						rotator.rotateCW();
					}
					System.out.println("Currently at " + t.getLocation() + " heading " + t.getOrientation());
				}
				
			}
		});
	}

}
