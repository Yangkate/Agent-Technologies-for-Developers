package fi.jyu.ties454.assignment3.group1.task4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.jyu.ties454.assignment3.group1.task4.MyCleaner1.doorInfor;
import fi.jyu.ties454.cleaningAgents.actuators.Cleaner;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.actuators.Rotator;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.agent.Tracker;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/**
 * The agent extends from CleaningAgent, which is actually a normal JADE agent.
 * As an extra it has methods to obtain sensors and actuators.
 */
public class MyCleaner3 extends GameAgent {

	private static final long serialVersionUID = 1L;
	

	@Override
	protected void setup() {
		
		// it is safe to obtain parts in setup(), but using them must be done in
		// behaviors!
		// getting the device is don using the getDevice call.
		// This call returns an Optional<E extends Device> which is present if
		// the device was successfully obtained
		
		Tracker t = new Tracker();
		
		
		ForwardMover mover = t.registerForwardMover(this.getDevice(DefaultDevices.BasicForwardMover.class).get());
		Rotator rotator = t.registerRotator(this.getDevice(DefaultDevices.BasicRotator.class).get());
		// A cleaner does not move the robot, so no need to register it
		Cleaner cleaner = this.getDevice(DefaultDevices.BasicCleaner.class).get();
		
		addBehaviour(new Behaviour() {
			HashMap<Integer, List<doorInfor>> roomInfor = new HashMap<Integer, List<doorInfor>>();
			List<doorInfor> doorsInfor=new ArrayList<doorInfor>();
			int roomNumber=0;
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void action() {
				ACLMessage msg = receive();
				if(msg !=null){
					

						try {
							List<doorInfor> dInfor =(List<doorInfor>) msg.getContentObject();
							
							//System.out.println("cleaner 3"+dInfor.get(0).getLocation());
						
							
							if(roomInfor.size() !=0){
							for(int i=0;i<roomNumber+1;i++){
								
								
								if(roomInfor.get(0)==null){
									
									
									return;
									}
								if(
										roomInfor.get(roomNumber)==null
										
										){
									
									return;
									}
								for(int j=0;j<roomInfor.get(i).size();j++){
									
									if(roomInfor.get(roomNumber).get(j).equals(dInfor.get(0))){
										
										return;
										}
								}
								
							}
							}
							
							roomInfor.put(roomNumber,dInfor);
							
						
							
						} catch (UnreadableException e) {
							
							e.printStackTrace();
						}
					
					
				}else{
					block();
				System.out.println("--3--msg is null");
				}
				
				
				//walk and clear
				
				//hit the wall
				boolean moveOrNot = true;
				while (moveOrNot) {
					int distance = mover.move();
					// System.out.println("--77---");
					if (distance == 0) {
						// System.out.println("--88---");
						moveOrNot = false;
						rotator.rotateCCW();
						
					}
				}
				//walk along woth the wall
				while(true){
					int distanceLine = mover.move();
					cleaner.clean();
					if (distanceLine ==0) {// read at corner	
						rotator.rotateCCW();
						
						}
					
				}
				
				
				
				
			}

//			
			
		});
		
		
	}

	

}
