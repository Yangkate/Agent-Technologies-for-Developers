package fi.jyu.ties454.assignment3.group1.task3;

import java.util.Random;

import com.google.common.base.Optional;

import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.Location;
import fi.jyu.ties454.cleaningAgents.infra.Orientation;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.AreaCleaner;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JackieChanRotator;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JumpForwardMover;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner2 extends GameAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		Tracker t = new Tracker();
		// it is safe to obtain parts in setup(), but using them must be done in
		// behaviors!
		// getting the device is don using the getDevice call.
		// This call returns an Optional<E extends Device> which is present if
		// the device was successfully obtained
		ForwardMover jump = t.registerForwardMover(this.getDevice(
				DefaultDevices.JumpForwardMover.class).get());
		ForwardMover mover = t.registerForwardMover(this.getDevice(
				DefaultDevices.BasicForwardMover.class).get());
		AreaCleaner areaCleaner = this.getDevice(
				DefaultDevices.AreaCleaner.class).get();
		Rotator rotator = t.registerRotator(this.getDevice(
				DefaultDevices.BasicRotator.class).get());

		ACLMessage requestMsg = new ACLMessage(ACLMessage.SUBSCRIBE);
		AID dest = new AID("Cleaner5", AID.ISLOCALNAME);
		requestMsg.addReceiver(dest);
		requestMsg.setPerformative(ACLMessage.SUBSCRIBE);
		requestMsg.setContent("2");
		send(requestMsg);
		this.addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				addBehaviour(new Behaviour() {// add means a new behaviour is
					// created

					private boolean received = false;

					@Override
					public boolean done() {
						return this.received;
					}

					public void action() {
						ACLMessage rec = receive();
						if (rec != null) {
							try {

								Location l = (Location) rec.getContentObject();
								System.out.println("receive Location" + l);
								if (t.getLocation().X < l.X) {
									TD(t.getOrientation(), Orientation.E);
									MoveX(l.X - t.getLocation().X);
									if (t.getLocation().Y < l.Y) {
										TD(Orientation.E, Orientation.N);
										MoveY(l.Y - t.getLocation().Y);
									} else {
										TD(Orientation.E, Orientation.S);
										MoveY(l.Y - t.getLocation().Y);
									}
									
								}
								else {
									TD(t.getOrientation(), Orientation.W);
									MoveX(l.X - t.getLocation().X);
									if (t.getLocation().Y < l.Y) {
										TD(Orientation.W, Orientation.N);
										MoveY(l.Y - t.getLocation().Y);
									} else {
										TD(Orientation.W, Orientation.S);
										MoveY(l.Y - t.getLocation().Y);
									}
								}
								
							}

							catch (UnreadableException e) {
								throw new Error(e);
							}
							areaCleaner.clean();
							ACLMessage reply = rec.createReply();
							reply.setPerformative( ACLMessage.REQUEST );
							reply.setContent("cleaner2 down" );
							send(reply);
						} else {
							block();// this block just wait this behaviour,
									// waiting for response
						}
					}

					public void TD(Orientation oDir, Orientation mDir) {
						// if(oDir.equals(Orientation.N))
						if ((oDir == Orientation.N && mDir == Orientation.E)
								|| (oDir == Orientation.E && mDir == Orientation.S)
								|| (oDir == Orientation.S && mDir == Orientation.W)
								|| (oDir == Orientation.W && mDir == Orientation.N)) {
							rotator.rotateCW();
							System.out.println("rotate2 cw");
						}
						if ((oDir == Orientation.N && mDir == Orientation.W)
								|| (oDir == Orientation.W && mDir == Orientation.S)
								|| (oDir == Orientation.S && mDir == Orientation.E)
								|| (oDir == Orientation.E && mDir == Orientation.N)) {
							rotator.rotateCCW();
							System.out.println("rotate2 ccw");
						}

						if ((oDir == Orientation.N && mDir == Orientation.S)
								|| (oDir == Orientation.S && mDir == Orientation.N)
								|| (oDir == Orientation.W && mDir == Orientation.E)
								|| (oDir == Orientation.E && mDir == Orientation.W)) {
							rotator.rotateCCW();
							rotator.rotateCCW();
							System.out.println("rotate2  ccw  ccw");
						}
					}

					public void MoveX(int x) {
						int stepx;
						stepx = Math.abs(x);
						if (stepx / 5 == 0) {
							for (int i = 0; i < stepx / 5; i++) {
								jump.move();
								System.out.println("move2 X");
							}

						} else {
							for (int i = 0; i < stepx / 5; i++) {
								jump.move();
								System.out.println("move2 X");
							}
							for (int i = 0; i < stepx % 5; i++) {
								mover.move();
								System.out.println("move2 X");
							}
						}
					}

					public void MoveY(int y) {

						int stepy;
						stepy = Math.abs(y);
						if (stepy / 5 == 0) {
							for (int i = 0; i < stepy / 5; i++) {
								jump.move();
								System.out.println("move2 Y");
							}

						} else {
							for (int i = 0; i < stepy / 5; i++) {
								jump.move();
								System.out.println("move2 Y");
							}
							for (int i = 0; i < stepy % 5; i++) {
								mover.move();
								System.out.println("move2 Y");
							}
						}
					}
				});

			}

		});

	}

}