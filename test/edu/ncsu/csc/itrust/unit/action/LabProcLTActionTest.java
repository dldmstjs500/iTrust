package edu.ncsu.csc.itrust.unit.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.filters.LabProceduresFilter;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.LabProcLTAction;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class LabProcLTActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private LabProcedureDAO lpDAO = factory.getLabProcedureDAO();
	private TestDataGenerator gen;
	LabProcLTAction action;
	LabProcLTAction action2;
	
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.transactionLog();
		gen.ltData0();
		gen.ltData1();
		gen.ltData2();
		gen.hcp0();
		gen.labProcedures();
		action = new LabProcLTAction(factory);
	}

	public void testUpdateProcedure() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusInTransit();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		List<LabProcedureBean> beans = action.viewInTransitProcedures(5000000005L);
		assertEquals(1, beans.size());
		LabProcedureBean lpBean = beans.get(0);
		
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals(procedures.getOvID(), lpBean.getOvID());
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals("In Transit", lpBean.getStatus());
		assertEquals(procedures.getProcedureID(), lpBean.getProcedureID());
	}

	public void testViewReceivedProcedures() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusReceived();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		List<LabProcedureBean> beans = action.viewReceivedProcedures(5000000005L);
		assertEquals(1, beans.size());
		LabProcedureBean lpBean = beans.get(0);
		
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals(procedures.getOvID(), lpBean.getOvID());
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals("Received", lpBean.getStatus());
		assertEquals(procedures.getProcedureID(), lpBean.getProcedureID());
	}

	public void testViewTestingProcedures() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusTesting();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		List<LabProcedureBean> beans = action.viewTestingProcedures(5000000005L);
		assertEquals(1, beans.size());
		LabProcedureBean lpBean = beans.get(0);
		
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals(procedures.getOvID(), lpBean.getOvID());
		assertEquals(procedures.getLoinc(), lpBean.getLoinc());
		assertEquals("Testing", lpBean.getStatus());
		assertEquals(procedures.getProcedureID(), lpBean.getProcedureID());
	}
	
	public void testGetLabProcedure() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusTesting();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		LabProcedureBean proc = lpDAO.getLabProcedure(id);
		
		assertEquals(lp.getOvID(), proc.getOvID());

	}

	public void testGetHCPName() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusTesting();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		lpDAO.getLabProcedure(id);
		
		assertEquals("Kelly Doctor", action.getHCPName(902L));
		
	}

	/**
	 * testSubmiteResults
	 * @throws Exception
	 */
	public void testSubmitResults() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusReceived();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		assertTrue(action.submitResults(""+id, "12", "grams", "13", "14"));
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		assertEquals("Pending", procedures.getStatus());
		assertEquals("12", procedures.getNumericalResult());
		assertEquals("grams", procedures.getNumericalResultUnit());
		assertEquals("13", procedures.getUpperBound());
		assertEquals("14", procedures.getLowerBound());

	}
	
	/**
	 * testSubmitReceived
	 * @throws Exception
	 */
	public void testSubmitReceived() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusInTransit();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		assertTrue(action.submitReceived(""+id));
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		assertEquals("Received", procedures.getStatus());
	}
	
	/**
	 * testSetToTesting
	 * @throws Exception
	 */
	public void testSetToTesting() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusReceived();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		assertTrue(action.setToTesting(id));
		
		LabProcedureBean procedures = lpDAO.getLabProcedure(id);
		
		assertEquals("Testing", procedures.getStatus());
	}
	
	/**
	 * testGetLabProc
	 * @throws Exception
	 */
	public void testGetLabProc() throws Exception {
		LabProcedureBean lp = new LabProcedureBean();
		lp.setPid(2L);
		lp.setLoinc("10763-1");
		lp.setCommentary("This is a test");
		lp.setOvID(902L);
		lp.setResults("Test Result");
		lp.allow();
		lp.statusReceived();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		assertEquals(lp.getCommentary(), action.getLabProcedure(id).getCommentary());
		assertEquals(lp.getPid(), action.getLabProcedure(id).getPid());
		assertEquals(lp.getLoinc(), action.getLabProcedure(id).getLoinc());
		assertEquals(lp.getOvID(), action.getLabProcedure(id).getOvID());
		assertEquals(lp.getResults(), action.getLabProcedure(id).getResults());
		assertEquals(lp.getLTID(), action.getLabProcedure(id).getLTID());
		assertEquals(lp.getStatus(), action.getLabProcedure(id).getStatus());
		assertEquals(lp.getRights(), action.getLabProcedure(id).getRights());
	}
	
	/**
	 * testSubmiteResultsWronIDNumberFormat
	 * @throws FormValidationException
	 */
	public void testSubmitResultsWrongIDNumberFormat() throws FormValidationException {
		
		boolean successfulSubmit = action.submitResults("Test", "99", "99", "100", "0");
		assertFalse(successfulSubmit);
	}
	
	/**
	 * testSudmitReceivedWrongIDNubmerFormat
	 * @throws DBException
	 */
	public void testSubmitReceivedWrongIDNumberFormat() throws DBException {
		
		boolean successfulSubmit = action.submitReceived("Test");
		assertFalse(successfulSubmit);
	}
	
	/**
	 * testSubmitResultsEvilFactory
	 * @throws Exception
	 */
	public void testSubmitResultsEvilFactory() throws Exception {
		action = new LabProcLTAction(new EvilDAOFactory());
		LabProcedureBean lp = new LabProcedureBean();
		lp.setLoinc("10763-1");
		lp.setCommentary("");
		lp.setOvID(902L);
		lp.setPid(2L);
		lp.setResults("");
		lp.allow();
		lp.statusReceived();
		lp.setLTID(5000000005L);
		long id = lpDAO.addLabProcedure(lp);
		lp.setProcedureID(id);
		
		assertFalse(action.submitResults(""+id, "12", "grams", "13", "14"));
	}

	public void testLabProceduresFilterFiltering() throws ParseException {
		LabProcedureBean first = new LabProcedureBean();
		first.setPid(0000000001);
		first.setPriorityCode(1);
		first.setProcedureID(10);
		first.setLoinc("12345-6");
		first.statusInTransit();
		first.setCommentary("Their blood is purple and orange.");
		first.setResults("Please call us for your results.");
		first.setOvID(902);
		Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("03/28/2008 12:00");
		first.setTimestamp(new java.sql.Timestamp(date.getTime()));
		first.allow();
		LabProcedureBean second = new LabProcedureBean();
		second.setPid(0000000002);
		second.setProcedureID(20);
		second.setPriorityCode(2);
		second.setLoinc("12345-7");
		second.statusInTransit();
		second.setCommentary("Their blood is purple and orange.");
		second.setResults("Please call us for your results.");
		second.setOvID(10023);
		second.setTimestamp(new java.sql.Timestamp(date.getTime()));
		second.allow();
		List<LabProcedureBean> labProcedures = new ArrayList<>();
		labProcedures.add(first);
		labProcedures.add(second);

		LabProceduresFilter testFilter1 = LabProceduresFilter.builder()
				.labProcedureId(10)
                .labCode("12345-6")
				.build();
		LabProceduresFilter testFilter2 = LabProceduresFilter.builder()
				.labProcedureId(20)
				.labCode("12345-7")
				.build();
		LabProceduresFilter testFilter3 = LabProceduresFilter.builder()
				.hcpName("Kelly Doctor")
				.build();
		LabProceduresFilter testFilter4 = LabProceduresFilter.builder()
				.hcpName("Hehefasdfasdf")
				.build();
		LabProceduresFilter testFilter5 = LabProceduresFilter.builder()
				.priority(1)
				.build();
		LabProceduresFilter testFilter6 = LabProceduresFilter.builder()
				.priority(3)
				.build();
		LabProceduresFilter testFilter7 = LabProceduresFilter.builder()
                .lastUpdatedDateStart("01/01/1111")
				.lastUpdatedDateEnd("01/01/3333")
				.build();
		LabProceduresFilter testFilter8 = LabProceduresFilter.builder()
				.lastUpdatedDateStart("01/01/3333")
				.lastUpdatedDateEnd("01/02/3333")
				.build();
		SoftAssertions softAssert = new SoftAssertions();
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter1)).hasSize(1);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter1).get(0).getLoinc()).isEqualTo("12345-6");
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter1).get(0).getProcedureID()).isEqualTo(10);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter2)).hasSize(1);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter2).get(0).getLoinc()).isEqualTo("12345-7");
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter2).get(0).getProcedureID()).isEqualTo(20);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter3)).hasSize(1);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter3).get(0).getOvID()).isEqualTo(902);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter3).get(0).getProcedureID()).isEqualTo(10);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter4)).hasSize(0);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter5)).hasSize(1);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter6)).hasSize(0);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter7)).hasSize(2);
		softAssert.assertThat(action.filterLabProcedures(labProcedures, testFilter8)).hasSize(0);
		softAssert.assertAll();
	}

	public void testGetLabProceduresFilter() throws ITrustException, SQLException {
		SoftAssertions softAssert = new SoftAssertions();
		softAssert.assertThat(action.getLabProceduresFilter(5000000001L)).isEqualTo("{}");
	}

	public void testSaveLabProceduresFilter() throws ITrustException, SQLException {
		String filterString = "{ \"priority\" : 1, " +
				"\"labProcedureId\" : 200, " +
				"\"lastUpdatedDateStart\" : \"01/01/1111\", " +
				"\"lastUpdatedDateEnd\" : \"01/01/1111\", " +
				"\"labCode\": \"test lab code\", " +
				"\"hcpName\" : \"test hcp name\" " +
				"}";
		action.saveLabProceduresFilter(5000000001L, filterString);
		String victimString = action.getLabProceduresFilter(5000000001L);
		JsonParser parser = new JsonParser();
		JsonElement oracle = parser.parse(filterString);
		JsonElement victim = parser.parse(victimString);
		assertThat(oracle.equals(victim)).isTrue();
	}
}
