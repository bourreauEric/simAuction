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
		Integer value,bestValue;
		StringMessage m = (StringMessage) nextMessage();
		int count = getAgentsWithRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE).size();
		while(m != null) {
			System.out.println("DSP"+m.getSender()+"->PA"+m.getReceiver()+": "+m.getContent());
			if (m.getContent().startsWith("answer")) {
				value = new Integer(Integer.parseInt(m.getContent().split(" ")[1]));
				if (auctionValues.containsKey(m.getConversationID())) {
					bestValue = auctionValues.get(m.getConversationID());
					if (bestValue < value) {
						auctionValues.put(m.getConversationID(), value);
						auctionAgents.put(m.getConversationID(), m.getSender()); 				
					}
				} else {
					auctionValues.put(m.getConversationID(), value);
					auctionAgents.put(m.getConversationID(), m.getSender()); 				
				}
				m = (StringMessage) nextMessage();
			}
		}
		if (((int) (Math.random()*100)) == 0) {
			broadcastMessage(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE, new StringMessage("ask"));
			System.out.println("new ask from PubAgent");
		}
	}
}