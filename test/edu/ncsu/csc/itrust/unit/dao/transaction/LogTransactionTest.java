package edu.ncsu.csc.itrust.unit.dao.transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Test;

public class LogTransactionTest extends TestCase {
	private TransactionDAO tranDAO = TestDAOFactory.getTestInstance().getTransactionDAO();

	private TestDataGenerator gen;
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.transactionLog();
	}

	@Test
	public void testGetAllTransactions() throws Exception {
		List<TransactionBean> list = tranDAO.getAllTransactions();
		assertEquals(8, list.size());
		// that last one inserted should be last because it was backdated
		assertEquals(1L, list.get(3).getLoggedInMID());
		assertEquals(TransactionType.DEMOGRAPHICS_EDIT, list.get(3).getTransactionType());
	}

	@Test
	public void testLogFull() throws Exception {
		tranDAO.logTransaction(TransactionType.OFFICE_VISIT_EDIT, 9000000000L, 1L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();
		assertEquals(9, list.size());
		assertEquals(9000000000L, list.get(0).getLoggedInMID());
		assertEquals(1L, list.get(0).getSecondaryMID());
		assertEquals("added information", list.get(0).getAddedInfo());
		assertEquals(TransactionType.OFFICE_VISIT_EDIT, list.get(0).getTransactionType());
	}
	
	/**
	 * Tests to see if the right MID number shows up in the secondaryMID column in the transactionLog.
	 * @throws Exception
	 */
	@Test
	public void testSecondaryMIDHCP() throws Exception{
		tranDAO.logTransaction(TransactionType.PATIENT_CREATE, 9000000000L, 98L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();

		assertEquals(9000000000L, list.get(0).getLoggedInMID());
		assertEquals(98L, list.get(0).getSecondaryMID());
	}

	@Test
	public void testSecondaryMIDPatient() throws Exception{
		tranDAO.logTransaction(TransactionType.PATIENT_CREATE, 1L, 98L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();

		assertEquals(1L, list.get(0).getLoggedInMID());
		assertEquals(98L, list.get(0).getSecondaryMID());
	}

	@Test
	public void testSecondaryMIDUAP() throws Exception{
		tranDAO.logTransaction(TransactionType.PATIENT_CREATE, 9000000001L, 98L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();

		assertEquals(9000000001L, list.get(0).getLoggedInMID());
		assertEquals(98L, list.get(0).getSecondaryMID());
	}

	@Test
	public void testGetTransactionsForDate() throws Exception{
		tranDAO.logTransaction(TransactionType.PATIENT_CREATE, 9000000002L, 98L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();
		List<TransactionBean> result = tranDAO.getTransactionsForDate(list.get(0).getTimeLogged(), list.get(0).getTimeLogged());
		String date = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(result.get(0).getTimeLogged());

		assertEquals(date, new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(result.get(0).getTimeLogged()));
	}

	@Test
	public void testGetRole() throws Exception{
		tranDAO.logTransaction(TransactionType.PATIENT_CREATE, 9000000003L, 98L, "added information");
		List<TransactionBean> list = tranDAO.getAllTransactions();

		assertEquals(null, tranDAO.getRole(list.get(0).getLoggedInMID()));
	}
}
