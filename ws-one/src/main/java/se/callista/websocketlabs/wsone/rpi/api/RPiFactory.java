package se.callista.websocketlabs.wsone.rpi.api;

import se.callista.websocketlabs.wsone.rpi.impl.RPITeststub;
import se.callista.websocketlabs.wsone.rpi.impl.RPiImpl;

public class RPiFactory {
	static public RPi getRPi() {
		// FIX ME. For now assume that os.arch == arm means that we are on the RPi
		if (System.getProperty("os.arch").equals("arm")) {
			// FIX ME. We should only return one and the same instance here!
			return new RPiImpl();
		} else {
			return new RPITeststub();
		}
	}
}
