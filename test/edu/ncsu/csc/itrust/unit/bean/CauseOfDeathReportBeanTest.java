package edu.ncsu.csc.itrust.unit.bean;

import edu.ncsu.csc.itrust.beans.CauseOfDeathReportBean;
import junit.framework.TestCase;

public class CauseOfDeathReportBeanTest extends TestCase {
    CauseOfDeathReportBean bean = new CauseOfDeathReportBean();

    // Test the getters for default values

    /**
     * test default value
     */
    public void testGetAllname1() { assertEquals(bean.getAllname1(), "N/A"); }

    /**
     * test default value
     */
    public void testGetAllname2(){
        assertEquals(bean.getAllname2(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetAllcode1(){
        assertEquals(bean.getAllcode1(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetAllcode2(){
        assertEquals(bean.getAllcode2(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetHcpname1(){
        assertEquals(bean.getHcpname1(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetHcpname2(){
        assertEquals(bean.getHcpname2(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetHcpcode1(){
        assertEquals(bean.getHcpcode1(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetHcpcode2(){
        assertEquals(bean.getHcpcode2(), "N/A");
    }

    /**
     * test default value
     */
    public void testGetAllCount1() {
        assertEquals(bean.getAllcount1(), 0);
    }

    /**
     * test default value
     */
    public void testGetAllCount2(){
        assertEquals(bean.getAllcount2(), 0);
    }

    /**
     * test default value
     */
    public void testGetHcpCount1() {
        assertEquals(bean.getHcpcount1(), 0);
    }

    /**
     * test default value
     */
    public void testGetHcpCount2(){
        assertEquals(bean.getHcpcount2(), 0);
    }
}
