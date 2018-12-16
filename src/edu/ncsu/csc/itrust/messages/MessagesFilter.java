package edu.ncsu.csc.itrust.messages;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessagesFilter {
    private String name; // May be sender or recipient depending on whether filtering inbox or outbox
    private String subject;

    // I would typically make this a list, but based on hcp/messageInbox, and ViewMyMessagesAction,
    // it'll get smashed into a single string...
    private String includedWords;
    private String excludedWords;
    private String startDate;
    private String endDate;

    /**
     * Converts this filter object to the format of the filter
     * string used in ViewMyMessagesAction
     *
     * @return
     */
    public String convertToFilterString() {
        String filter = "";
        filter = filter + name + ",";
        filter = filter + subject + ",";
        filter = filter + includedWords + ",";
        filter = filter + excludedWords + ",";
        filter = filter + startDate + ",";
        filter = filter + endDate;
        return filter;
    }

    /**
     * Converts the filter string into a MessageFilter object
     * @param filterString The serialized form of the message filter
     * @return A message filter
     */
    public static MessagesFilter fromFilterString(String filterString) {
        String filterParts[] = filterString.split(",", -1);
        return MessagesFilter.builder()
                .name("" + filterParts[0])
                .subject("" + filterParts[1])
                .includedWords("" + filterParts[2])
                .excludedWords("" + filterParts[3])
                .startDate("" + filterParts[4])
                .endDate("" + filterParts[5])
                .build();

    }
}
