package com.source3g.hermes.entity.device;

import com.source3g.hermes.entity.sim.SimInfo;

public class SimChangeRecord {

	private SimInfo oldSim;
	private SimInfo newSim;

	public SimChangeRecord(SimInfo oldSim, SimInfo newSim) {
		super();
		this.oldSim = oldSim;
		this.newSim = newSim;
	}

	public SimChangeRecord() {
		super();
	}

	public SimInfo getOldSim() {
		return oldSim;
	}

	public void setOldSim(SimInfo oldSim) {
		this.oldSim = oldSim;
	}

	public SimInfo getNewSim() {
		return newSim;
	}

	public void setNewSim(SimInfo newSim) {
		this.newSim = newSim;
	}

}
