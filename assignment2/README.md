 English auction 
 
 
 =====
 Assignment 2: Platform Infrastructure and Auctions
Goal

After completing these exercises the student should have a basic understanding services provided by a multi-agent platform. Further, he or she will be able to implement basic auction mechanisms. Concepts covered are directory facilitation, agent mobility, agent cloning, connecting multiple containers, and auctions.

Prerequisites

Attendance to the first six lectures by Vagan is recommended. Otherwise, self-study of the materials is necessary. Most of the concepts used are dealt with in the theory part of the course.

You must have completed the first assignment. The beginners guide to JADE contains a section about the use of the directory facilitator.

You must have knowledge of programming in Java or be prepared to work on your skills during the course.

Tasks

The assignment consists of three parts, which are not strictly connected (if you get stuck with one, you can start the next). In the first task you will interact with the Jade Directory Facilitator (DF) and learn how you can discover other agents on the platform. Then, in the second task, you will have to connect multiple containers together and find out about moving agents between them. Finally, you need to implement an auction scenario to get an impression of how resources can be shared among agents.

These tasks are performed individually. You have to use Java 7 or 8 for these tasks.

Task 0 - Creating the project

If you did the first assignment, you can just continue working in the same project/repository. Otherwise, check the previous task for a description on how to create the project.

For this task, make sure you create the package fi.jyu.ties454.<username>.assignment2, where <username> must be replaced with your korppi username. All code of this assignment must be placed in that package. The first task must be placed in fi.jyu.ties454.<username>.assignment2.task1, the second one in fi.jyu.ties454.<username>.assignment2.task2, and so on.

Task 1 - Directory Facilitation

For this task you have to create 3 agents. Agent1 and Agent2 are service providers. Agent3 is a service consumer.

Agent1 provides weather information. Specifically, another agent can ask it for the outside temperature for a given city.
The agent actually gets its information from openweathermap.org. You can either create your own account and get an APPID or ask the teacher.
Agent2 provides clothing advise. Given a temperature, it will advise you on what to wear.
Use your own inspiration, three different options depending on the temperature.
Agent3 is in Jyväskylä and would like to know what to wear at the moment.
If Agent3 would know about the fact that Agent1 and Agent2 provide these services, this would be an easy task. However, for this task you have to assume Agent3 does not know about them. Therefore, it needs to discover them using the directory facilitator.

Three things must be done:

Agent1 and Agent2 must register themselves to the DF, indicating they provide the weatherInfo and clothingAdvise services, respectively.
Agent3 must search for agents providing weatherInfo and clothingAdvise services from the DF. Implement the search for service providers in a behavior which blocks for 1 second in case services are not found yet.
Once the agents providing these services are found, Agent3 interacts with them to obtain the required information.
All communication between these three agents must be done using a self defined protocol. The correct performative to use when asking information is query-ref. When answering inform should be sued. You can optionally perform the call to openweathermap.org in a separate thread.

Task 2 - Multiple containers - mobility and cloning

In this second task you will work with more advanced features of JADE which are also avaialble on most other agent platforms. First, you will connect two agent containers together. In JADE, an agent container is a one JVM process which accommodates a number of agents. The agent platform is a collection of containers which are connected to each other. One platform has to be chosen as the main container and all other containers have to connect to that one. Up till now we have only used a single container, which was configured as a main container.

Now, you need to start a second container which will connect to the main one. To do this, create a class called Run2 which has the following main method:

public static void main(String[] args) {
    Properties pp = new Properties();
    pp.setProperty(Profile.MAIN, Boolean.FALSE.toString());
    pp.setProperty(Profile.MAIN_HOST, "127.0.0.1");
    pp.setProperty(Profile.MAIN_PORT, Integer.toString(1099));
    Profile p = new ProfileImpl(pp);
    AgentContainer ac = jade.core.Runtime.instance().createAgentContainer(p);
    try {
        ac.acceptNewAgent("JumpingAgent", new Agent()).start();
    } catch (StaleProxyException e) {
        throw new Error(e);
    }
}
This is very similar to the main method of the Run classes we have been using in earlier exercises (and which we now also need to start the main container). The changes are some properties indicating how this container must connect to the main container and a different call to the JADE runtime. It is possible to run each container on a different computer. However, this will only work in case the firewall allows traffic to go trough on the specified port. Try to run both containers and check in the GUI that both containers are visible. Note also that only the main container has an ams and df agent.

When working with multiple containers it makes a difference whether the agent is on the one or the other platform, in particular when the agent needs to access local resources (like the file system, sensors, actuators, console, CPU power, … ). Therefore, it is possible for an agent to migrate from one container to another. In JADE the moving process requires that the agent and all classes used in the agent are Serializable.

There is one difficulty with moving agents and that is specifying where the agent should be going to. The doMove() method of the agent requires a Location object and obtaining one of these is unnecessary hard. The code to obtain the list of all locations is given below:

private ArrayList<Location> getLocations() {
    // adapted from :
    // http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility/ControllerAgent.java
    getContentManager().registerLanguage(new SLCodec());
    getContentManager().registerOntology(MobilityOntology.getInstance());

    // Get available locations with AMS
    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
    request.setLanguage(new SLCodec().getName());
    request.setOntology(MobilityOntology.getInstance().getName());
    Action action = new Action(getAMS(), new QueryPlatformLocationsAction());
    try {
        getContentManager().fillContent(request, action);
    } catch (CodecException | OntologyException e) {
        throw new Error(e);
    }
    request.addReceiver(action.getActor());
    send(request);

    // Receive response from AMS
    MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    ACLMessage resp = blockingReceive(mt);
    ContentElement ce;
    try {
        ce = getContentManager().extractContent(resp);
    } catch (CodecException | OntologyException e) {
        throw new Error(e);
    }
    Result result = (Result) ce;
    jade.util.leap.Iterator it = result.getItems().iterator();
    ArrayList<Location> locations = new ArrayList<Location>();
    while (it.hasNext()) {
        Location loc = (Location) it.next();
        locations.add(loc);
    }
    return locations;
}
Your task is now to create an agent (class name JumpingAgent) which moves from the one container to the other every five seconds (CyclicBehavior).

The agent needs to get the list of locations only once.
Start the JumpingAgent in the non-main container (to make sure the ‘other’ container exists when getting the list).
Remember at which location the agent was (so that you can jump to the other one).
Initially you don’t know whether you should jump to the first or second location in the list. Just start by moving to the first one, even if that means you stay in the same container.
Task 3 - Auctions

Often the resources on the platform are limited and the agents should agree which ones are going to be able to use the resource. Resources can include things like sensors and actuators but also CPU time, etc. In order to assign these resources to agents auctions are commonly used. The idea is that if an agent wants to use a resource, he should win it in an auction. Agents then pay for the resources using some form of virtual money or credit. A broad discussion and mathematical analysis of different auction types can be found from Multiagent Systems - Algorithmic, Game-Theoretic, and Logical Foundations. This exercise description is inspired by that book.

Many different types of auctions exist, and some auction types have multiple names. Three common types of open-outcry auctions are:

English auctions : the auctioneer sets a starting bid (can be 0) and agents interested in the resource can place a higher bid. The price increases until no other agents place a higher bid. The agent with the highest bid becomes the winner of the auction i.e. he receives the good and pays the last price he offered.
Japanese auctions : somewhat similar to English. A starting price is set and agents interested should announce whether they are still interested, agents leaving the auction cannot reenter. The price is increased steadily until only one agent is interested, who pays the last announced price.
Dutch auction : The starting price is set (too) high. Then it is lowered each time until an agent shows interest. The resource is then sold for that price.
Alternatively, an auction can be done by sealed-bid, i.e. the offers are not publicly announced. Typically, the winner of the auction is the one who offered the highest amount. He will either pay the amount he offered or the amount which was offered by the second highest bidder (these are called first-price and second-price auctions).

Your task is to implement a scenario in which 20 agents are competing for access to a sensor using one of the above open-outcry auction types. In the scenario there are the following agents:

20 Bidders
Each bidder initially generates a random number between 500 and 1000 which is the maximum amount he is prepared to pay.
Each bidder registers to the auctioneer to inform that it is interested in the auction.
When the auctioneer announces a new price, the bidder either
Offers more/confirms that he received the information (English) <– more difficult option: The current highest bidder should not offer more, i.e. the highest bidder should know he is currently the highest bidder.
Informs whether he is still in/now out (Japanese)
Confirms that he buys/received the information (Dutch)
1 Auctioneer
The auctioneer manages the auction
Waits until 20 bidders have registered and then starts the auction (initial price 0 for English, Japanese. 1500 for Dutch)
Repeat until sold:
Announce new price (increment/decrement with 1)
Wait for all answers to come
Check whether sold
Sends the result (winner + final price) of the auction to all 20 agents.
Note that all agents need a unique name. In case of a tie, the final winner is selected at random among the winners.

Start the auctioneer before the bidders to make sure the bidders messages will find the auctioneer.

Returning the assignment

The deadline for this assignment is February 15.

The teacher has created a repository for the first assignment in yousource and added you as a collaborator. We will keep on using this repository for this second assignment. The repository can be cloned from git@yousource.it.jyu.fi:ties454-2016-assignment1/<username>.git (again replace <username> with your korppi username). Keep in mind that before you can work with yoursource you have to create a keypair (see http://yousource.it.jyu.fi/yousource/pages/SSH_key_help ). A very short guide to git can be found from http://yousource.it.jyu.fi/yousource/pages/Git_Help.

In the repository you place

The maven project (only code + pom.xml file)
A readme file with
your name
Extra information if you think it is needed
Hints

The agent platform is a normal Java program. Debuggers can be used normally. It is also useful to combine the debugger and the sniffer in the JADE GUI: one can stop the agents, then start the sniffer and watch all messages being sent.
