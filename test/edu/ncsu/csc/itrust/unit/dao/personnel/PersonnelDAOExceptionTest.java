package edu.ncsu.csc.itrust.unit.dao.personnel;

import edu.ncsu.csc.itrust.exception.ITrustException;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.enums.Role;

public class PersonnelDAOExceptionTest extends TestCase {
	private PersonnelDAO evilDAO = EvilDAOFactory.getEvilInstance().getPersonnelDAO();

	@Override
	protected void setUp() throws Exception {
	}

	public void testAddEmptyPersonnelException() throws Exception {
		try {
			evilDAO.addEmptyPersonnel(Role.HCP);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testCheckPersonnelExistsException() throws Exception {
		try {
			evilDAO.checkPersonnelExists(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testEditPersonnelException() throws Exception {
		try {
			evilDAO.editPersonnel(new PersonnelBean());
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testGetHospitalsException() throws Exception {
		try {
			evilDAO.getHospitals(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
	
	public void testGetUAPHospitalsException() throws Exception {
		try {
			evilDAO.getUAPHospitals(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
	
	public void testGetNameException() throws Exception {
		try {
			evilDAO.getName(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	public void testPersonnelException() throws Exception {
		try {
			evilDAO.getPersonnel(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	/**
	 * Test that getMessageFilter throws a DBException
	 * @throws Exception
	 */
	public void testGetMessageFilter() throws Exception {
		try {
			evilDAO.getMessageFilter(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		} catch (ITrustException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getMessage());
		}
	}

	/**
	 * Test that saveMessageFilter throws a DBException
	 * @throws Exception
	 */
	public void testSetMessageFilter() throws Exception {
		try {
			evilDAO.saveMessageFilter("test", 0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}

	/**
	 * Test that getLabProceduresFilter throws a DBException
	 * @throws Exception
	 */
	public void testGetLabProceduresFilter() throws Exception {
		try {
			evilDAO.getLabProceduresFilter(0L);
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		} catch (ITrustException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getMessage());
		}
	}

	/**
	 * Test that saveLabProceduresFilter throws a DBException
	 * @throws Exception
	 */
	public void testSetLabProceduresFilter() throws Exception {
		try {
			evilDAO.saveLabProceduresFilter( 0L, "test");
			fail("DBException should have been thrown");
		} catch (DBException e) {
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
}