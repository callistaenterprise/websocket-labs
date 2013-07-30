package se.callista.websocketlabs.wsone.rpi.api;

import java.math.BigInteger;

public interface RPi {
	public boolean isLedOn();
	public void setLedOn(boolean isLedOn);
	
	public BigInteger fibonacci(int i);
}
