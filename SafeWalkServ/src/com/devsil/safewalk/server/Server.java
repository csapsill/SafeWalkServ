package com.devsil.safewalk.server;

import edu.purdue.cs.cs180.channel.*;


/**
 * The server class, a console application, that receives messages from Requests
 * and Responses.
 * 
 * The protocol as follow: <tt>When it receives a
 * Request: 
 * 	Adds the Request Message to the queue for Request Type Messages. 
 * 
 * Similarly, when it receives a Response message: 
 * 	Adds the Response Message to the Response queue. 
 *   
 *   The Matching of the message senders is done through the Matcher class
 * </tt>
 * 
 * @author sill
 * 
 */

public class Server implements MessageListener{
	
	/**
	 * The server channel.
	 */
	private static Channel channel = null;
	private static long sleepTime;
	private static String matchingType;

	/**
	 * The server constructor needs a port, matching type, and a sleep time(in milliseconds).
	 * 
	 * @param port
	 * @param matchType
	 * @param sleep
	 */
	public Server(int port,String matchType,int sleep) {
		channel = new TCPChannel(port);
		channel.setMessageListener(this);
		Server.matchingType = matchType;
		Server.sleepTime = sleep;
	}
	
	// sends searching to the client after it has received a message from it. 
	public void sendSearching(String message, int to){
		try{
			channel.sendMessage(message,to);
		}catch (ChannelException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	 * Handle messages received.
	 * Takes the message received from the client and add its to the appropriate 
	 * Vector, Then sends "searching" to the appropriate sender
	 */
	@Override
	public void messageReceived(String messageString, int clientID) {
		Message message = new Message(messageString, clientID);
		switch (message.getType()) {
		case Request:
			ListManager.pendingRequesters.add(message);
			sendSearching((Message.Type.Searching).toString(),message.getClientID());
			break;
		case Response:
			ListManager.pendingResponders.add(message);
			sendSearching((Message.Type.Searching).toString(), message.getClientID());
			break;
		default:
			System.err.println("Unexpected message of type "
					+ message.getType());
			break;
		}
	}
	

	/**
	 * The server expects a port number as an argument.
	 * 
	 * @param sleep time
	 * @param matching type
	 * @param args Expects a port number.
	 * @param  
	 */
	public static void main(String[] args) {	
		DataFeeder f = new ListManager();
		new Server(Integer.parseInt(args[0]),args[1],Integer.parseInt(args[2]));
		new Matcher(f, sleepTime, matchingType, channel).start();
	}

}
