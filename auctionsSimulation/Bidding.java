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

import madkit.kernel.AgentAddress;


/**
 * Class used for storing a 3-tuple information (bidValue, bidder, numberOfReceivedBids)
 */
public class Bidding {

	/** Bid value **/
	int bidValue;
	/** Bidder (associated to bid value) **/
	AgentAddress bidder;
	/** Counting number of bids already received **/
	int bidsCount;

	/**
	 * Contructor
	 */
	public Bidding(int bidValue, AgentAddress bidder) {
		this.bidValue = bidValue;
		this.bidder = bidder;
		this.bidsCount = 1;
	}

	/**
	 * Setter for two first attributes at once (bidValue, bidder) and auto increment bidsCount
	 */
	public void set(int bv, AgentAddress b) {
		this.bidValue = bv;
		this.bidder = b;
		this.incrementBidsCount();
	}

	/** getters and setters **/
	public int getBidValue() { return bidValue;	}
	public void setBidValue(int bidValue) { this.bidValue = bidValue; }
	public AgentAddress getBidder() { return bidder; }
	public void setBidder(AgentAddress bidder) { this.bidder = bidder; }
	public int getBidsCount() { return bidsCount; }
	public void incrementBidsCount() { this.bidsCount += 1; }

}