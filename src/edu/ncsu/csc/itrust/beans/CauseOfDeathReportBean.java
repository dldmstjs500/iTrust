package edu.ncsu.csc.itrust.beans;

/**
 * Stores the cause of death data. Needs to store the top two causes of death (name, code, count)
 * for all doctors and the hcp with the specified id
 * Also filters by date range and gender (all or one)
 */
public class CauseOfDeathReportBean {
    // First cause of death for all patients
    String allname1 = "N/A";
    String allcode1 = "N/A";
    int allcount1 = 0;

    // Second cause of death for all patients
    String allname2 = "N/A";
    String allcode2 = "N/A";
    int allcount2 = 0;

    // First cause of death for specified hcp
    String hcpname1 = "N/A";
    String hcpcode1 = "N/A";
    int hcpcount1 = 0;

    // Second cause of death for specified hcp
    String hcpname2 = "N/A";
    String hcpcode2 = "N/A";
    int hcpcount2 = 0;

    /**
     * Getter
     * @return Name of icdcode
     */
    public String getAllname1() { return allname1; }

    /**
     * Setter
     * @param allname1 Name of icdcode
     */
    public void setAllname1(String allname1) {
        this.allname1 = allname1;
    }

    /**
     * Getter
     * @return icdcode
     */
    public String getAllcode1() {
        return allcode1;
    }

    /**
     * Setter
     * @param allcode1 icdcode
     */
    public void setAllcode1(String allcode1) { this.allcode1 = allcode1; }

    /**
     * Getter
     * @return number of cases
     */
    public int getAllcount1() {
        return allcount1;
    }

    /**
     * Setter
     * @param allcount1 number of cases
     */
    public void setAllcount1(int allcount1) {
        this.allcount1 = allcount1;
    }

    /**
     * Getter
     * @return Name of icdcode
     */
    public String getAllname2() {
        return allname2;
    }

    /**
     * Setter
     * @param allname2 Name of icdcode
     */
    public void setAllname2(String allname2) {
        this.allname2 = allname2;
    }

    /**
     * Getter
     * @return icdcode
     */
    public String getAllcode2() {
        return allcode2;
    }

    /**
     * Setter
     * @param allcode2 icdcode
     */
    public void setAllcode2(String allcode2) {
        this.allcode2 = allcode2;
    }

    /**
     * Getter
     * @return number of cases
     */
    public int getAllcount2() {
        return allcount2;
    }

    /**
     * Setter
     * @param allcount2 number of cases
     */
    public void setAllcount2(int allcount2) {
        this.allcount2 = allcount2;
    }

    /**
     * Getter
     * @return Name of icdcode
     */
    public String getHcpname1() {
        return hcpname1;
    }

    /**
     * Setter
     * @param hcpname1 Name of icdcode
     */
    public void setHcpname1(String hcpname1) {
        this.hcpname1 = hcpname1;
    }

    /**
     * Getter
     * @return icdcode
     */
    public String getHcpcode1() {
        return hcpcode1;
    }

    /**
     * Setter
     * @param hcpcode1 icdcode
     */
    public void setHcpcode1(String hcpcode1) {
        this.hcpcode1 = hcpcode1;
    }

    /**
     * Getter
     * @return number of cases
     */
    public int getHcpcount1() { return hcpcount1; }

    /**
     * Setter
     * @param hcpcount1 number of cases
     */
    public void setHcpcount1(int hcpcount1) {
        this.hcpcount1 = hcpcount1;
    }

    /**
     * Getter
     * @return Name of icdcode
     */
    public String getHcpname2() {
        return hcpname2;
    }

    /**
     * Setter
     * @param hcpname2 Name of icdcode
     */
    public void setHcpname2(String hcpname2) {
        this.hcpname2 = hcpname2;
    }

    /**
     * Getter
     * @return icdcode
     */
    public String getHcpcode2() {
        return hcpcode2;
    }

    /**
     * Setter
     * @param hcpcode2 icdcode
     */
    public void setHcpcode2(String hcpcode2) {
        this.hcpcode2 = hcpcode2;
    }

    /**
     * Getter
     * @return number of cases
     */
    public int getHcpcount2() {
        return hcpcount2;
    }

    /**
     * Setter
     * @param hcpcount2 number of cases
     */
    public void setHcpcount2(int hcpcount2) {
        this.hcpcount2 = hcpcount2;
    }
}
