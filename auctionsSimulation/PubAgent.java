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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import madkit.kernel.AbstractAgent;
import madkit.kernel.AgentAddress;
import madkit.kernel.ConversationID;
import madkit.kernel.Message;
import madkit.message.StringMessage;

// VOCABULARY
// ask
// answer IntValue
// win

public class PubAgent extends AbstractAgent {
	
	/**
	 * The agent's environment. 
	 * Here it is just used to know its boundaries. 
	 * It will be automatically set
	 * by the environment agent itself: No need to instantiate anything here.
	 */
	private EnvironmentAgent environment;
	private HashMap<ConversationID, Integer> encheres = new  HashMap<ConversationID, Integer>();
	private HashMap<ConversationID, AgentAddress> encherisseurs = new  HashMap<ConversationID, AgentAddress>();
	
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
	private void askIt() {
		Integer value,bestValue;
		StringMessage m = (StringMessage) nextMessage();
	 	while(m != null) {
	 		if (m.getContent().startsWith("answer")) {
	 			value = new Integer(Integer.parseInt(m.getContent().split(" ")[1]));
	 			if (encheres.containsKey(m.getConversationID())) {
		 		    bestValue = encheres.get(m.getConversationID());
		 			if (bestValue < value) {
		 				encheres.put(m.getConversationID(), value);
		 				encherisseurs.put(m.getConversationID(), m.getSender()); 				
		 			}
	 			} else {
	 				encheres.put(m.getConversationID(), value);
	 				encherisseurs.put(m.getConversationID(), m.getSender()); 				
	 			}
	 		m = (StringMessage) nextMessage();
	 	} 	
		if (((int) Math.random()*10) == 0) {
			broadcastMessage(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE, new StringMessage("ask"));					
		}
	}
}