package edu.ncsu.csc.itrust.unit.validate.regex;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.unit.testutils.ValidatorProxy;
import edu.ncsu.csc.itrust.validate.ValidationFormat;

public class GenderValidatorTest extends TestCase {
	private ValidatorProxy validatorProxy = new ValidatorProxy();
	private static final ValidationFormat VALIDATION_FORMAT = ValidationFormat.ADDRESS;
	private static final String PASSED = "";
	private static final String FAILED = "Name: " + VALIDATION_FORMAT.getDescription();

	public void testGoodAnswer() throws Exception {
		String value = "356 Some place blvd";
		String errorMessage = PASSED;
		assertEquals(errorMessage, validatorProxy.checkFormat("Name", value, VALIDATION_FORMAT, false));
	}

	public void testBadLength() throws Exception {
		String value = "abcabcabcabcabcabcabcabcabcabca";
		String errorMessage = FAILED;
		assertEquals(errorMessage, validatorProxy.checkFormat("Name", value, VALIDATION_FORMAT, false));
	}

	public void testBadLetters() throws Exception {
		String value = "bob%";
		String errorMessage = FAILED;
		assertEquals(errorMessage, validatorProxy.checkFormat("Name", value, VALIDATION_FORMAT, false));
	}
}
