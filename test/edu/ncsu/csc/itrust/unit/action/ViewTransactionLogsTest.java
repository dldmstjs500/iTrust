package edu.ncsu.csc.itrust.unit.action;
import edu.ncsu.csc.itrust.action.ViewTransactionLogs;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.enums.TransactionType;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class ViewTransactionLogsTest extends TestCase{
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private ViewTransactionLogs action;
    private TestDataGenerator gen;

    @Override
    protected void setUp() throws Exception {
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
    }

    @Test
    public void testGetAllTransactions() throws Exception{
        action = new ViewTransactionLogs(factory);
        // if start date and end date are given
        List<TransactionBean> rs1 = action.getTransactionsForAll(new Date(), new Date());
        assertEquals(0, rs1.size());
        // if start date and end date are null
        List<TransactionBean> rs2 = action.getTransactionsForAll(null, null);
        assertEquals(47, rs2.size());
    }

    @Test
    public void testGetRequestTransactions() throws  Exception{
        action = new ViewTransactionLogs(factory);
        // LoggingRole.compareTo("")==0 && SecondaryRole.compareTo("")==0 && type == -1
        List<TransactionBean> rs1 = action.getRequestTransactions("", "", null, null, -1);
        assertEquals(47, rs1.size());
        // LoggingRole.compareTo("")==0 && SecondaryRole.compareTo("")==0 && type != -1
        List<TransactionBean> rs2 = action.getRequestTransactions("", "", null, null, 1600);
        assertEquals(4, rs2.size());
        // LoggingRole.compareTo(first)==0 && SecondaryRole.compareTo("")==0 && type == -1
        List<TransactionBean> rs3 = action.getRequestTransactions("patient", "", null, null, -1);
        assertEquals(4, rs3.size());
        // LoggingRole.compareTo(first)==0 && SecondaryRole.compareTo("")==0 && type != -1
        List<TransactionBean> rs4 = action.getRequestTransactions("patient", "", null, null, 1900);
        assertEquals(2, rs4.size());
        // LoggingRole.compareTo("")==0 && SecondaryRole.compareTo(second)==0 && type == -1
        List<TransactionBean> rs5 = action.getRequestTransactions("", "patient", null, null, -1);
        assertEquals(44, rs5.size());
        // LoggingRole.compareTo("")==0 && SecondaryRole.compareTo(second)==0 && type != -1
        List<TransactionBean> rs6 = action.getRequestTransactions("", "patient", null, null, 1600);
        assertEquals(4, rs6.size());
        // LoggingRole.compareTo(first)==0 && SecondaryRole.compareTo(second)==0 && type == -1
        List<TransactionBean> rs7 = action.getRequestTransactions("patient", "patient", null, null, -1);
        assertEquals(2, rs7.size());
        // LoggingRole.compareTo(first)==0 && SecondaryRole.compareTo(second)==0 && type != -1
        List<TransactionBean> rs8 = action.getRequestTransactions("patient", "patient", null, null, 1900);
        assertEquals(2, rs8.size());
    }

    @Test
    public void testGetNumForLoggedIn() throws Exception {
        action = new ViewTransactionLogs(factory);
        List<TransactionBean> rs1= action.getRequestTransactions("patient", "patient", new Date(), new Date(), 1);
        int numLoggedIn1 = action.getNumForLoggedIn(rs1, "all");
        assertEquals(0,numLoggedIn1);
        List<TransactionBean> rs2= action.getRequestTransactions("", "", null, null, -1);
        int numLoggedIn2 = action.getNumForLoggedIn(rs2, "patient");
        assertEquals(4,numLoggedIn2);
    }

    @Test
    public void testGetNumForSecondary() throws Exception {
        action = new ViewTransactionLogs(factory);
        List<TransactionBean> rs2= action.getRequestTransactions("", "", null, null, -1);
        int numSecondary1 = action.getNumForSecondary(rs2, "all");
        assertEquals(0,numSecondary1);
        int numSecondary2 = action.getNumForSecondary(rs2, "patient");
        assertEquals(44,numSecondary2);
    }

    @Test
    public void testGetNumForType() throws Exception {
        action = new ViewTransactionLogs(factory);
        List<TransactionBean> rs1 = action.getRequestTransactions("patient", "patient", new Date(), new Date(), 1);
        int numType1 = action.getNumForType(rs1, 300);
        assertEquals(0, numType1);
        List<TransactionBean> rs2 = action.getRequestTransactions("", "", null, null, -1);
        int numType2 = action.getNumForType(rs2, 1900);
        assertEquals(22, numType2);
    }

    @Test
    public void testGetNumForMonth() throws Exception {
        action = new ViewTransactionLogs(factory);
        List<TransactionBean> rs1 = action.getRequestTransactions("patient", "patient", new Date(), new Date(), 1);
        int month1 = action.getNumForMonth(rs1, 2017, 12);
        assertEquals(0, month1);
        List<TransactionBean> rs2 = action.getRequestTransactions("", "", null, null, -1);
        int month2 = action.getNumForMonth(rs2, 2007, 6);
        assertEquals(31, month2);
    }
}
