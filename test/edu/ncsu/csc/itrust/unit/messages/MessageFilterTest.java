package edu.ncsu.csc.itrust.unit.messages;

import edu.ncsu.csc.itrust.messages.MessagesFilter;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageFilterTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testPartiallyFilledMessageFilter() {
        // Filter that does has only a few of the fields filled out.
        MessagesFilter testFilter = MessagesFilter.builder()
                .name("testName")
                .subject("testSubject")
                .includedWords("")
                .excludedWords("excluded words")
                .startDate("")
                .endDate("")
                .build();
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getName()).as("Name to filter against") .isEqualTo("testName");
        softAssert.assertThat(testFilter.getSubject()).as("Subject to filter against").isEqualTo("testSubject");
        softAssert.assertThat(testFilter.getExcludedWords()).as("Excluded words").isEqualTo("excluded words");
        softAssert.assertThat(testFilter.getIncludedWords()).as("Included words").isEmpty();
        softAssert.assertThat(testFilter.getStartDate()).as("Start Date").isEmpty();
        softAssert.assertThat(testFilter.getEndDate()).as("End Date").isEmpty();
        softAssert.assertThat(testFilter.convertToFilterString()).as("Filter to Filter String Conversion")
                .isEqualTo("testName,testSubject,,excluded words,,");
        softAssert.assertAll();
    }

    @Test
    public void testFullyFilledMessageFilter() {
        MessagesFilter testFilter = MessagesFilter.builder()
                .name("testName")
                .subject("testSubject")
                .includedWords("included words")
                .excludedWords("excluded words")
                .startDate("01/01/1111")
                .endDate("02/02/2222")
                .build();
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getName()).as("Name to filter against") .isEqualTo("testName");
        softAssert.assertThat(testFilter.getSubject()).as("Subject to filter against").isEqualTo("testSubject");
        softAssert.assertThat(testFilter.getExcludedWords()).as("Excluded words").isEqualTo("excluded words");
        softAssert.assertThat(testFilter.getIncludedWords()).as("Included words").isEqualTo("included words");
        softAssert.assertThat(testFilter.getStartDate()).as("Start Date").isEqualTo("01/01/1111");
        softAssert.assertThat(testFilter.getEndDate()).as("End Date").isEqualTo("02/02/2222");
        softAssert.assertThat(testFilter.convertToFilterString()).as("Filter to Filter String Conversion")
                .isEqualTo("testName,testSubject,included words,excluded words,01/01/1111,02/02/2222");
        softAssert.assertAll();
    }

    @Test
    public void testFullFilterStringToMessageFilter() {
        String testFilterString = "testName,testSubject,included words,excluded words,01/01/1111,02/02/2222";
        MessagesFilter testFilter = MessagesFilter.fromFilterString(testFilterString);
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getName()).as("Name to filter against") .isEqualTo("testName");
        softAssert.assertThat(testFilter.getSubject()).as("Subject to filter against").isEqualTo("testSubject");
        softAssert.assertThat(testFilter.getExcludedWords()).as("Excluded words").isEqualTo("excluded words");
        softAssert.assertThat(testFilter.getIncludedWords()).as("Included words").isEqualTo("included words");
        softAssert.assertThat(testFilter.getStartDate()).as("Start Date").isEqualTo("01/01/1111");
        softAssert.assertThat(testFilter.getEndDate()).as("End Date").isEqualTo("02/02/2222");
        softAssert.assertThat(testFilter.convertToFilterString()).as("Filter string to MessageFilter back to Filter String")
            .isEqualTo(testFilterString);
        softAssert.assertAll();
    }

    @Test
    public void testPartialFilterStringToMessageFilter() {
        String testFilterString = "testName,,included words,excluded words,01/01/1111,";
        MessagesFilter testFilter = MessagesFilter.fromFilterString(testFilterString);
        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(testFilter.getName()).as("Name to filter against") .isEqualTo("testName");
        softAssert.assertThat(testFilter.getSubject()).as("Subject to filter against").isEmpty();
        softAssert.assertThat(testFilter.getExcludedWords()).as("Excluded words").isEqualTo("excluded words");
        softAssert.assertThat(testFilter.getIncludedWords()).as("Included words").isEqualTo("included words");
        softAssert.assertThat(testFilter.getStartDate()).as("Start Date").isEqualTo("01/01/1111");
        softAssert.assertThat(testFilter.getEndDate()).as("End Date").isEmpty();
        softAssert.assertThat(testFilter.convertToFilterString()).as("Filter string to MessageFilter back to Filter String")
                .isEqualTo(testFilterString);
        softAssert.assertAll();
    }

    @Test
    public void testMessageFilterHandleLombok() {
        // Apparently, Lombok's generated functions aren't ignored in Jacoco 0.7.9 but rather
        // 0.7.10, which isn't available yet. Running this to appease Jacoco regarding the
        // generated methods.
        assertThat(MessagesFilter.builder().toString())
                .isNotNull()
                .isNotEmpty();
    }
}
