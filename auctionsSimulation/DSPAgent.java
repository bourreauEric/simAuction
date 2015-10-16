/*
 * Copyright 2011-2012 Fabien & Eric
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

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.HashMap;

import madkit.kernel.ConversationID;
import madkit.kernel.AbstractAgent;
import madkit.message.StringMessage;


@SuppressWarnings("serial")
public class DSPAgent extends AbstractAgent {

	/**
	 * The agent's environment. 
	 * Here it is just used to know its boundaries. 
	 * It will be automatically set
	 * by the environment agent itself: No need to instantiate anything here.
	 */
	private EnvironmentAgent environment;
	private HashMap<ConversationID, Integer> bidHistory = new HashMap<ConversationID, Integer>();
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
			System.out.println("PA"+m.getSender()+"->DSP"+m.getReceiver()+": "+m.getContent());
			String[] msgArray = m.getContent().split(" ");

			// switching according to first part of message content
			switch (msgArray[0]) {

			// we have a proposition we answer
			case "askForBid":
				int bid = Math.min(budget, (int)(Math.random()*100));
				bidHistory.put(m.getConversationID(), bid);
				sendReply(m, new StringMessage("answerBid " + bid));
				break;

				// we won the competition!
			case "winBid":
				budget -= Integer.parseInt(msgArray[1]);
				System.out.println("new budget: "+budget);
				break;
			}

			m = (StringMessage) nextMessage();
		} 	
	}
}
