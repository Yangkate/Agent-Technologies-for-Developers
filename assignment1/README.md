README
=======
Assignment 1: First Steps Using a Multi-agent System
Goal

After completing these exercises the student should have a basic understanding of how to start developing on a multi-agent platform (in particular JADE). Concepts covered are agents, basic behaviors, and messaging.

Prerequisites

Attendance to the first four lectures by Vagan is recommended. Otherwise, self-study of the materials is necessary. Most of the concepts used are dealt with in the theory part of the course.

A beginners guide to JADE : http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf

If you are interested in the standards used for agent communication, you could attempt to read trough the FIPA ACL Message Structure Specification and the FIPA Communicative Act Library Specification. This is for the moment not strictly necessary.

You must have knowledge of programming in Java or be prepared to work on your skills during the course.

Tasks

The assignment consists of five tasks, which build upon each other. First, you will create a very simple scenario with two agents. Then, these two agents will have to communicate. Next, the functionality of the agents needs to be extended using Behaviors. Next, you need to use message filters to receive specific messages only. Finally, you need to implement a basic messaging pattern.

These tasks are performed individually. You have to use Java 7 or 8 for these tasks.

Task 1 - Hello Agent World

To start the first task, you need to create a maven project. Use fi.jyu.ties454 as the Group Id, <username>.assignment1 as the Artifact Id, and Version 0.0.1-SNAPSHOT.

After the project is created include the following parts in the pom.xml file:

<dependencies>
<!-- The agent platform -->
    <dependency>
        <groupId>com.tilab.jade</groupId>
        <artifactId>jade</artifactId>
        <version>4.4.0</version>
    </dependency>
</dependencies>
<!-- repository hosting the agent platform -->
<repositories>
    <repository>
        <id>tilab</id>
        <url>http://jade.tilab.com/maven/</url>
    </repository>
</repositories>
<!-- set compiler to Java 8 -->
<build>
    <plugins>
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.3</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
In an IDE like Eclipse you might have to update the project (right click project>Maven>Update Project) in order for these changes to take effect.

Now, make sure the package fi.jyu.ties454.<username>.assignment1 exists, where <username> must be replaced with your korppi username. All code of this assignment must be placed in that package. The first task must be placed in fi.jyu.ties454.<username>.assignment1.task1, the second one in fi.jyu.ties454.<username>.assignment1.task2, and so on. If your project contains some default code (App.java, AppTest.java), remove it.

It is useful also download the JADE source code. This way you also get the javadoc in your IDE. Unfortunately the maven repository does not include the source code itself. Therefore, you will have to download the source code from http://jade.tilab.com/download/jade/license/jade-download/ manually and add attach it (browse to one of the compiled JADE classes in Eclipse and click ‘attach source’).

For this first task you have to create two classes (HelloAgent and HiAgent) which extend from the class jade.core.Agent. These classes can now be started as agents using the following code: Create a new class called Run and add the following main method:

public static void main(String[] args) {
    Properties pp = new Properties();
    pp.setProperty(Profile.GUI, Boolean.TRUE.toString());
    Profile p = new ProfileImpl(pp);
    AgentContainer ac = jade.core.Runtime.instance().createMainContainer(p);
    try {
        ac.acceptNewAgent("Hello", new HelloAgent()).start();
        ac.acceptNewAgent("Hi", new HelloAgent()).start();
    } catch (StaleProxyException e) {
        throw new Error(e);
    }
}
Running this main method will start the agent system containing the two agents. Also a GUI will show up which you can use to debug things. Play around with the GUI to see what it can do. Also try to find your agents in the GUI and note that they are active (they are running even though they are not currently doing anything).

At the moment your agents don’t do anything yet. To make the do something you need to override the void setup() method. For the HelloAgent print “Hello agent world” in that method. For the HiAgent print “Hi agent world”.

Task 2 - Hello other agent

The main power of a multi-agent system lies in the fact that it is able to collaborate with other agents. In JADE this is facilitated by messaging. Messaging in JADE is point-to-point, i.e., a message is sent from a specified sender to a number of specified receivers.

In this task you will send a message from the HiAgent to the HelloAgent and reply to it. First, you need to copy the package fi.jyu.ties454.<username>.assignment1.task1 to fi.jyu.ties454.<username>.assignment1.task2 to continue working on the code. Then, you need to write code to

send a message from Hi to Hello (in HiAgent)
receive the message from Hi (in HelloAgent)
send a response to the message (in HelloAgent)
receive the response from Hello (in HiAgent)
To send a message in Jade you have to create an instance of the ACLMessage class. Create one of these messages as follows new ACLMessage(ACLMessage.INFORM);. The performative (ACLMessage.INFORM) is one of the standard FIPA performatives. Set the content of the message to Hello or Hi and the language to English.

To receive a message, you need to use one of the receive/blockingReceive methods of the agent. Check their Javadoc to decide which one is suitable.

All code of this task goes into the setup() method of the agent.

Task 3 - Behaviors

Writing the agent’s code in the setup method method is somewhat limited. All actions the agent takes must be written in a sequential fashion. JADE provides a different way to organize the agent’s functionality using so-called Behaviors. The behaviors of an agent are pieces of functionality which will be executed by the agent in a round-robin fashion.

Behaviors are non-preemptive, so you are expected to write your behaviors in a collaborative fashion. The way they work can be seen as a horizontal layering.

Implementation-wise, a behavior is a Java class which derives from the jade.core.behaviours.Behaviour class. The behavior has the methods action() in which the actual functionality gets implemented and done() which indicates whether the Behavior should be rescheduled. (If done() returns true the behavior will not be rescheduled.) Frequently one wants behaviors which only runs once or which run forever. To cater these use-cases JADE provides the OneShotBehavior and CyclicBehavior classes, respectively. One more possible use-case is a task which should repeat every so often (e.g. every 30 seconds). The TickerBehavior is exactly suitable for that scenario. Finally, there is WakerBehavior which gets executed once after a certain amount of time has elapsed. Behaviors can be added to an agent at any time. Typically you will add some initial behaviors in the setup() method and add more later as needed,often from inside other behaviors. The method on the Agent class to add Behaviors is called addBehavior(Behavior b).

For this task you need to make yet another copy of the package of the previous task and call it fi.jyu.ties454.<username>.assignment1.task3. The functionality needs to be as follows:

The HelloAgent sends its favorite message (“Hello”) to the HiAgent every 5 seconds
The HiAgent receives these messages and answers them saying “Hi”
The HelloAgent receives these responses and prints them to the console
Task 4 - Message filtering

Often an agent will be waiting for multiple messages at the same time. A piece of code used to handle specific messages only wants to receive and process these and not other types. One way to solve this problem is to receive a message and then put it back to the agent’s message queue if it was not the message you expected. A better way to do this is by using MessageTemplates. These templates can, for instance, be created as follows:

MessageTemplate t = MessageTemplate.MatchInReplyTo("8bed3315799895f6f55b");
Then, the template is used in one of the receive calls of the agent.

Again make a fresh copy of the package of the previous task. Now, change the functionality as follows:

The HelloAgent sends “Hello for the x-th time” message to the HiAgent every 5 seconds
x is an integer which is incremented each time a message is sent.
The message also has the ReplyWith property set to x
Once the message is sent, the HelloAgent starts a Behavior (derive directly from the Behavior class) which only listens for an answer to this specific message.
The following code should be used in the Behavior to listen for messages. The block call ensures that this behavior will not consume all CPU time and that this behavior gets woken up when new messages arrive.

  public void action() {
      ACLMessage msg = myAgent.receive( some template here );
      if (msg != null) {
        // Message received. Process it
      } else {
          block();
      } 
  }
When the message is received the behavior prints the content and the value of x to the console.

After the message has been printed, the done() method must return true such that this Behavior won’t be scheduled any longer.
The HiAgent receives these messages and answers them saying Hi after waiting for ten seconds (use WakerBehavior to achieve this).
If you use message.createReply() the InReplyTo property will be set to whatever was in the ReplyWith property (in this case the value of x)
Task 5 - Complex behaviors and messaging patterns

Behaviors can be combined in all sorts of ways. JADE provides some classes which aim to help this composition. However, often it is easier to combine the four classes mentioned above to compose complex functionality, or to derive from Behavior directly.

Similarly to design patterns in programming there are also messaging patters in MAS. This means that for certain engineering tasks certain messaging patterns can be re-used. One such pattern is commonly known as publish-subscribe. Your task is to implement that pattern.

In the scenario there are the following agents:

25 Publishers
Each publisher sends (publishes) a message to the broker every second.
The message has two parts: a topic and the content.
The topic is one of the following strings: DTIME, P, EXPTIME, NTIME, NP, NEXPTIME, DSPACE, L, PSPACE, EXPSPACE, NSPACE, NL, NPSPACE, or NEXPSPACE at random.
The content is a just a random number.
You need to encode these two pieces of information into the content of the message.
50 Subscribers
Each subscriber is subscribed to 1 of the topics from the list above at random.
The subscribers receive updates (use CyclicBehaviour) and print the latest value received every 10 seconds (use TickerBehavior - note that you will need a bit of shared state - between the behaviours - in order to do this).
1 Broker
The broker manages the publishers and subscribers
Uses performatives to distinguish between subscribe requests and publish actions.
Makes sure that each agent subscribed to a given topic receives the updates about that topic.
Note: The behaviors of an agent are executed in the same thread. Hence, you do not have to think about simultaneous access to state. However, this also means that you will have to make sure that all behaviors get a chance to run (e.g. don’t use blocking receive calls since they cause the whole agent to be blocked.).
Note that each agent needs a unique name. Further, it is likely that when you start the platform not all subscribers will immediately get the messages about their topics. This can happen because the subscription only gets processed after the publisher already sent the first updates. You do not have to solve this problem.

In this last tasks you might run into an issue where the Broker agent is not reachable from the very beginning. This happens when you start the publishers and subscribers first and they start sending messages before the broker is started. In this case the publisher or subscriber will receive a message from the AMS that the broker could not be found. To solve this problem, make sure the broker is started before starting any publishers and subscribers.

Returning the assignment

The deadline for this assignment is February 1.

The teacher has created a repository in yousource and added you as a collaborator. The repository can be cloned from git@yousource.it.jyu.fi:ties454-2016-assignment1/<username>.git (again replace <username> with your korppi username). Keep in mind that before you can work with yoursource you have to create a keypair (see http://yousource.it.jyu.fi/yousource/pages/SSH_key_help ). A very short guide to git can be found from http://yousource.it.jyu.fi/yousource/pages/Git_Help.

In the repository you place

The maven project (only code + pom.xml file)
A readme file with
your name
Extra information if you think it is needed
Hints

The agent platform is a normal Java program. Debuggers can be used normally. It is also useful to combine the debugger and the sniffer in the JADE GUI: one can stop the agents, then start the sniffer and watch all messages being sent.
