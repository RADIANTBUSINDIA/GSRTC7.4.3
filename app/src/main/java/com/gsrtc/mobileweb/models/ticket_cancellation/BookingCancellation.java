package com.gsrtc.mobileweb.models.ticket_cancellation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Swastik on 5/8/2017.
 */

public class BookingCancellation implements Serializable {
    public CancellationSearchParams cancellationSearchParams;
    public List<GetAllPassengerDetailsByPnrNoResponse> selectedPassengers;  //Selected Passengers
    public boolean isPartialCancellation;
    public GetAllPassengerDetailsByPnrNoResponse[] getAllPassengerDetailsByPnrNoResponse;
    public ConfirmFullCancellationResponse confirmFullCancellationResponse;
    public GetFullCancellationDetailsResponse getFullCancellationDetailsResponse;
    public GetFullCancellationTransactionDetailsResponse getFullCancellationTransactionDetailsResponse;

    public ConfirmPartialCancellationResponse confirmPartialCancellationResponse;
    public GetPartialCancellationDetailsResponse getPartialCancellationDetailsResponse;
    public GetPartialCancellationTransactionDetailsResponse getPartialCancellationTransactionDetailsResponse;

    // Divyang & Helper linked PNR cancellation fields
    public boolean isDivyangWithHelper;
    public DivyangHelperPNRDetailsResponse divyangHelperDetails;
    public GetFullCancellationDetailsResponse helperFullCancellationDetailsResponse;
    public GetFullCancellationTransactionDetailsResponse helperFullCancellationTransactionDetailsResponse;
    public ConfirmFullCancellationResponse helperConfirmFullCancellationResponse;
}
