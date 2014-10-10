package com.devsil.safewalk.server;

import java.util.Vector;

public class ListManager implements DataFeeder{
	
	



	/**

	 * Maintains the pending requests. 

	 * This is a synchronized data structure for use in multiple threads

	 */

	public static Vector<Message> pendingRequesters = new Vector<Message>();

	/**

	 * Maintains the pending responses.

	 * This is a synchronized data structure for use in multiple threads

	 */

	public static Vector<Message> pendingResponders = new Vector<Message>();



	



	@Override

	public Message getFirstRequest() {

		Message firstRequest = pendingRequesters.get(0);

		return firstRequest;

	}



	@Override

	public Message getFirstResponse() {

		Message firstResponse = pendingResponders.get(0);

		return firstResponse;

	}



	@Override

	public Message getLastRequest() {

		Message lastRequest = pendingRequesters.lastElement();

		return lastRequest;

	}



	@Override

	public Message getLastResponse() {

		Message lastResponse = pendingResponders.lastElement();

		return lastResponse;

	}



	@Override

	public boolean hasNextRequest() {

		if(pendingRequesters.isEmpty()){

			return false;

		}

		else{

			return true;

		}

	}



	@Override

	public boolean hasNextResponse() {

		if(pendingResponders.isEmpty()) {

			return false;

		}

		else {

			return true;

		}

	}



	@Override

	public void removeFirstRequest() {

		pendingRequesters.remove(0);		

	}



	@Override

	public void removeFirstResponse() {

		pendingResponders.remove(0);	

	}



	@Override

	public void removeLastRequest() {

		int lastRequestIndex = pendingRequesters.size() -1;

		pendingRequesters.remove(lastRequestIndex);

	}



	@Override

	public void removeLastResponse() {

		int lastResponseIndex = pendingResponders.size() -1;

		pendingResponders.remove(lastResponseIndex);

	}



}
