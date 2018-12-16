package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import org.junit.Test;

public class TransactionBeanTest extends TestCase{

    @Test
    public void testTransactionBean() throws Exception {
        TransactionBean b = new TransactionBean();

        // test newly added setters
        b.setLoggedInRole("admin");
        b.setSecondaryRole("admin");

        assertEquals(0, b.getTransactionID());
        // confirm with getters
        assertEquals("admin", b.getLoggedInRole());
        assertEquals("admin", b.getSecondaryRole());
    }
}
