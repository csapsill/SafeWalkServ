package com.devsil.safewalk.server;

import edu.purdue.cs.cs180.channel.*;

public class Matcher extends Thread{
	

	private long sleepTime;

	private String matchType;

	private DataFeeder feeder;

	private Channel channel;

	

	public Matcher(DataFeeder f,long sleep,String matchType, Channel channel){

		this.sleepTime = sleep;

		this.matchType = matchType;

		this.channel = channel;

		this.feeder = f;

	}

	

	/**

	 * Sends the message to the different clients

	 */

	private void messageSender(String info, int to){

		try{

			channel.sendMessage(info,to);

		}catch(ChannelException e){

			e.printStackTrace();

			System.exit(1);

		}

	}	

	/**

	 * This is what is called when this thread is started.

	 * 

	 * While both lists have a pending request it will go through and send the appropriate 

	 * information to each client. 

	 */

	@Override

	public void run(){

			while(feeder.hasNextRequest() & feeder.hasNextResponse()){

				if (matchType.equalsIgnoreCase("fcfs")) {

					Message request = feeder.getFirstRequest();

					Message response = feeder.getFirstResponse();

					

					// String to be sent to the requester

				    String requesterMessage = new Message(Message.Type.Assigned,response.getInfo(),

				    		response.getClientID()).toString();

				    int requestClientID = request.getClientID();

				    messageSender(requesterMessage, requestClientID);

				    

				    

				    // string to be sent to the responder

				    String responderMessage = new Message(Message.Type.Assigned,request.getInfo(),

				    		request.getClientID()).toString();

				    int responseClientID = response.getClientID();

				    messageSender(responderMessage,responseClientID);

				    

				    

				    //remove both messages from queue

				    feeder.removeFirstRequest();

				    feeder.removeFirstResponse();

				    

				}

				else { // This is the Most Recent Matching Type

					Message request = feeder.getLastRequest();

					Message response = feeder.getLastResponse();

					

					//String to send to requester

					String requesterMessage = new Message(Message.Type.Assigned,response.getInfo(),

							response.getClientID()).toString();

					int requestClientID  = request.getClientID();

					messageSender(requesterMessage,requestClientID);

					

					

					//String to the responder

					String responeMessage = new Message(Message.Type.Assigned,request.getInfo(),

							request.getClientID()).toString();

					int responseClientID = response.getClientID();

					messageSender(responeMessage,responseClientID);			

	

					//remove from the list

					feeder.removeLastRequest();

					feeder.removeLastResponse();

				}

			}

			// If either list doesn't have anyone in it, let this server sleep

			//for specified time.

			try {

				Thread.sleep(sleepTime);

				run();

			} catch (InterruptedException e) {

				e.printStackTrace();

				System.exit(1);

			}

	}

}
