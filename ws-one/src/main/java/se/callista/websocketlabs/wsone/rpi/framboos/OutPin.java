package se.callista.websocketlabs.wsone.rpi.framboos;

import static se.callista.websocketlabs.wsone.rpi.framboos.FilePaths.*;

public class OutPin extends GpioPin {
	
	public OutPin(int pinNumber) {
		super(pinNumber, Direction.OUT);
//		setValue(false);
	}
	
	public void setValue(boolean isOne) {
		if (!isClosing) {
			writeFile(getValuePath(pinNumber), isOne ? "1" : "0");
		}
	}
	
	@Override
	public void close() {
//		setValue(false);
		super.close();
	}
}
