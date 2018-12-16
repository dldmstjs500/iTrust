package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.filters.LabProceduresFilter;

/**
 * Class for LabProcLT.jsp.  Handles lab procedures for LTs
 */
public class LabProcLTAction {
	private LabProcedureDAO lpDAO;
	private OfficeVisitDAO ovDAO;
	private PersonnelDAO personDAO;
	/**
 * Setup 
 * @param factory The DAOFactory used to create the DAOs used in this action.
 */
	public LabProcLTAction(DAOFactory factory) {
		ovDAO = factory.getOfficeVisitDAO();
		lpDAO = factory.getLabProcedureDAO();
		personDAO = factory.getPersonnelDAO();
	}
	
	public List<LabProcedureBean> viewInTransitProcedures(long id) throws DBException{
		return lpDAO.getLabProceduresInTransitForLabTech(id);
	}
	
	public List<LabProcedureBean> viewReceivedProcedures(long id) throws DBException{
		return lpDAO.getLabProceduresReceivedForLabTech(id);
	}
	
	public List<LabProcedureBean> viewTestingProcedures(long id) throws DBException{
		return lpDAO.getLabProceduresTestingForLabTech(id);
	}
	
	public LabProcedureBean getLabProcedure(long id) throws DBException{
		return lpDAO.getLabProcedure(id);
	}

	public String getLabProceduresFilter(long id) throws DBException, ITrustException, SQLException {
	    return personDAO.getLabProceduresFilter(id);
	}

	public void saveLabProceduresFilter(long id, String filterJson) throws DBException, SQLException {
		personDAO.saveLabProceduresFilter(id, filterJson);
	}

	public String getHCPName (long ovid) throws ITrustException {
		OfficeVisitBean b = ovDAO.getOfficeVisit(ovid);
		return personDAO.getName(b.getHcpID());
	}
	
	public Boolean submitResults(String id, String numericalResults, String numericalResultsUnit, String upperBound, String lowerBound) throws FormValidationException {
			try {
				long procedureID = Long.parseLong(id);
				LabProcedureBean lp = lpDAO.getLabProcedure(procedureID);
				lp.setNumericalResult(numericalResults);
				lp.setNumericalResultUnit(numericalResultsUnit);
				lp.setUpperBound(upperBound);
				lp.setLowerBound(lowerBound);
				lpDAO.submitTestResults(Long.parseLong(id), numericalResults, numericalResultsUnit, upperBound, lowerBound);
			} catch (NumberFormatException e) {
				
				return false;
			} catch (DBException e) {
				
				return false;
			}
			return true;
	}
	
	public Boolean submitReceived(String id) throws DBException{
		try {
			lpDAO.submitReceivedLP(Long.parseLong(id));
		} catch (NumberFormatException e) {
			
			return false;
		}
		return true;
	}
	
	public Boolean setToTesting(long id) throws DBException {
		lpDAO.setLPToTesting(id);
		return true;
	}

	/**
	 * Filters the list of lab procedures based on the provided lab procedures filter
	 *
	 * @param labProcedures The list of lab procedures to filter
	 * @param filter The filter containing the information used to filter the list of procedures
	 * @return The filtered list of procedures.
	 * @throws ParseException
	 */
	public List<LabProcedureBean> filterLabProcedures(List<LabProcedureBean> labProcedures, LabProceduresFilter filter) throws ParseException {
		Date start = null, end = null;
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		if (!"".equals(filter.getLastUpdatedDateStart()))
			start = format.parse(filter.getLastUpdatedDateStart());
		if (!"".equals(filter.getLastUpdatedDateEnd()))
			end = format.parse(filter.getLastUpdatedDateEnd());
		Date finalStart = start;
		Date finalEnd = end;
		// Multiple filters are slower, but easier to read. Num lab procedures too small to matter.
		return labProcedures.stream()
				.filter(procedure -> filter.getPriority() <= 0 || procedure.getPriorityCode() == filter.getPriority())
				.filter(procedure -> filter.getLabProcedureId() <= 0 || procedure.getProcedureID() == filter.getLabProcedureId())
				.filter(procedure -> finalStart == null || procedure.getTimestamp().after(finalStart))
				.filter(procedure -> finalEnd == null || procedure.getTimestamp().before(finalEnd))
				.filter(procedure -> "".equals(filter.getLabCode()) || procedure.getLoinc().equals(filter.getLabCode()))
				.filter(procedure -> {
					if ("".equals(filter.getHcpName()))
						return true;
					try {
						String hcpName = getHCPName(procedure.getOvID());
						return hcpName.toLowerCase().equals(filter.getHcpName().toLowerCase());
					} catch (Exception e) {
					    return false;
					}
				})
				.collect(Collectors.toList());
	}
}
