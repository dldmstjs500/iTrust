package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class PatientMessageFilterTest extends TestCase {
    private TestDataGenerator gen = new TestDataGenerator();
    private PatientDAO patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
    }

    /**
     * Check that getting a filter for a patient that doesn't have one gives the default filter
     * @throws Exception
     */
    public void testGetNoFilter() throws Exception {
        gen.patient1();
        assertEquals(patientDAO.getMessageFilter(1l), ",,,,,");
    }

    /***
     * Set a filter for a user that doesn't have one
     * @throws Exception
     */
    public void testNewFilter() throws Exception {
        gen.patient1();
        patientDAO.saveMessageFilter("test filter 3,,,,,", 1l);
        assertEquals(patientDAO.getMessageFilter(1l), "test filter 3,,,,,");
    }

    /***
     * Attempt to update a filter that already exists
     * @throws Exception
     */
    public void testUpdateFilter() throws Exception {
        gen.patient1();
        patientDAO.saveMessageFilter("test filter 1,,,,,", 1l);
        patientDAO.saveMessageFilter("test filter 2,,,,,", 1l);
        assertEquals(patientDAO.getMessageFilter(1l), "test filter 2,,,,,");
    }

    /***
     * Getting a message filter for a user that doesn't exist should throw an error
     * @throws Exception
     */
    public void testFilterNotExist() throws Exception {
        gen.patient1();
        try {
            patientDAO.getMessageFilter(1236546789l);
            fail("exception should have been thrown");
        } catch (ITrustException e) {
            assertEquals("User does not exist", e.getMessage());
        }
    }
}
