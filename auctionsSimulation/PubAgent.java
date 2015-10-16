/*
 * Copyright 2015 Eric Bourreau & Fabien Hervouet
 * 
 * This file is part of SimAuction.
 * 
 * SimAuction is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SimAuction is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SimAuction. If not, see <http://www.gnu.org/licenses/>.
 */
package auctionsSimulation;

import java.util.HashMap;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ConversationID;
import madkit.message.StringMessage;


public class PubAgent extends AbstractAgent {

	/** List of bidding and related information (best bid value, best bidder) **/
	private HashMap<ConversationID, Bidding> auctions = new  HashMap<ConversationID, Bidding>();

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
			//System.out.println("DSP"+m.getSender()+"->PA"+m.getReceiver()+": "+m.getContent());
			Bidding auction = auctions.get(m.getConversationID());
			String[] msgArray = m.getContent().split(" ");

			// switching according to first part of message content
			switch (msgArray[0]) {

			// we got an answer
			case "answerBid":
				// if key does not already exist
				if (!auctions.containsKey(m.getConversationID())) {
					auctions.put(m.getConversationID(), new Bidding(Integer.parseInt(msgArray[1]), m.getSender()));
				}
				// otherwise we update best values
				else {
					int bestValue = auctions.get(m.getConversationID()).getBidValue();
					int newValue = Integer.parseInt(msgArray[1]);
					if (bestValue < newValue) {
						auctions.get(m.getConversationID()).set(newValue, m.getSender());
					}
					else {
						auctions.get(m.getConversationID()).incrementBidsCount();
					}
				}
				// if number of bids equals number of DSP agents we inform winner
				if (auctions.get(m.getConversationID()).getBidsCount() == dspSize) {
					sendMessage(auctions.get(m.getConversationID()).getBidder(), new StringMessage("winBid " + auctions.get(m.getConversationID()).getBidValue()));
				}
				break;
			}
			m = (StringMessage) nextMessage();
		}

		// we may send another request for a new bid
		if (((int) (Math.random()*100)) == 0) {
			broadcastMessage(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE, new StringMessage("askForBid"));
			System.out.println("\n/!\\ New offer from PubAgent /!\\");
		}
	}
}