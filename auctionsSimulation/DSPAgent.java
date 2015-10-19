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

import madkit.kernel.ConversationID;
import madkit.kernel.AbstractAgent;
import madkit.message.StringMessage;


public class DSPAgent extends AbstractAgent {

	/** Agent's environment used to know its boundaries, automatically set **/
	private EnvironmentAgent environment;
	/** Local history of bidding as a tuple (conversation, bid) **/
	private HashMap<ConversationID, Integer> bidHistory = new HashMap<ConversationID, Integer>();
	/** Granted budget of the agent used for future bidding **/
	private Integer budget;


	/**
	 * initialize my role and fields
	 */
	@Override
	protected void activate() {
		budget = 1000;
		requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE);
		environment.getDimension();
	}

	/**
	 *  Bid response
	 */
	@SuppressWarnings("unused")
	private void manageAuction() {
		StringMessage m = (StringMessage) nextMessage();
		// while message in our mailbox
		while(m != null) {
			String[] msgArray = m.getContent().split(" ");

			// switching according to first part of message content
			switch (msgArray[0]) {

			// we have a proposition we answer
			case "askForBid":
				int bid = Math.min(budget, (int)(Math.random()*100));
				bidHistory.put(m.getConversationID(), bid);
				System.out.println(this.getName() + "(" + this.budget + ") => " + bid);
				sendReply(m, new StringMessage("answerBid " + bid));
				break;

				// we won the competition!
			case "winBid":
				budget -= Integer.parseInt(msgArray[1]);
				break;
			}

			m = (StringMessage) nextMessage();
		} 	
	}
}
