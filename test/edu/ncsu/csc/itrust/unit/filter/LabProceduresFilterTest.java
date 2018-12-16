package edu.ncsu.csc.itrust.unit.filter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.filters.LabProceduresFilter;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LabProceduresFilterTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testPartiallyFilledLabProceduresFilter() {
        LabProceduresFilter testFilter = LabProceduresFilter.builder()
                .priority(1)
                .labProcedureId(200)
                .labCode("test lab code")
                .build();
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getPriority()).as("Priority to filter for").isEqualTo(1);
        softAssert.assertThat(testFilter.getLabProcedureId()).as("Lab procedure id").isEqualTo(200);
        softAssert.assertThat(testFilter.getLabCode()).as("Lab code").isEqualTo("test lab code");
        softAssert.assertThat(testFilter.getHcpName()).as("HCP name in filter when none specified").isEmpty();
        softAssert.assertThat(testFilter.getLastUpdatedDateStart()).as("Empty last-updated-date start").isEmpty();
        softAssert.assertThat(testFilter.getLastUpdatedDateEnd()).as("Empty last-updated-date end").isEmpty();
        softAssert.assertAll();
    }

    @Test
    public void testFullyFilledLabProceduresFilter() {
        LabProceduresFilter testFilter = LabProceduresFilter.builder()
                .priority(1)
                .labProcedureId(200)
                .lastUpdatedDateEnd("01/01/1111")
                .lastUpdatedDateStart("01/01/1111")
                .labCode("test lab code")
                .hcpName("test hcp name")
                .build();
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getPriority()).as("Priority to filter for").isEqualTo(1);
        softAssert.assertThat(testFilter.getLabProcedureId()).as("Lab procedure id").isEqualTo(200);
        softAssert.assertThat(testFilter.getLabCode()).as("Lab code").isEqualTo("test lab code");
        softAssert.assertThat(testFilter.getHcpName()).as("HCP name").isEqualTo("test hcp name");
        softAssert.assertThat(testFilter.getLastUpdatedDateStart()).as("Last updated date start").isEqualTo("01/01/1111");
        softAssert.assertThat(testFilter.getLastUpdatedDateEnd()).as("Last updated date end").isEqualTo("01/01/1111");
        softAssert.assertAll();
    }

    @Test
    public void testFullyFilledFilterStringToLabProceduresFilter() {
        String jsonString = "{ \"priority\" : 1, " +
                "\"labProcedureId\" : 200, " +
                "\"lastUpdatedDateStart\" : \"01/01/1111\", " +
                "\"lastUpdatedDateEnd\" : \"01/01/1111\", " +
                "\"labCode\": \"test lab code\", " +
                "\"hcpName\" : \"test hcp name\" " +
                "}";
        LabProceduresFilter testFilter = LabProceduresFilter.fromJsonString(jsonString);
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getPriority()).as("Priority to filter for").isEqualTo(1);
        softAssert.assertThat(testFilter.getLabProcedureId()).as("Lab procedure id").isEqualTo(200);
        softAssert.assertThat(testFilter.getLabCode()).as("Lab code").isEqualTo("test lab code");
        softAssert.assertThat(testFilter.getHcpName()).as("HCP name").isEqualTo("test hcp name");
        softAssert.assertThat(testFilter.getLastUpdatedDateStart()).as("Last updated date start").isEqualTo("01/01/1111");
        softAssert.assertThat(testFilter.getLastUpdatedDateEnd()).as("Last updated date end").isEqualTo("01/01/1111");
        softAssert.assertAll();
    }

    @Test
    public void testPartiallyFilledFilterStringToLabProceduresFilter() {
        String jsonString = "{ \"priority\" : 1, " +
                "\"labProcedureId\" : 200 " +
                "}";
        LabProceduresFilter testFilter = LabProceduresFilter.fromJsonString(jsonString);
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getPriority()).as("Priority to filter for").isEqualTo(1);
        softAssert.assertThat(testFilter.getLabProcedureId()).as("Lab procedure id").isEqualTo(200);
        softAssert.assertThat(testFilter.getLabCode()).as("Lab code").isEmpty();
        softAssert.assertThat(testFilter.getHcpName()).as("HCP name").isEmpty();
        softAssert.assertThat(testFilter.getLastUpdatedDateStart()).as("Last updated date start").isEmpty();
        softAssert.assertThat(testFilter.getLastUpdatedDateEnd()).as("Last updated date end").isEmpty();
        softAssert.assertAll();
    }

    @Test
    public void testLabProceduresFilterToString() {
        String jsonString = "{ \"priority\" : 1, " +
                "\"labProcedureId\" : 200, " +
                "\"lastUpdatedDateStart\" : \"01/01/1111\", " +
                "\"lastUpdatedDateEnd\" : \"01/01/1111\", " +
                "\"labCode\": \"test lab code\", " +
                "\"hcpName\" : \"test hcp name\" " +
                "}";
        LabProceduresFilter testFilter = LabProceduresFilter.builder()
                .priority(1)
                .labProcedureId(200)
                .lastUpdatedDateEnd("01/01/1111")
                .lastUpdatedDateStart("01/01/1111")
                .labCode("test lab code")
                .hcpName("test hcp name")
                .build();
        String victimString = testFilter.toJsonString();
        JsonParser parser = new JsonParser();
        JsonElement oracle = parser.parse(jsonString);
        JsonElement victim = parser.parse(victimString);
        assertThat(oracle.equals(victim)).isTrue();
    }

    @Test
    public void testLabProceduresFilterHandleLombok() {
        assertThat(LabProceduresFilter.builder().toString())
            .isNotNull()
            .isNotEmpty();
    }

}
