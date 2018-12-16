package edu.ncsu.csc.itrust.unit.dao.personnel;

import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;


public class PersonnelLabProceduresFilterTest extends TestCase {
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
        gen.ltData0();
        assertEquals(personnelDAO.getLabProceduresFilter(5000000001l), "{}");
    }

    /***
     * Set a filter for a user that doesn't have one
     * @throws Exception
     */
    public void testNewFilter() throws Exception {
        gen.ltData0();
        personnelDAO.saveLabProceduresFilter(5000000001l, "some string");
        assertEquals(personnelDAO.getLabProceduresFilter(5000000001l), "some string");
    }

    /***
     * Attempt to update a filter that already exists
     * @throws Exception
     */
    public void testUpdateFilter() throws Exception {
        gen.ltData0();
        personnelDAO.saveLabProceduresFilter( 5000000001l, "first string");
        personnelDAO.saveLabProceduresFilter(5000000001l, "second string");
        assertEquals(personnelDAO.getLabProceduresFilter(5000000001l), "second string");
    }

    /***
     * Getting a LabProcedures filter for a user that doesn't exist should throw an error
     * @throws Exception
     */
    public void testFilterNotExist() throws Exception {
        gen.ltData0();
        try {
            personnelDAO.getLabProceduresFilter(1236546789l);
            fail("exception should have been thrown");
        } catch (ITrustException e) {
            assertEquals("User does not exist", e.getMessage());
        }
    }

}
