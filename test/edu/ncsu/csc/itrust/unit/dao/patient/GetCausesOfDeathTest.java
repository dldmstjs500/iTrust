package edu.ncsu.csc.itrust.unit.dao.patient;

import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.*;

//Tests getCausesOfDeath and getCausesOfDeathGender in PatientDAO.java
public class GetCausesOfDeathTest extends TestCase {
    private PatientDAO patientDAO;
    Date d00 = new Date(100, 1, 1);
    Date d01 = new Date(101, 12, 31);
    Date d10 = new Date(110, 12, 31);

    @Override
    protected void setUp() throws Exception {
        patientDAO = TestDAOFactory.getTestInstance().getPatientDAO();
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Test for a year range where there are no results
     * @throws Exception
     */
    public void testGetCausesOfDeathNoResults() throws Exception {
        ArrayList<String> res = patientDAO.getCausesOfDeath(d00, d01);
        assertEquals(res.size(), 0);

    }

    /**
     * Test for a year range where there is one result
     * @throws Exception
     */
    public void testGetCausesOfDeath() throws Exception {
        ArrayList<String> res = patientDAO.getCausesOfDeath(d00, d10);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0), "2|250.10|Diabetes with ketoacidosis");

    }

    /**
     * Test for a year range where there are no results
     * @throws Exception
     */
    public void testGetCausesOfDeathGenderNoResults() throws Exception {
        ArrayList<String> res = patientDAO.getCausesOfDeathGender(d00, d01, "Male");
        assertEquals(res.size(), 0);

    }

    /**
     * Test for a year range where there is one result
     * @throws Exception
     */
    public void testGetCausesOfDeathGender() throws Exception {
        ArrayList<String> res = patientDAO.getCausesOfDeathGender(d00, d10, "Male");
        assertEquals(res.size(), 1);
        assertEquals(res.get(0), "2|250.10|Diabetes with ketoacidosis");
    }
}
