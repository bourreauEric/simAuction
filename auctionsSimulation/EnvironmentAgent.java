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

import java.awt.Dimension;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probe.PropertyProbe;


/**
 * This agent is used to model a quite simple environment.
 * Nothing in it; It just defines its boundaries and uses
 * a {@link PropertyProbe} to set the agents' environment field so 
 * that they can use the environment's methods once they enter
 * the artificial society.
 * 
 */
public class EnvironmentAgent extends Watcher {
	
	/**
	 * environment's boundaries
	 */
	private Dimension	 dimension;
	
	/**
	 * so that the agents can perceive my dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	protected void activate() {
		dimension = new Dimension(400, 400);

		// 1 : request my role so that the viewer can probe me 
		requestRole(MySimulationModel.MY_COMMUNITY,
				MySimulationModel.SIMU_GROUP,
				MySimulationModel.ENV_ROLE);
		
		// 2 : this probe is used to initialize the agents' environment field
		addProbe(new AgentsProbe(
					MySimulationModel.MY_COMMUNITY,
					MySimulationModel.SIMU_GROUP,
					MySimulationModel.DSP_ROLE, 
					"environment"));
	}

	
	class AgentsProbe extends PropertyProbe<AbstractAgent, EnvironmentAgent>{
		
		public AgentsProbe(String community, String group, String role, String fieldName) {
			super(community, group, role, fieldName);
		}

		protected void adding(AbstractAgent agent) {
			super.adding(agent);
			setPropertyValue(agent, EnvironmentAgent.this);
		}
}

}