package edu.ncsu.csc.itrust.unit.action;

import edu.ncsu.csc.itrust.action.ViewCauseOfDeathReportAction;
import edu.ncsu.csc.itrust.beans.CauseOfDeathReportBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewCauseOfDeathReportActionTest extends TestCase {
    ViewCauseOfDeathReportAction action;

    /**
     * Add 5 new dead patients: 3 with influenza (that had office visits with 9000000000), and 2 that died of malaria (that didn't)
     * Patient 2 already died of Diabetes with ketoacidosis
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();

        DAOFactory factory = TestDAOFactory.getTestInstance();
        PatientDAO patientDAO = factory.getPatientDAO();

        //Add dead patients
        //Numbers chosen so only one possible output (e.g. not two maximums)
        //Patient 2 already has code 250.10 ("Diabetes with ketoacidosis")
        PatientBean p;
        //Patient 1, 3, and 22 had a visit with 9000000000; die of influenza
        for (Long l : Arrays.asList(1l, 3l, 22l)) {
            p = patientDAO.getPatient(l);
            p.setCauseOfDeath("487.00");// Die of influenza
            p.setDateOfDeathStr("05/05/2006");
            patientDAO.editPatient(p, -1);
        }

        //Patient 10, and 11 did not; give malaria
        for (Long l : Arrays.asList(10l, 11l)) {
            p = patientDAO.getPatient(l);
            p.setCauseOfDeath("84.50");// Die of malaria
            p.setDateOfDeathStr("08/08/2008");
            patientDAO.editPatient(p, -1);
        }
        action = new ViewCauseOfDeathReportAction(factory);
    }

    /**
     * Enter an invalid year, check error is thrown
     * @throws Exception
     */
    public void testInvalidYear() throws Exception {
        try {
            action.getReport("abcd", "2017", "", 0l);
            fail("Exception should have been thrown");
        } catch (FormValidationException e) {
            assert(e.getMessage().contains("Invalid year entered"));
        }
    }

    //Start year after end year, check error
    public void testStartAfterEnd() throws Exception {
        try {
            action.getReport("2018", "2017", "", 0l);
            fail("Exception should have been thrown");
        } catch (FormValidationException e) {
            assert(e.getMessage().contains("Start date must be before end date!"));
        }
    }

    /**
     * Get all patients from 2000 to 2010; check values are as expected
     * @throws Exception
     */
    public void testAllPatients() throws Exception{
        CauseOfDeathReportBean codbean = action.getReport("2000", "2010", "All", 9000000000l);
        assertEquals(codbean.getAllcode1(), "487.00");
        assertEquals(codbean.getAllcode2(), "84.50");
        assertEquals(codbean.getHcpcode1(), "487.00");
        assertEquals(codbean.getHcpcode2(), "250.10");
        assertEquals(codbean.getAllname1(), "Influenza");
        assertEquals(codbean.getAllname2(), "Malaria");
        assertEquals(codbean.getHcpname1(), "Influenza");
        assertEquals(codbean.getHcpname2(), "Diabetes with ketoacidosis");
        assertEquals(codbean.getAllcount1(), 3);
        assertEquals(codbean.getAllcount2(), 2);
        assertEquals(codbean.getHcpcount1(), 3);
        assertEquals(codbean.getHcpcount2(), 1);
    }

    /**
     * Get all male patients from 2000 to 2010; check values are as expected
     * @throws Exception
     */
    public void testMalePatients() throws Exception {
        CauseOfDeathReportBean codbean = action.getReport("2000", "2010", "Male", 9000000000l);
        assertEquals(codbean.getAllcode1(), "487.00");
        assertEquals(codbean.getAllcode2(), "250.10");
        assertEquals(codbean.getHcpcode1(), "487.00");
        assertEquals(codbean.getHcpcode2(), "250.10");
        assertEquals(codbean.getAllname1(), "Influenza");
        assertEquals(codbean.getAllname2(), "Diabetes with ketoacidosis");
        assertEquals(codbean.getHcpname1(), "Influenza");
        assertEquals(codbean.getHcpname2(), "Diabetes with ketoacidosis");
        assertEquals(codbean.getAllcount1(), 2);
        assertEquals(codbean.getAllcount2(), 1);
        assertEquals(codbean.getHcpcount1(), 2);
        assertEquals(codbean.getHcpcount2(), 1);

    }

    /**
     * Get all female patients from 2000 to 2010; check values are as expected
     * @throws Exception
     */
    public void testFemalePatients() throws Exception {
        CauseOfDeathReportBean codbean = action.getReport("2000", "2010", "Female", 9000000000l);
        assertEquals(codbean.getAllcode1(), "487.00");
        assertEquals(codbean.getAllcode2(), "N/A");
        assertEquals(codbean.getHcpcode1(), "487.00");
        assertEquals(codbean.getHcpcode2(), "N/A");
        assertEquals(codbean.getAllname1(), "Influenza");
        assertEquals(codbean.getAllname2(), "N/A");
        assertEquals(codbean.getHcpname1(), "Influenza");
        assertEquals(codbean.getHcpname2(), "N/A");
        assertEquals(codbean.getAllcount1(), 1);
        assertEquals(codbean.getAllcount2(), 0);
        assertEquals(codbean.getHcpcount1(), 1);
        assertEquals(codbean.getHcpcount2(), 0);
    }

    /**
     * Get all patients from 2000 to 2001; there should be none dead
     * @throws Exception
     */
    public void testRangeNoPatients() throws Exception {
        CauseOfDeathReportBean codbean = action.getReport("2000", "2001", "All", 9000000000l);
        assertEquals(codbean.getAllcode1(), "N/A");
        assertEquals(codbean.getAllcode2(), "N/A");
        assertEquals(codbean.getHcpcode1(), "N/A");
        assertEquals(codbean.getHcpcode2(), "N/A");
        assertEquals(codbean.getAllname1(), "N/A");
        assertEquals(codbean.getAllname2(), "N/A");
        assertEquals(codbean.getHcpname1(), "N/A");
        assertEquals(codbean.getHcpname2(), "N/A");
        assertEquals(codbean.getAllcount1(), 0);
        assertEquals(codbean.getAllcount2(), 0);
        assertEquals(codbean.getHcpcount1(), 0);
        assertEquals(codbean.getHcpcount2(), 0);
    }

}
