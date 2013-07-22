package se.callista.websocketlabs.wsone.rpi.impl;

import se.callista.websocketlabs.wsone.rpi.api.RPi;

public class RPITeststub implements RPi {

	boolean isLedOn = false;
	
	@Override
	public boolean isLedOn() {
		return isLedOn;
	}

	@Override
	public void setLedOn(boolean isLedOn) {
		this.isLedOn = isLedOn;
	}
}
