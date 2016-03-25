Assignment 3: Agent Coordination, Resource Allocation, and Learning
Goal

After completing these exercises the student should have a deeper understanding on how agents can collaborate in an environment with restricted resources. Further, he or she gets a chance to implement basic learning algorithms. Concepts covered are coordination, software robots, sensors and actuators, and resource allocation.

Prerequisites

Attendance to the first six lectures by Vagan is recommended. Otherwise, self-study of the materials is necessary. Most of the the concepts used are dealt with in the theory part of the course.

To be able to start working you should have completed the first and second assignment. The resources of these exercises can also help you to complete this task.

You must have knowledge of programming in Java or be prepared to work on your skills during the course.

Tasks

This assignment is loosely based on the essay which needs to be written for the theoretical course. The goal is to program agents (software robots) such that they together keep the space in which they are placed as clean as possible.

The assignment consists of 4 parts, which you are advised to make in order. The first task introduces the cleaners. The goal is to clean a rectangular room. The second task introduces more advanced sensors and actuators the cleaners can use as well as their opponents, the dirtiers. The third task focuses on coordination of multiple cleaners to avoid parts of a room being cleaned twice. In the fourth task you have to program an efficient team of cleaning robots which cleans on maps with several rooms of varying sizes as fast as possible. In this final task you also have to create a team of dirt spreaders.

These tasks are performed in team of 1 to 3 students. You have to use Java 8 for these tasks (Java 7 won’t do).

Task 0 - Obtaining the project

The teacher has prepared a project in a git repository for each group. Inform the teacher to be assigned a group. Once assigned to a group, you can clone the project using git clone git@yousource.it.jyu.fi:ties454-2016-assignment3/group<nr>.git

The project contains two packages:

fi.jy.ties454.cleaningAgents, which is the basic platform. Normally you won’t change the code in that package.
fi.jyu.ties454.assignment3.group0, which contains basic skeletons for the first two tasks.
To start working, make a copy of the fi.jyu.ties454.assignment3.group0 package and change the group number to the one you received. All code of this assignment must be placed in that package. The first skeleton can be found in fi.jyu.ties454.assignment3.group<number>.task1, the second one in fi.jyu.ties454.assignment3.group<number>.task2, and so on.

Also make a copy of the resource folder /src/main/resources/fi/jyu/ties454/assignment3/group0 to /src/main/resources/fi/jyu/ties454/assignment3/group<groupnumber>.

Task 1 - Cleaning the rectangle

In this first task you have to write the code for a cleaning robot which has to clean a rectangular room. The skeleton for this task is already created. Carefully read the comments on the fi.jyu.ties454.assignment3.group<nr>.task1.MyCleaner class. To start the simulation, you need to execute the fi.jyu.ties454.assignment3.group<nr>.task1.Run class.

If the simulation runs to slow to your liking, read the hints below.

Task 2 - Advanced sensors and enemies

The actuators used up till now are a bit boring. The only things you could do were making one step, rotating, and cleaning. Obviously, cleaning gets more fun with better gear. Therefore, you can buy additional sensors and actuators for your agent. To obtain these, your cleaning agent needs to send a message to the Manager. If your team still has budget (the total budget is 12,000), the manager will install the part onto your agent. Since sending this kind of messages is common, it has been implemented in the getDevice() method. See the skeleton code for an example.

The following parts are available from the fi.jyu.ties454.cleaningAgents.infra.DefaultDevices class. See the javadoc of the DefaultDevices class for more information on how they work. Note that there are also parts for making the rooms dirty. Your cleaning agents do not want these!

part	cost	speed
AreaCleaner	exp	normal
AreaDirtier	middle	normal
BasicBackwardMover	cheap	slow
BasicCleaner	free	normal
BasicDirtier	free	slooow
BasicDirtSensor	free	fast
BasicForwardMover	free	slow
BasicRotator	free	normal
BasicWallSensor	middle	fast
DirtExplosion	middle	instant
FrogHopperForwardMover	exp	instant
HighProbabilisticDirtSensor	middle	faster
JackieChanRotator	middle	instant
JumpForwardMover	middle	fast
LaserDirtSensor	expensive	fast/instant depending on function
LeftMover	middle	fast
LowProbabilisticDirtSensor	cheap	faster
Where free = 0, cheap = 500, middle = 1,500, and exp = 3,000. For speeds (at default timeFactor of 50, see below), slooow = 2.5s, slow = 1.25s, normal = 0.5s, fast = 0.25s, faster = 0.05s, instant = 0s.

Your task is to create a cleaning agent which attempts to clean the dirt which the MyDirtier agent creates. The agent you create can use all available sensors and actuators (but mind the budget of 12,000). Once you are happy with your agent, you can let the simulation run for 150 seconds (2.5 minutes). After that the simulation will stop and inform what the average amount of dirt in the rooms was. Your goal is to get that number down. (Don’t spent too much time on this, there is no point trying to find the perfect solution.)

Task 3 - Coordinating multiple cleaners

When multiple cleaners are started, they all start on the same location (still, this same location will be different each time you run). Multiple dirtiers, however, start each on a different location.

For the third task you have to create three cleaners which together clean the area (while two provided agents are making it dirty again). The point of this task is to make the agents work together and use each other’s capabilities. For example:

You do not want to clean the same area multiple times in a short time.
You only want to clean if it is likely that there is dirt in a location.
Moreover, you do not have the budget to give each of these agents a good dirt sensor, moving ability, cleaning device, etc.
While cleaning, you cannot be moving, so why have a fast moving robot cleaning.
If you want to map the environment it only has to be done once (assuming unchanging room shapes).
etc.
Concrete, you need to implement the system such that there is one leader agent which collects the information from the others and spreads it back. This could be done by something like the publish subscribe system which was created in the first assignment. For this assignment you are allowed to use the void setContentObject(Object) method of the ACLMessage. This saves a fair bit of work encoding and decoding. There are several pieces of information which could be send around: valid areas, dirty areas, cleaned areas, … To keep the map of the area in the agent you can consider the com.google.common.collect.Table type in the guava library.

Since all agents start at the same position, you can keep track of their relative locations. A class called Tracker has been prepared which can be used for tracking the location of the robot relative to the starting point. An example of its usage can be found in the fi.jyu.ties454.cleaningAgents.example.cleaning.PoorCleaner class. Note that the tracker class uses a coordinate system which is typical for computer graphics programming.

Orientation of the axis of the tracker.

As a map, you could use the rectangleRoomLargeClean.txt or create your own more complicated map.

Task 4 - Multiple rooms - competition

Up till now there has only been one room to clean. This tasks adds the complexity that there can be multiple rooms. To make things easier, you only have to consider maps with rectangular rooms (always 4 corners), there are no doors at the corner points, and doors always consist of one cell.

You have two sub-tasks:

Create a team of 3 agents able to clean multiple rooms. The agents should be generic, i.e., able to clean any kind of environment.
Create a team of 2 agents which make rooms dirty.
During the final session we will let the agents of different teams fight with each other and find out who has the best cleaners and the best soilers.

Don’ts of the game:

Don’t use reflection to change values you won’t have access to otherwise
Don’t start extra threads
Don’t attempt denial-of-service type attacks
Don’t send messages on other agents’ behalf
If you think you are cheating, you probably are
Floor file format

The file format for the floor maps is as follows: The map consists of cells and each cell is represented by one character.

C indicates a clean cell
# indicates a dirty cell
‘ ’ (a space) indicates an empty cell, i.e. where there is no floor.
Walls are created by inserting spaces between areas consisting of other characters. Doors are creating by a normal cell. Hence, the following is an example of three rooms - two clean ones and a dirty one - connected by doors:

CC CCCCC ####
CCCCCCCCC####
CC CCCCC ####
CC
Returning the assignment

The deadline for this assignment is February 29. During session on March 2 you will get feedback on your work. Then there is still about a week of time to get things in shape for the final session March 9.

The teacher created a repository for each group in yousource and added the starter code to it. When you inform your group members to the teacher you will be added as a collaborator.

The repository can be cloned from git@yousource.it.jyu.fi:ties454-2016-assignment3/group<nr>.git (replace <nr> with your group number).

In the repository you place

The maven project (code + resources (maps) + pom.xml file)
A readme file with
The names of your groups’ members
Extra information if you think it is needed
Hints

The agent platform is a normal Java program. Debuggers can be used normally. It is also useful to combine the debugger and the sniffer in the JADE GUI: one can stop the agents, then start the sniffer and watch all messages being sent.

The class fi.jyu.ties454.cleaningAgents.infra.DefaultDevices has a constant called timeFactor (near the bottom where many constants are defined). Changing this constant (lower) speeds up or (higher) slows down the simulation. Putting this number too low (under 10) makes results unreliable. Feel free to change this while programming. The original value is 50.

Robots which are programmed using lots of randomness tend to stay in the same room for a fair amount of time because they will rarely find the door.

You can copy the map in a certain state from the GUI and paste it into a text file. This file can then be loaded as the state next time.
http://users.jyu.fi/~miselico/teaching/TIES454/exercises/exercise3/
