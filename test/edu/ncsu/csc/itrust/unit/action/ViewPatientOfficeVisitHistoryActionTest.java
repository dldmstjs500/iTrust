package edu.ncsu.csc.itrust.unit.action;

import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction;
import edu.ncsu.csc.itrust.beans.PatientVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Test;

/**
 * ViewPatientOfficeVisitHistoryActionTest
 */
public class ViewPatientOfficeVisitHistoryActionTest extends TestCase{

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private ViewPatientOfficeVisitHistoryAction action;
	
	@Override
	protected void setUp() throws Exception{
		action = new ViewPatientOfficeVisitHistoryAction(factory, 9000000000L);
		gen.clearAllTables();
		gen.standardData();
		gen.preRegisteredPatientData();
	}
	
	/**
	 * testGetPersonnel
	 * @throws Exception
	 */
	@Test
	public void testGetPersonnel() throws Exception {
		PersonnelBean hcp = action.getPersonnel();
		assertNotNull(hcp.getFirstName(), "Kelly");
	}
	
	/**
	 * testGetPatients
	 * @throws Exception
	 */
	@Test
	public void testGetPatients() throws Exception {
		List<PatientVisitBean> list = action.getPatients();
		assertEquals(59, list.size());
		assertEquals("31", list.get(21).getLastOVDateD());
		assertEquals("03", list.get(21).getLastOVDateM());
	}
	@Test
	public void testGetPreRegisteredPatients() throws Exception {
		List<PatientBean> list = action.getPreRegisteredPatients();
		assertEquals(2, list.size());
		assertEquals("60", list.get(0).getHeight());
		assertEquals("120", list.get(0).getWeight());
	}

	@Test
	public void testRegisterPatient() throws Exception {
		List<PatientBean> list = action.getPreRegisteredPatients();
		action.registerPatient(list.get(0));
		list = action.getPreRegisteredPatients();
		assertEquals(1, list.size());
	}

	@Test
	public void testDeregisterPatient() throws Exception {
		List<PatientBean> list = action.getPreRegisteredPatients();
		action.deregisterPatient(list.get(0));
		list = action.getPreRegisteredPatients();
		assertEquals(1, list.size());
	}
}
