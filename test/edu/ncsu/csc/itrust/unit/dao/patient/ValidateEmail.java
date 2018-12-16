package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Test;

@SuppressWarnings("unused")
public class ValidateEmail extends TestCase {
	PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
	TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.patient2();
	}

	@Test
	public void testValidateEmail() throws Exception {
		PatientBean p = patientDAO.getPatient(2);
		assertTrue(patientDAO.checkForPatientsWithEmail("andy.programmer@gmail.com"));
		assertFalse(patientDAO.checkForPatientsWithEmail("email"));
	}
}
