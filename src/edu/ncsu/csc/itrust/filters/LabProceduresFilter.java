package edu.ncsu.csc.itrust.filters;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
/**
 * Filter for lab procedures representing the various fields that are available
 * to use when filtering a list of lab procedures.
 */
public class LabProceduresFilter {
    private long priority;
    private long labProcedureId;

    @Builder.Default private String lastUpdatedDateStart = "";
    @Builder.Default private String lastUpdatedDateEnd = "";
    @Builder.Default private String labCode = "";
    @Builder.Default private String hcpName = "";

    /**
     * Converts this LabProceduresFilter into a JSON string for storage.
     *
     * @return
     */
    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Create a LabProceduresFilter based on the stringified (JSON format)
     * format of one.
     *
     * @param stringified The JSON string representing a LabProceduresFilter
     * @return The LabProceduresFilter
     */
    public static LabProceduresFilter fromJsonString(String stringified) {
        Gson gson = new Gson();
        LabProceduresFilter filter = gson.fromJson(stringified, LabProceduresFilter.class);
        if (filter.lastUpdatedDateStart == null)
            filter.lastUpdatedDateStart = "";
        if (filter.lastUpdatedDateEnd == null)
            filter.lastUpdatedDateEnd = "";
        if (filter.labCode == null)
            filter.labCode = "";
        if (filter.hcpName == null)
            filter.hcpName = "";
        return filter;
    }
}
