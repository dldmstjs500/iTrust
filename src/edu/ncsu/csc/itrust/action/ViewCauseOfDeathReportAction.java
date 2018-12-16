package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.CauseOfDeathReportBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewCauseOfDeathReportAction {
    Date lower;
    Date upper;
    long mid;
    String gender;
    OfficeVisitDAO ovDAO;
    PatientDAO patientDAO;

    /**
     * Constructor for the action. Initializes DAO fields
     * @param factory The session's factory for DAOs
     */
    public ViewCauseOfDeathReportAction(DAOFactory factory) {
        //initialize DAOs
        ovDAO = factory.getOfficeVisitDAO();
        patientDAO = factory.getPatientDAO();
    }

    /**
     * Build the report with the specified paramters
     * @param startYear first death year
     * @param endYear last death year
     * @param gender gender as string ("Male, Female, All")
     * @param mid mid to filter by
     * @return report bean with all the data filled out
     * @throws FormValidationException
     * @throws ITrustException
     */
    public CauseOfDeathReportBean getReport(String startYear, String endYear, String gender, long mid) throws SQLException, FormValidationException, ITrustException {
        CauseOfDeathReportBean codBean = new CauseOfDeathReportBean();
        //attempt to parse the date
        this.mid = mid;
        this.gender = gender;
        try {
            lower = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/" + startYear);
            upper = new SimpleDateFormat("MM/dd/yyyy").parse("12/31/" + endYear);
        } catch (ParseException e) {
            throw new FormValidationException("Invalid year entered");
        }
        //check that the before date is before the after date
        if (lower.after(upper)) {
            throw new FormValidationException("Start date must be before end date!");
        }

        //get list of patients; this will be used to filter later
        List<OfficeVisitBean> visits = ovDAO.getAllOfficeVisitsForLHCP(mid);
        // get all office visits
        HashSet<Long> patients = new HashSet<>();
        //save the patient
        for (OfficeVisitBean ovbean : visits)
            patients.add(ovbean.getPatientID());

        // Each string is of the form mid|code|desc
        ArrayList<String> patientCauses;
        // Get Male or Female if asked for
        if(gender.equals("Male") || gender.equals("Female"))
            patientCauses = patientDAO.getCausesOfDeathGender(lower, upper, gender);
        // Otherwise, get all
        else
            patientCauses = patientDAO.getCausesOfDeath(lower, upper);
        // The key for each is the ICD code. The integer values are counts
        HashMap<String, Integer> codeCountTotal = new HashMap<>();
        HashMap<String, Integer> codeCountHCP = new HashMap<>();
        // The string value is the description
        HashMap<String, String> codeToDesc = new HashMap<>();
        // For each cause
        for (String s : patientCauses) {
            //Break into its actual types
           String[] tokens = s.split("\\|");
           long pid = Long.parseLong(tokens[0]);
           String code = tokens[1];
           String desc = tokens[2];
           // Create the entries in the table if they don't exist
            if(!codeCountTotal.containsKey(code))
                codeCountTotal.put(code, 0);
                codeToDesc.put(code, desc);
            // Add the cause of death to the total count
            codeCountTotal.put(code, codeCountTotal.get(code) + 1);
            // if one of the hcp's patient, add it to its count
            if (patients.contains(pid)) {
                if (!codeCountHCP.containsKey(code))
                    codeCountHCP.put(code, 0);
                codeCountHCP.put(code, codeCountHCP.get(code) + 1);
            }
        }
        // Get the most common cause of death among all patients
        if(!codeCountTotal.isEmpty()) {
            String code = Collections.max(codeCountTotal.entrySet(), Map.Entry.comparingByValue()).getKey();
            codBean.setAllcode1(code);
            codBean.setAllcount1(codeCountTotal.get(code));
            codBean.setAllname1(codeToDesc.get(code));
            //remove, so the second most common can be found
            codeCountTotal.remove(code);
        }
        // Get the second most comm cause of death among all patients
        if(!codeCountTotal.isEmpty()) {
            String code = Collections.max(codeCountTotal.entrySet(), Map.Entry.comparingByValue()).getKey();
            codBean.setAllcode2(code);
            codBean.setAllcount2(codeCountTotal.get(code));
            codBean.setAllname2(codeToDesc.get(code));
        }
        // Get the most  common cause of death among the HCP's patients
        if(!codeCountHCP.isEmpty()) {
            String code = Collections.max(codeCountHCP.entrySet(), Map.Entry.comparingByValue()).getKey();
            codBean.setHcpcode1(code);
            codBean.setHcpcount1(codeCountHCP.get(code));
            codBean.setHcpname1(codeToDesc.get(code));
            //remove, so the second most common can be found
            codeCountHCP.remove(code);
        }
        // Get the second most common cause of death among the HCP's patients
        if(!codeCountHCP.isEmpty()) {
            String code = Collections.max(codeCountHCP.entrySet(), Map.Entry.comparingByValue()).getKey();
            codBean.setHcpcode2(code);
            codBean.setHcpcount2(codeCountHCP.get(code));
            codBean.setHcpname2(codeToDesc.get(code));
        }
        //return the report
        return codBean;
    }
}
