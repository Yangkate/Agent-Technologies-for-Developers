package fi.jyu.ties454.assignment3.group1.task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import fi.jyu.ties454.cleaningAgents.actuators.Dirtier;
import fi.jyu.ties454.cleaningAgents.actuators.ForwardMover;
import fi.jyu.ties454.cleaningAgents.agent.GameAgent;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.AreaDirtier;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.BasicRotator;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.DirtExplosion;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.FrogHopperForwardMover;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JackieChanRotator;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.JumpForwardMover;
import fi.jyu.ties454.cleaningAgents.infra.DefaultDevices.LeftMover;
import jade.core.behaviours.OneShotBehaviour;

/**
 * The agent extends from {@link GameAgent}, which is actually a normal JADE
 * agent. As an extra it has methods to obtain sensors and actuators.
 */
public class MyDirtier1 extends GameAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {

		BasicRotator rotator = this
				.getDevice(DefaultDevices.BasicRotator.class).get();
		Dirtier d = this.getDevice(DefaultDevices.BasicDirtier.class).get();
		LeftMover lm = this.getDevice(DefaultDevices.LeftMover.class).get();
		ForwardMover f = this.getDevice(DefaultDevices.BasicForwardMover.class)
				.get();
		Optional<JackieChanRotator> jr = this.getDevice(DefaultDevices.JackieChanRotator.class);
		Optional<FrogHopperForwardMover> frogForward = this.getDevice(DefaultDevices.FrogHopperForwardMover.class);

		this.addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				Random r = new Random();
				Optional<AreaDirtier> areaDirtier = MyDirtier1.this
						.getDevice(DefaultDevices.AreaDirtier.class);
				Optional<JumpForwardMover> jumper = MyDirtier1.this
						.getDevice(DefaultDevices.JumpForwardMover.class);
			
				while(true){
					searchDimensions(rotator, jumper, f, jr,d,r.nextDouble());
					searchDoorLocations(rotator,lm,f,jr,d,r.nextDouble());
					makeDirt(areaDirtier, jumper,jr, rotator, d, f,frogForward);
					}
			}
		});

	}

	public void searchDoorLocations(BasicRotator br, LeftMover lm,ForwardMover fm,
			Optional<JackieChanRotator> fr,Dirtier d,double r) {
		boolean doorFound = false;
		/*
		 * Tracker t = new Tracker(); t.registerRotator(br); Orientation o =
		 * t.getOrientation();
		 */
		//d.makeMess();
		Random rnd = new Random();
		if(r<0.5){
			if (fr.isPresent()) {
				fr.get().rotateCW();
			} else {
				br.rotateCW();
			}}else{
				if (fr.isPresent()) {
					fr.get().rotateCCW();
				} else {
					br.rotateCCW();
				}
			}
		
		while(doorFound ==false){
			if(rnd.nextDouble()<0.3){
				d.makeMess();
			}
		
		if (lm.move() == 0)// odd
		{
			if(r<0.5){
				if (fr.isPresent()) {
					fr.get().rotateCW();
				} else {
					br.rotateCW();
				}}else{
					if (fr.isPresent()) {
						fr.get().rotateCCW();
					} else {
						br.rotateCCW();
					}
				}
			// fr.get().rotateCW();
			while (lm.move() != 0 && doorFound == false) {
				if(r<0.5){
					if (fr.isPresent()) {
						fr.get().rotateCCW();
					} else {
						br.rotateCCW();
					}}else{
						if (fr.isPresent()) {
							fr.get().rotateCW();
						} else {
							br.rotateCW();
						}
					}
					if(rnd.nextDouble()<0.3){
					d.makeMess();
				}
				if (lm.move() != 0) {
					doorFound = true;// door found
				} else {
					if(r<0.5){
						if (fr.isPresent()) {
							fr.get().rotateCW();
						} else {
							br.rotateCW();
						}}else{
							if (fr.isPresent()) {
								fr.get().rotateCCW();
							} else {
								br.rotateCCW();
							}
						}
					// fr.get().rotateCW();
				}
			}
		} else { // other corners
			if(r<0.5){
				if (fr.isPresent()) {
					fr.get().rotateCCW();
					fr.get().rotateCCW();
				} else {
					br.rotateCCW();
					br.rotateCCW();
				}}else{
					if (fr.isPresent()) {
						fr.get().rotateCW();
						fr.get().rotateCW();
					} else {
						br.rotateCW();
						br.rotateCW();
					}
				}
			
			lm.move();
			if(r<0.5){
				if (fr.isPresent()) {
					fr.get().rotateCCW();
				} else {
					br.rotateCCW();
				}}else{
					if (fr.isPresent()) {
						fr.get().rotateCW();
					} else {
						br.rotateCW();
					}
				}
			// fr.get().rotateCCW();
			while (lm.move() != 0 && doorFound == false) {
				if(r<0.5){
					if (fr.isPresent()) {
						fr.get().rotateCW();
					} else {
						br.rotateCW();
					}}else{
						if (fr.isPresent()) {
							fr.get().rotateCCW();
						} else {
							br.rotateCCW();
						}
					}
					if(rnd.nextDouble()<0.3){
					d.makeMess();
				}
				// fr.get().rotateCW();
				if (lm.move() != 0) {
					doorFound = true;// door found
					
				} else {
					if(r<0.5){
						if (fr.isPresent()) {
							fr.get().rotateCCW();
						} else {
							br.rotateCCW();
						}}else{
							if (fr.isPresent()) {
								fr.get().rotateCW();
							} else {
								br.rotateCW();
							}
						}
					// fr.get().rotateCCW();
				}
			}

		}
		if(rnd.nextDouble()<0.4)
		{
			if (fr.isPresent()) {
				fr.get().rotateCCW();
			} else {
				br.rotateCCW();
			}
		}
		}
		// once door found move forward turn CW and end
		int tot = 0,dist;
		if((dist =lm.move())!=0 && tot<4){
			tot = tot + dist;
		}
		//while((dist = dist+lm.move())<3);
	if(rnd.nextDouble()<0.5){	
		if (fr.isPresent()) {
			fr.get().rotateCW();
			//fr.get().rotateCW();
		} else {
			br.rotateCW();
			//br.rotateCW();
		}}else{if (fr.isPresent()) {
			fr.get().rotateCCW();
			//fr.get().rotateCCW();
		} else {
			br.rotateCCW();
			//br.rotateCCW();
		}
			
		}

	}

	public void searchDimensions(BasicRotator br, Optional<JumpForwardMover> j,
			ForwardMover fm, Optional<JackieChanRotator> fr,Dirtier d,double r) {
		// while (true) {
		Random rnd = new Random();
		List<Integer> listCord = new ArrayList<Integer>();
		for (int i = 0; i < 2 + rnd.nextInt(4); i++) {
			if (j.isPresent()) {
				int count, dist = 0;
				while ((count = j.get().move()) != 0) {
					dist = dist + count;
					if(rnd.nextDouble()<0.3){
						d.makeMess();
					}
				}
				listCord.add(dist);
				if(r<0.5){
				if (fr.isPresent()) {
					fr.get().rotateCW();
				} else {
					br.rotateCW();
				}}else{
					if (fr.isPresent()) {
						fr.get().rotateCCW();
					} else {
						br.rotateCCW();
					}
				}
			} else {
				int dist = 0, count = 0;
				while ((count = fm.move()) != 0) {
					dist = dist + count;
					if(rnd.nextDouble()<0.3){
						d.makeMess();
					}
				}
				if(r<0.5){
					if (fr.isPresent()) {
						fr.get().rotateCW();
					} else {
						br.rotateCW();
					}}else{
						if (fr.isPresent()) {
							fr.get().rotateCCW();
						} else {
							br.rotateCCW();
						}
					}
				listCord.add(dist);
			}
		}
		Collections.sort(listCord);
		for (int item : listCord) {
			System.out.println("dim " + item + " , ");
		}
	}

	public void makeDirt(Optional<AreaDirtier> areaDirtier,
			Optional<JumpForwardMover> jumper,Optional<JackieChanRotator> jackieRt, BasicRotator rotator, Dirtier d,
			ForwardMover f,Optional<FrogHopperForwardMover> frogFW) {
		Random r = new Random();
		if (jumper.isPresent()) {
			jumper.get().move();
			for(int i=0;i<5;i++){
			if(r.nextDouble()<0.5){
					if(jackieRt.isPresent()){
						jackieRt.get().rotateCCW();
					}
					else{
						rotator.rotateCCW();
					}
				}
				else{
					if(jackieRt.isPresent()){
						jackieRt.get().rotateCW();
					}
					else{
						rotator.rotateCW();
					}
				}
			if(frogFW.isPresent()){
				frogFW.get().move();
			}
				if(r.nextDouble()<0.4){
				  if(jumper.isPresent()){
					  if(jumper.get().move()==5){
						  if (areaDirtier.isPresent() && !areaDirtier.get().isEmpty()) {
								areaDirtier.get().makeMess();
							} else {
								// just normal mess
								d.makeMess();
							}
					  }
				  }
					if (areaDirtier.isPresent() && !areaDirtier.get().isEmpty()) {
						areaDirtier.get().makeMess();
					} else {
						// just normal mess
						d.makeMess();
					}
				}
				else{
					d.makeMess();
					}
					if (r.nextInt(5) == 0) {
						rotator.rotateCW();
					}
					if (r.nextInt(25) == 0) {
						// attempt dirt explosion. This only works if
						// there is money left.
						Optional<DirtExplosion> dirtExplosion = MyDirtier1.this
								.getDevice(DefaultDevices.DirtExplosion.class);
						if (dirtExplosion.isPresent()) {
							dirtExplosion.get().makeMess();
						}
					}
					if (r.nextInt(10) == 0) {
						f.move();
					}
					if(f.move()==0){
						if(jackieRt.isPresent()){
							jackieRt.get().rotateCCW();
							jackieRt.get().rotateCCW();
						}
					}
				}
			}
		}
	

}