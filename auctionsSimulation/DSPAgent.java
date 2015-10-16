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

	/**
	 * initialize my role and fields
	 */
	@Override
	protected void activate() {
		requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE);
		environment.getDimension();
	}

	/**
	 *  Réponse à l'enchère
	 */
	@SuppressWarnings("unused")
	private void manageAuction() {
		StringMessage m = (StringMessage) nextMessage();
		while(m != null) {
			System.out.println("PA"+m.getSender()+"->DSP"+m.getReceiver()+": "+m.getContent());
			if (m.getContent() == "win") {
				m.getConversationID();
			} else {
				sendReply(m, new StringMessage("answer "+((int)(Math.random()*1))));
			}
			m = (StringMessage) nextMessage();
		} 	
	}
}
