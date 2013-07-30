package se.callista.websocketlabs.wsone.rpi.impl;

import java.math.BigInteger;

import se.callista.websocketlabs.wsone.rpi.api.RPi;

public abstract class RPiBaseImpl implements RPi {

	public BigInteger fibonacci(int i) {
		return fibonacciIterativeBigInteger(i);
	}
	
	protected long fibonacciIterativeLong(int max) {
		long n1 = 0;
		long n2 = 1;
		long tmp;
		
		for (int i = 1; i <= max; i++) {
			tmp = n1;
			n1 = n1 + n2;
			n2 = tmp;
		}
		
		return n1;
	}

	protected BigInteger fibonacciIterativeBigInteger(int max) {
		BigInteger n1 = new BigInteger("0");
		BigInteger n2 = new BigInteger("1");
		BigInteger tmp;
		
		for (int i = 1; i <= max; i++) {
			tmp = n1;
			n1 = n1.add(n2);
			n2 = tmp;
		}

		return n1;
	}

	protected long fibonacciRecursiveLong(int n)  {
		if (n == 0)      return 0;
		else if (n == 1) return 1;
		else             return fibonacciRecursiveLong(n - 1) + fibonacciRecursiveLong(n - 2);
	}

	protected double fibonacciMathDouble(int n) {
	    double f1 = Math.pow(((1 + Math.sqrt(5)) / 2.0), n);
	    double f2 = Math.pow(((1 - Math.sqrt(5)) / 2.0), n);

	    return Math.floor((f1 - f2) / Math.sqrt(5));
	}	

}
