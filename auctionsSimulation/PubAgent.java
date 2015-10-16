/*
 * Copyright 2011-2012 Fabien Michel
 * 
 * This file is part of MaDKit-tutorials.
 * 
 * MaDKit-tutorials is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MaDKit-tutorials is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MaDKit-tutorials. If not, see <http://www.gnu.org/licenses/>.
 */
package auctionsSimulation;

import java.util.HashMap;

import madkit.kernel.AbstractAgent;
import madkit.kernel.AgentAddress;
import madkit.kernel.ConversationID;
import madkit.message.StringMessage;


@SuppressWarnings("serial")
public class PubAgent extends AbstractAgent {

	private HashMap<ConversationID, Integer> auctionValues = new  HashMap<ConversationID, Integer>();
	private HashMap<ConversationID, AgentAddress> auctionAgents = new  HashMap<ConversationID, AgentAddress>();
	private HashMap<ConversationID, Integer> auctionCount = new  HashMap<ConversationID, Integer>();

	/**
	 * initialize my role and fields
	 */
	@Override
	protected void activate() {
		requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.PUB_ROLE);
	}

	/**
	 * A non sense behavior, just moving around.
	 */
	@SuppressWarnings("unused")
	private void manageAds() {
		StringMessage m = (StringMessage) nextMessage();
		int dspSize = getAgentsWithRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE).size();
		// while message in our mailbox
		while(m != null) {
			System.out.println("DSP"+m.getSender()+"->PA"+m.getReceiver()+": "+m.getContent());
			String[] msgArray = m.getContent().split(" ");

			// switching according to first part of message content
			switch (msgArray[0]) {

			// we got an answer
			case "answerBid":
				// if key does not already exist
				if (!auctionValues.containsKey(m.getConversationID())) {
					auctionCount.put(m.getConversationID(), 1);
					auctionValues.put(m.getConversationID(), Integer.parseInt(msgArray[1]));
					auctionAgents.put(m.getConversationID(), m.getSender());
				}
				// otherwise we update best values
				else {
					auctionCount.put(m.getConversationID(), auctionCount.get(m.getConversationID())+1);
					int bestValue = auctionValues.get(m.getConversationID());
					if (bestValue < Integer.parseInt(msgArray[1])) {
						auctionValues.put(m.getConversationID(), Integer.parseInt(msgArray[1]));
						auctionAgents.put(m.getConversationID(), m.getSender());
					}
				}
				// if number of bids equals number of DSP agents we inform winner
				if (auctionCount.get(m.getConversationID()) == dspSize) {
					System.out.println("sending win message to " + auctionAgents.get(m.getConversationID()));
					sendMessage(auctionAgents.get(m.getConversationID()), new StringMessage("winBid " + auctionValues.get(m.getConversationID())));
				}
				break;
			}
			m = (StringMessage) nextMessage();
		}

		// we may send another request for a new bid
		if (((int) (Math.random()*100)) == 0) {
			broadcastMessage(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE, new StringMessage("askForBid"));
			System.out.println("new ask from PubAgent");
		}
	}
}