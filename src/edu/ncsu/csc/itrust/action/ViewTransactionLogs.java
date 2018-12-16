package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;

import java.text.SimpleDateFormat;
import java.util.*;

import edu.ncsu.csc.itrust.exception.DBException;

/**
 * A class for UC39 View Transaction Logs
 *
 * ViewTransactionLogs has methods that are necessary for view transaction logs page and result pages (View, Summarize).
 * Using transactionDAO, get transaction logs with given dates and then filter data with logged-in role, secondary role, and transaction type.
 * For Summarize page, methods such as getNumForMonth counts the number of transaction logs that satisfy the given condition with given parameter.
 */
public class ViewTransactionLogs {
    private TransactionDAO transactionDAO;
    private DAOFactory factory;

    /**
     * Set up defaults.
     *
     * @param factory The DAOFactory used to create the DAOs used in this action.
     */
    public ViewTransactionLogs(DAOFactory factory) {
        this.transactionDAO = factory.getTransactionDAO();
        this.factory = factory;
    }

    /**
     * Filter transaction log data with the given period.
     * Returns all transaction logs if lower and upper are null; otherwise, returns filtered list.
     *
     * @param lower The starting date as a java.util.Date
     * @param upper The end date as a java.util.Date
     * @return List of transaction logs filtered by the given period.
     * @throws DBException
     */
    public List<TransactionBean> getTransactionsForAll(java.util.Date lower, java.util.Date upper) throws DBException {
        List<TransactionBean> transactions;
        if (lower == null && upper == null) {
            transactions = transactionDAO.getAllTransactions();
        }
        else {
            transactions = transactionDAO.getTransactionsForDate(lower, upper);
        }
        return transactions;
    }

    /**
     * Filter transaction logs by logged-in role, secondary role, period, and transaction type.
     * Returns filtered list.
     *
     * @param LoggingRole Logged-in role name
     * @param SecondaryRole Secondary role name
     * @param lower The starting date as a java.util.Date
     * @param upper The end date as a java.util.Date
     * @param type Code of Transaction Type
     * @return List of transaction logs filtered by given parameters
     * @throws DBException
     */
    public List<TransactionBean> getRequestTransactions(String LoggingRole, String SecondaryRole, java.util.Date lower, java.util.Date upper, int type) throws DBException{
        List<TransactionBean> preTransactions = this.getTransactionsForAll(lower, upper);
        List<TransactionBean> results = new ArrayList<TransactionBean>();
        for(int i = 0; i < preTransactions.size(); ++i){
            TransactionBean candidate = preTransactions.get(i);
            // get logged-in role as a string and store the value in loggedInRole variable for each transaction log
            String first = transactionDAO.getRole(candidate.getLoggedInMID());
            candidate.setLoggedInRole(first);
            // get secondary role as a string and store the value in secondaryRole variable for each transaction log
            String second = transactionDAO.getRole(candidate.getSecondaryMID());
            candidate.setSecondaryRole(second);
            TransactionType transactionType  = candidate.getTransactionType();
            // if type is -1, it means user chose "all" option for transaction type
            if(type == -1){
                // if LoggingRole is "", it means user chose "all" option for logged-in user role. It means the same for SecondaryRole.
                if((LoggingRole.compareTo("")==0 && SecondaryRole.compareTo("")==0) || (SecondaryRole.compareTo("")==0 && LoggingRole.compareTo(first)==0))
                    results.add(candidate);
                // there are logs in database that their secondary roles are null, so this if statement checks for it since compareTo function does not accept null string as a parameter
                if(second != null){
                    if((LoggingRole.compareTo("")==0 && SecondaryRole.compareTo(second)==0) || (LoggingRole.compareTo(first)==0 && SecondaryRole.compareTo(second)==0)){
                        results.add(candidate);
                    }
                }
            }else{
                if(transactionType.getCode() == type
                        && ((LoggingRole.compareTo("")==0 && SecondaryRole.compareTo("")==0)
                        || (LoggingRole.compareTo("")==0 && SecondaryRole.compareTo(second)==0)
                        || (SecondaryRole.compareTo("")==0 && first.compareTo(LoggingRole)==0)
                        || (first.compareTo(LoggingRole)==0 && SecondaryRole.compareTo(second)==0))){
                    results.add(candidate);
                }
            }
        }
        return results;
    }

    /**
     * Count the number of transaction logs that have the same logged-in role with the role given as a parameter.
     *
     * @param result Transaction log list
     * @param role Role name to be counted
     * @return The number of transaction logs with the specified logged-in role.
     */
    public int getNumForLoggedIn(List<TransactionBean> result, String role){
        int answer = 0;
        for(TransactionBean re: result){
            if(re.getLoggedInRole().compareTo(role)==0){
                answer++;
            }
        }
        return answer;
    }

    /**
     * Count the number of transaction logs that have the same secondary role with the role given as a parameter.
     *
     * @param result Transaction log list
     * @param role Role name to be counted
     * @return The number of transaction logs with the specified secondary role.
     */
    public int getNumForSecondary(List<TransactionBean> result, String role){
        int answer = 0;
        for(TransactionBean re: result){
            if(re.getSecondaryRole() != null && re.getSecondaryRole().compareTo(role)==0){
                answer++;
            }
        }
        return answer;
    }

    /**
     * Count the number of transaction logs that have the same transaction type code with the given parameter.
     *
     * @param result Transaction log list
     * @param code Transaction type code
     * @return The number of transaction logs with the specified transaction type code.
     */
    public int getNumForType(List<TransactionBean> result, int code){
        int answer = 0;
        for(TransactionBean re: result){
            if(re.getTransactionType().getCode() == code){
                answer++;
            }
        }
        return answer;
    }

    /**
     * Counthe number of transaction logs that have the same year and month with the given year and month.
     *
     * @param result Transaction log list
     * @param year 4 digit year
     * @param month  t1-2 digit month
     * @return The number of transaction logs with the specified year and month.
     */
    public int getNumForMonth(List<TransactionBean> result, int year, int month){
        int answer = 0;
        Date date = new Date();
        for(TransactionBean re: result){
            date.setTime(re.getTimeLogged().getTime());
            String str = new SimpleDateFormat("MM/dd/yyyy").format(date);
            if(Integer.parseInt(str.substring(0, 2)) == month && Integer.parseInt(str.substring(6, 10)) == year){
                answer ++;
            }
        }
        return answer;
    }
}
