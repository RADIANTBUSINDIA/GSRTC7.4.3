package com.gsrtc.mobileweb.models.ticket_cancellation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DivyangHelperPNRDetailsResponse implements Serializable {

    @JsonProperty("DIVYANG_HELPER_PNRNO")
    private String divyangHelperPnrNo;

    @JsonProperty("HELPER_TICKET_ID")
    private String helperTicketId;

    @JsonProperty("IS_DIVYANG_PNRNO")
    private String isDivyangPnrNo;

    @JsonProperty("i_PNRNO")
    private String iPnrNo;

    @JsonProperty("HELPER_SEATNO")
    private String helperSeatNo;

    @JsonProperty("HELPER_PNRID")
    private String helperPnrId;

    public String getDivyangHelperPnrNo() {
        return divyangHelperPnrNo;
    }

    public void setDivyangHelperPnrNo(String divyangHelperPnrNo) {
        this.divyangHelperPnrNo = divyangHelperPnrNo;
    }

    public String getHelperTicketId() {
        return helperTicketId;
    }

    public void setHelperTicketId(String helperTicketId) {
        this.helperTicketId = helperTicketId;
    }

    public String getIsDivyangPnrNo() {
        return isDivyangPnrNo;
    }

    public void setIsDivyangPnrNo(String isDivyangPnrNo) {
        this.isDivyangPnrNo = isDivyangPnrNo;
    }

    public String getiPnrNo() {
        return iPnrNo;
    }

    public void setiPnrNo(String iPnrNo) {
        this.iPnrNo = iPnrNo;
    }

    public String getHelperSeatNo() {
        return helperSeatNo;
    }

    public void setHelperSeatNo(String helperSeatNo) {
        this.helperSeatNo = helperSeatNo;
    }

    public String getHelperPnrId() {
        return helperPnrId;
    }

    public void setHelperPnrId(String helperPnrId) {
        this.helperPnrId = helperPnrId;
    }

    /**
     * Checks if a valid distinct helper PNR is present.
     */
    public boolean hasHelperPnr() {
        return divyangHelperPnrNo != null
                && !divyangHelperPnrNo.trim().isEmpty()
                && !divyangHelperPnrNo.equalsIgnoreCase("?")
                && !divyangHelperPnrNo.equalsIgnoreCase("null")
                && (iPnrNo == null || !divyangHelperPnrNo.trim().equalsIgnoreCase(iPnrNo.trim()));
    }

    /**
     * Retrieves the linked PNR distinct from the currently entered PNR.
     */
    public String getLinkedPnr(String enteredPnr) {
        if (enteredPnr == null) {
            return divyangHelperPnrNo;
        }
        if (divyangHelperPnrNo != null
                && !divyangHelperPnrNo.trim().isEmpty()
                && !divyangHelperPnrNo.equalsIgnoreCase("?")
                && !divyangHelperPnrNo.equalsIgnoreCase("null")
                && !divyangHelperPnrNo.trim().equalsIgnoreCase(enteredPnr.trim())) {
            return divyangHelperPnrNo.trim();
        }
        if (iPnrNo != null
                && !iPnrNo.trim().isEmpty()
                && !iPnrNo.equalsIgnoreCase("?")
                && !iPnrNo.equalsIgnoreCase("null")
                && !iPnrNo.trim().equalsIgnoreCase(enteredPnr.trim())) {
            return iPnrNo.trim();
        }
        return null;
    }

    public boolean hasHelperPnr(String enteredPnr) {
        String linked = getLinkedPnr(enteredPnr);
        return linked != null && !linked.isEmpty();
    }
}
