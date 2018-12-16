/**
 * Tests for AddPatientAction
 */

package edu.ncsu.csc.itrust.unit.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.AddPatientAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Test;

public class AddPatientActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	private AddPatientAction action;
	
/**
 * Sets up defaults
 */
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.transactionLog();
		gen.hcp0();
		action = new AddPatientAction(factory, 9000000000L);
	}

	/**
	 * Tests adding a new patient
	 * @throws Exception
	 */
	@Test
	public void testAddPatient() throws Exception {
		AuthDAO authDAO = factory.getAuthDAO();
		
		//Add a dependent
		PatientBean p = new PatientBean();
		p.setFirstName("Jiminy");
		p.setLastName("Cricket");
		p.setEmail("make.awish@gmail.com");
		long newMID = action.addDependentPatient(p, 102);
		assertEquals(p.getMID(), newMID);
		assertTrue(authDAO.isDependent(newMID));
		
		//Add a non-dependent
		p.setFirstName("Chuck");
		p.setLastName("Cheese");
		p.setEmail("admin@chuckecheese.com");
		newMID = action.addPatient(p);
		assertEquals(p.getMID(), newMID);
		assertFalse(authDAO.isDependent(newMID));

		//Add a pre-registered patient
		p.setFirstName("Jiminy");
		p.setLastName("Cricket");
		p.setEmail("make2.awish@gmail.com");
		newMID = action.addPreRegisteredPatient(p, "password1234");
		assertEquals(p.getMID(), newMID);
		assertEquals(p.getActivated(),0);
	}
}
