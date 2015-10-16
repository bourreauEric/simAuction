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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probe.PropertyProbe;
import madkit.simulation.probe.SingleAgentProbe;
import madkit.simulation.viewer.SwingViewer;

/**
 * This class will be used to display the simulation.
 * We could have extended the {@link Watcher} class, but there are a lot of
 * things already defined in {@link SwingViewer}. So why not use it.
 * 
 */
public class Viewer extends SwingViewer {

	/**
	 * environment's size, probed using a {@link SingleAgentProbe}.
	 */
	private Dimension											envSize;

	/**
	 * The probe by which we will get the agents' location
	 */
	protected PropertyProbe<AbstractAgent, Dimension>	 agentsLocationProbe;

	@Override
	protected void activate() {
		// 1 : request my role so that the scheduler can take me into account
		requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP,
				MySimulationModel.VIEWER_ROLE);

		// 2 : adding the probes 
		
		// probing the environment using an anonymous inner class
		SingleAgentProbe<EnvironmentAgent, Dimension> envProbe = new SingleAgentProbe<EnvironmentAgent, Dimension>(
				MySimulationModel.MY_COMMUNITY, 
				MySimulationModel.SIMU_GROUP,
				MySimulationModel.ENV_ROLE, 
				"dimension") {
				protected void adding(EnvironmentAgent agent) {
					super.adding(agent);
					envSize = getPropertyValue();
				}
		};
		addProbe(envProbe);

		// probing agents' location
		agentsLocationProbe = new PropertyProbe<AbstractAgent, Dimension>(
				MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP,
				MySimulationModel.DSP_ROLE, "location");
		addProbe(agentsLocationProbe);

		// 3 : Now that the probes are added,
		// we can setup the frame for the display according to the environment's properties
		getDisplayPane().setPreferredSize(envSize);
		getFrame().pack();

		// 4 (optional) set the synchronous painting mode: The display will be updated
		// for each step of the simulation.
		// Here it is useful because the simulation goes so fast that the agents
		// are almost invisible
		setSynchronousPainting(true);
	}

	/**
	 * render is the method where the custom painting has to be done.
	 * Here, we just draw red points at the agents' location
	 */
	@Override
	protected void render(Graphics g) {
		g.setColor(Color.RED);
		for (AbstractAgent a : agentsLocationProbe.getCurrentAgentsList()) {
			Dimension location = agentsLocationProbe.getPropertyValue(a);
			g.drawRect(location.width, location.height, 1, 1);
		}
	}

}
