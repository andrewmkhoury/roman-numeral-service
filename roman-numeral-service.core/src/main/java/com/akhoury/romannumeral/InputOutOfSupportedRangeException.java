package com.akhoury.romannumeral;

public class InputOutOfSupportedRangeException extends Exception {
	public InputOutOfSupportedRangeException(String inputInt) {
        super("Input value " + inputInt + " is out of supported range 1-3999");
    }
}
