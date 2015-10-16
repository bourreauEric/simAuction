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

import madkit.kernel.AbstractAgent;
import madkit.kernel.Scheduler;
import madkit.simulation.activator.GenericBehaviorActivator;

/**
 * 		#jws simulation.ex06.MySimulationModel jws#
 * 
 *  Nothing really new here, except that we define
 *  an additional Activator which is used to schedule the display.
 *  Especially, this is about calling the "observe" method of
 *  agents having the role of viewer in the organization
 * 
 */
public class MyScheduler extends Scheduler {

	protected GenericBehaviorActivator<AbstractAgent> pub_agents;
	protected GenericBehaviorActivator<AbstractAgent> dsp_agents;
	protected GenericBehaviorActivator<AbstractAgent> viewers;

	@Override
	protected void activate() {
		// 1 : request my role
		requestRole(MySimulationModel.MY_COMMUNITY,
				MySimulationModel.SIMU_GROUP,
				MySimulationModel.SCH_ROLE); 

		// 2 : initialize the activators
		// by default, they are activated once each in the order they have been added
		pub_agents = new GenericBehaviorActivator<AbstractAgent>(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.PUB_ROLE, "manageAds");
		addActivator(pub_agents);
		dsp_agents = new GenericBehaviorActivator<AbstractAgent>(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.DSP_ROLE, "manageAuction");
		addActivator(dsp_agents);
		viewers = new GenericBehaviorActivator<AbstractAgent>(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.VIEWER_ROLE, "observe");
		addActivator(viewers);

		// 3 : let us start the simulation automatically
		setSimulationState(SimulationState.RUNNING);

		// 4 : delay
		setDelay(20);
	}

}