package fi.jyu.ties454.assignment3.group1.task4;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.jyu.ties454.assignment3.group1.task4.MyCleaner1.doorInfor;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.AreaCleaner;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.BasicDirtSensor;
import fi.jyu.ties454.cleaningAgents.infra.Floor;
import fi.jyu.ties454.cleaningAgents.infra.Location;
import fi.jyu.ties454.cleaningAgents.infra.Orientation;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner2 extends GameAgent {

	private static final long serialVersionUID = 1L;

	java.util.Optional<Boolean> dirty;
	Floor map;
	doorInfor firstDoorOfnewRoom;
	boolean firstRoom = true;
	boolean startSate=true;
	@Override
	protected void setup() {

		ArrayList<ArrayList<doorInfor>> allDoorsInfor = new ArrayList<ArrayList<doorInfor>>();
		ArrayList<Location> doorsAgent2Walked = new ArrayList<Location>();
		ArrayList<Location> doorsAgent1Walked = new ArrayList<Location>();
		Tracker t = new Tracker();
		// it is safe to obtain parts in setup(), but using them must be done in
		// behaviors!
		// getting the device is don using the getDevice call.
		// when this call returns true, the update method of the agent has been
		// called
		ForwardMover LeftMover = t.registerLeftMover(this.getDevice(
				DefaultDevices.LeftMover.class).get());

		AreaCleaner areaCleaner = this.getDevice(
				DefaultDevices.AreaCleaner.class).get();
		Rotator JackieChanRotator = t.registerRotator(this.getDevice(
				DefaultDevices.JackieChanRotator.class).get());
		BasicDirtSensor BasicDirtSensor = this.getDevice(
				DefaultDevices.BasicDirtSensor.class).get();

	     // record doors walked by other agent
		
		this.addBehaviour(new OneShotBehaviour() {

			@Override
			public void action() {

				addBehaviour(new Behaviour() {

					@Override
					public boolean done() {

						return false;
					}

					@Override
					public void action() {
                  
						addBehaviour(new Behaviour() {
							
							@Override
							public boolean done() {
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public void action() {
								// record to door walked by cleaner2
								ACLMessage msg = receive();

								if (msg != null){
									if(msg.getSender() !=null){

									}
										if(msg.getSender().getLocalName().equals("Cleaner1")) {
											Location door = null;
									try {
										door = (Location) msg.getContentObject();
									} catch (UnreadableException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									doorsAgent1Walked.add(door);
									//System.out.println("cleaner 2--"+door);

								}
							}else{block();}
								
							}

							});

						//map model start
						// hit wall and CCW
						
						boolean moveOrNot = true;
						if(startSate){
						JackieChanRotator.rotateCCW();
						//JackieChanRotator.rotateCCW();
						startSate=false;
						}
						Orientation firstDoortest = t.getOrientation();
						while (moveOrNot) {
							int distance = LeftMover.move();
							// System.out.println("--77---");
							if (distance == 0) {
								// System.out.println("--88---");
								moveOrNot = false;
								JackieChanRotator.rotateCCW();
								Orientation firstDoortest2 = t.getOrientation();
								LeftMover.move();
							}
						}

						Location firstDoor = t.getLocation();
						// List<doorInfor>
						// doorsOfStartRoom=searchDoors(t,firstDoor );
						addBehaviour(new searchDoors(firstDoor));
                       //map model end
						System.out.println("cleaner 2 finish one whole search");

					}

				});

			}

			// --
			// CCLoop
			class searchDoors extends OneShotBehaviour {

				// Tracker t;
				Location doorOfFirstEntry;
				

				public searchDoors(Location doorOfFirstEntry) {
					// this.t=t;
					this.doorOfFirstEntry = doorOfFirstEntry;
					
				}

				@Override
				public void action() {
					
						searchDoors2(t, doorOfFirstEntry);
					

				}

				final public List<doorInfor> searchDoors2(Tracker t,
						Location doorOfFirstEntry) {

					ArrayList<doorInfor> doorsOfRoom = new ArrayList<doorInfor>();

					//add the first pass-by door
					//doorsOfRoom.add(firstDoorOfnewRoom);
					
					boolean LmoveOrNot = true;
					doorInfor door = null;
					boolean firstRun=true;
					boolean firstDoorAdded=true;
					while (LmoveOrNot) {
                        int step=0;
						if (firstRun||!t.getLocation().equals(doorOfFirstEntry)) {
							firstRun=false;
							int distanceLine = LeftMover.move();
                             
							if (distanceLine > 0) {// not read at corner

								JackieChanRotator.rotateCW();
								int distanceWall = LeftMover.move();
								if (distanceWall == 0) {// hit wall
									JackieChanRotator.rotateCCW();

								} else {// find door
									System.out.println("Entry doot already");
									JackieChanRotator.rotateCW();
									JackieChanRotator.rotateCW();
									LeftMover.move();
									JackieChanRotator.rotateCW();
									// record location of other non-original

									door = new doorInfor(t.getLocation(), t
											.getOrientation());
									if(firstDoorAdded&&firstDoorOfnewRoom!=null){
										doorsOfRoom.add(firstDoorOfnewRoom);
									firstDoorAdded=false;
									//tell agent another agent this door is walked
									sendMsg(firstDoorOfnewRoom.getLocation(), 1);
									}
									if (door != null) {
										doorsOfRoom.add(door);
										System.out.println(" find a new door" + door.getLocation());
									}
								}
								
								step++;
								cleanRandomly(step,3);
							} else {// read at corner
								JackieChanRotator.rotateCCW();
							}
							// get curent location here

						} else {

							LmoveOrNot = false;
						}

					}
					// send room with its doors to BossAgent below
					if(doorsOfRoom.size()>1){if(doorsOfRoom.get(0).getLocation().equals(doorsOfRoom.get(doorsOfRoom.size()-1).getLocation())){doorsOfRoom.remove(doorsOfRoom.size()-1);}}
					sendMsg(doorsOfRoom, 3);
					if(doorsOfRoom.size()>2){doorsOfRoom.remove(doorsOfRoom.size()-1);}
					allDoorsInfor.add(doorsOfRoom);

					// sendMsg(t.getLocation());
					// String content = "done";
					// sendMsg(content);// ask next door from boss
					// System.out.println(" doorsOfRoom" + doorsOfRoom);

					// to next room
					moveToNextRoom(doorsOfRoom);

					return doorsOfRoom;
				}

				
				public void cleanRandomly(int step,int doPerStep){
					Random r=new Random();
					if(r.nextInt(4)== step || step%doPerStep==0){
						areaCleaner.clean();	
					}
				}
				public void moveToNextRoom(ArrayList<doorInfor> doorsOfRoom) {

					if(doorsOfRoom.size()==0&&allDoorsInfor.size()!=0){doorsOfRoom=allDoorsInfor.get(allDoorsInfor.size()-1);}//else{return;}
					for (int i = 0; i < doorsOfRoom.size(); i++) {
						if (doorsAgent1Walked.contains(doorsOfRoom.get(i)
								.getLocation())) {
							doorsOfRoom.remove(i);
						}
					}
					
//					System.out.println(" door"
//							+ doorsOfRoom.get(doorsOfRoom.size() - 1)
//									.getLocation());
					if(doorsOfRoom.size()<1){return;}
					CCLoopBetweenRooms(doorsOfRoom.get(doorsOfRoom.size() - 1));

					// record history door
					doorsAgent2Walked.add(doorsOfRoom.get(
							doorsOfRoom.size() - 1).getLocation());
					// send email to another agent which door has been walked

					sendMsg(doorsOfRoom.get(doorsOfRoom.size() - 1)
							.getLocation(), 1);

					// remove walkedDoor
					allDoorsInfor.get(allDoorsInfor.size()-1).remove(doorsOfRoom.size() - 1);
					if (!firstRoom && doorsOfRoom.size() == 0) {
						//System.out.println(" move to next door" + doorsOfRoom);
						allDoorsInfor.remove(allDoorsInfor.size()-1);//clear one room(with doors) from all room record
						moveToNextRoom(doorsOfRoom);
						
					} else {
						firstRoom = false;
						LeftMover.move();
						// ?
						searchDoors2(t, t.getLocation());

					}

				}

				public void sendMsg(Serializable content, int i) {

					ACLMessage tellRoom = new ACLMessage(ACLMessage.INFORM);
					//String receiver = "Cleaner" + i;

					AID dest = null;
					if(i==1){ dest = new AID("Cleaner1", AID.ISLOCALNAME);}
					if(i==3){ dest = new AID("Cleaner3", AID.ISLOCALNAME);}
					tellRoom.addReceiver(dest);
					tellRoom.setPerformative(ACLMessage.INFORM);
					try {
						tellRoom.setContentObject(content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					send(tellRoom);
				}

				// --
				public void CCLoopBetweenRooms(doorInfor desDoorInfor) {

					Location desDoor = desDoorInfor.getLocation();
					Orientation desDoorO = desDoorInfor.getOrientation();
					System.out.println(" move to next door of another room" + desDoor);

					// move
					if (t.getLocation().X < desDoor.X) {
						TD(Orientation.E);
					} else {
						TD(Orientation.W);
					}
					moveForward(desDoor.X - t.getLocation().X);

					if (t.getLocation().Y < desDoor.Y) {
						TD(Orientation.S);
					} else {
						TD(Orientation.N);
					}
					moveForward(desDoor.Y - t.getLocation().Y);

					TD( desDoorO);
					crabMove(2);
					//add first door of new room
					JackieChanRotator.rotateCW();
					JackieChanRotator.rotateCW();
					
					 firstDoorOfnewRoom = new doorInfor(t.getLocation(), t
							.getOrientation());
					JackieChanRotator.rotateCCW();
					JackieChanRotator.rotateCCW();
					LeftMover.move();// move one step to right so that the first
										// door to find is the door Entried the
										// room
					JackieChanRotator.rotateCW();
					JackieChanRotator.rotateCW();// face to the wall
					
					
					//addBehaviour(new searchDoors(t.getLocation(), 1));
					//

				}

				// --
				// -
				public void TD(Orientation mDir) {
					Orientation oDir = t.getOrientation();
					if ((oDir == Orientation.N && mDir == Orientation.E)
							|| (oDir == Orientation.E && mDir == Orientation.S)
							|| (oDir == Orientation.S && mDir == Orientation.W)
							|| (oDir == Orientation.W && mDir == Orientation.N)) {
						JackieChanRotator.rotateCW();
						//System.out.println("rotate1 cw");
					}
					if ((oDir == Orientation.N && mDir == Orientation.W)
							|| (oDir == Orientation.W && mDir == Orientation.S)
							|| (oDir == Orientation.S && mDir == Orientation.E)
							|| (oDir == Orientation.E && mDir == Orientation.N)) {
						JackieChanRotator.rotateCCW();
						//System.out.println("rotate1 ccw");
					}

					if ((oDir == Orientation.N && mDir == Orientation.S)
							|| (oDir == Orientation.S && mDir == Orientation.N)
							|| (oDir == Orientation.W && mDir == Orientation.E)
							|| (oDir == Orientation.E && mDir == Orientation.W)) {
						JackieChanRotator.rotateCCW();
						JackieChanRotator.rotateCCW();
						//System.out.println("rotate1  ccw  ccw");
					}
				}

				// -
				// -3
				public void moveForward(int x) {
					int stepx, stepy;
					stepx = Math.abs(x);

					crabMove(stepx);

				}

				// -3

				// -4
				public void crabMove(int i) {
					JackieChanRotator.rotateCW();
					int moveStep=0;
					while (i > 0) {
                        
						LeftMover.move();
						i--;
						moveStep++;
					 this.cleanRandomly(moveStep, 3);
					}
					JackieChanRotator.rotateCCW();

				}
				// -4
				// -5

				// -5
				// --

			}

		});

	}




}
