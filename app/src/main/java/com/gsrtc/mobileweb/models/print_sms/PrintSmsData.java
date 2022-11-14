package com.gsrtc.mobileweb.models.print_sms;


import com.gsrtc.mobileweb.models.booking_history.GetTrackTicketDetails;

import java.io.Serializable;

/**
 * Created by Ajith on 3/12/2019.
 */

public class PrintSmsData implements Serializable {

    public PrintSmsParam printSmsParam;
    public GetPrintSmsDetailslResponse[] getPrintSmsDetailslResponses;
    public GetTrackTicketDetails[] getTrackTicketDetails;
}
