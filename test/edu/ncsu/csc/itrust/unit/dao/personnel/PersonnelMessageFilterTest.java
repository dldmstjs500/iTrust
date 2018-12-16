package edu.ncsu.csc.itrust.unit.dao.personnel;

import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class PersonnelMessageFilterTest extends TestCase {
    private TestDataGenerator gen = new TestDataGenerator();
    private PersonnelDAO personnelDAO = TestDAOFactory.getTestInstance().getPersonnelDAO();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
    }


    /**
     * Check that getting a filter for a personnel that doesn't have one gives the default filter
     * @throws Exception
     */
    public void testGetNoFilter() throws Exception {
        gen.hcp0();
        assertEquals(personnelDAO.getMessageFilter(9000000000l), ",,,,,");
    }

    /***
     * Set a filter for a user that doesn't have one
     * @throws Exception
     */
    public void testNewFilter() throws Exception {
        gen.hcp0();
        personnelDAO.saveMessageFilter("test filter 3,,,,,", 9000000000l);
        assertEquals(personnelDAO.getMessageFilter(9000000000l), "test filter 3,,,,,");
    }

    /***
     * Attempt to update a filter that already exists
     * @throws Exception
     */
    public void testUpdateFilter() throws Exception {
        gen.hcp0();
        personnelDAO.saveMessageFilter("test filter 1,,,,,", 9000000000l);
        personnelDAO.saveMessageFilter("test filter 2,,,,,", 9000000000l);
        assertEquals(personnelDAO.getMessageFilter(9000000000l), "test filter 2,,,,,");
    }

    /***
     * Getting a message filter for a user that doesn't exist should throw an error
     * @throws Exception
     */
    public void testFilterNotExist() throws Exception {
        gen.hcp0();
        try {
            personnelDAO.getMessageFilter(1236546789l);
            fail("exception should have been thrown");
        } catch (ITrustException e) {
            assertEquals("User does not exist", e.getMessage());
        }
    }
}
