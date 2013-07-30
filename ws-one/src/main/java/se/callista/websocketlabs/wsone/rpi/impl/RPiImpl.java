package se.callista.websocketlabs.wsone.rpi.impl;

import se.callista.websocketlabs.wsone.rpi.framboos.OutPin;

public class RPiImpl extends RPiBaseImpl {

	private final int LED_PIN_NO = 0;
	private OutPin pin = null;

	public RPiImpl() {
		pin = new OutPin(LED_PIN_NO);
	}
	
	@Override
	public boolean isLedOn() {
		return pin.getValue();
	}

	@Override
	public void setLedOn(boolean isLedOn) {
		pin.setValue(isLedOn);
	}
}
