package se.callista.websocketlabs.wsone.rpi.impl;

public class RPITeststub extends RPiBaseImpl {

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
