package com.gsrtc.mobileweb.ui.activities.current_booking_native;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.BusSummaryDetails;
import com.gsrtc.mobileweb.models.SaveCurrentBookingDetailsTemporarilyResponse;
import com.gsrtc.mobileweb.models.SearchParamsCurrent;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.SoapClientUtil;
import com.gsrtc.mobileweb.utils.ValidationUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;

/**
 * Created by mail4 on 30-12-2017.
 */

public class GetCurrentSummaryActivity extends BaseNavigationDrawerActivity{

    SearchParamsCurrent searchParams;
    ActivityViewHolder vHolder;
    boolean dataFetched = false;
    BusSummaryDetails busSummaryDetails;
    private String isCard;
    String textlang;
    SaveCurrentBookingDetailsTemporarilyResponse saveTempDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.get_current_summary);

        Log.e("Summary ::", "OnCreate");

        if(getIntent()!=null && getIntent().getExtras()!=null){

            searchParams = (SearchParamsCurrent) getIntent().getExtras().getSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM);
            textlang =searchParams.getCrntlanguage().trim();
        }

        setupViews();
    }

    public void setupViews() {
        vHolder = new ActivityViewHolder(this);

        if(!dataFetched){
            vHolder.loadingLayout.setVisibility(View.VISIBLE);
            vHolder.scrollViewPassengerInfo.setVisibility(View.GONE);
        }
        fetchSummaryDetails();
    }


    public void fetchSummaryDetails(){

        //CustomisedProgressDialog.SHOW_CUST_DIA(GetCurrentSummaryActivity.this, "please wait loding....");

        SoapClientUtil.getSummaryDetails(searchParams, new SoapClientUtil.GetCurrentTotalFareDetailsOfTicketCallback() {

            @Override
            public void onSuccess(BusSummaryDetails summaryDetails) {
                busSummaryDetails = summaryDetails;

                vHolder.loadingLayout.setVisibility(View.GONE);
                vHolder.scrollViewPassengerInfo.setVisibility(View.VISIBLE);

                vHolder.textViewDate.setText(busSummaryDetails.getJourneyDate());
                //searchParams.getFromLocation().getPlaceName() + " to " + searchParams.getToLocation().getPlaceName()
                vHolder.textViewFrom.setText(searchParams.getFromLocation().getPlaceName());
                vHolder.textViewTo.setText(searchParams.getToLocation().getPlaceName());
                vHolder.textViewService.setText(searchParams.getServiceRouteName());
                vHolder.textViewNoSeats.setText(String.valueOf(searchParams.getAdultCount() + searchParams.getChildrenCount()));


                vHolder.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        RadioButton button = (RadioButton) group.findViewById(checkedId);
                        if (button.getText().toString().equalsIgnoreCase("ICICI")) {

                            isCard = "733";

                        } else if(button.getText().toString().equalsIgnoreCase("PayGov")) {

                            isCard = "724";

                        }else if(button.getText().toString().equalsIgnoreCase("AggrePay")) {

                            isCard = "721";
                        }else{

                            isCard = "722";

                        }
                    }
                });
                vHolder.radio.check(R.id.paytm);
                Log.e("AccidentFund", busSummaryDetails.getAccidentReliefFund());

                searchParams.setAccidentReliefFund_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAccidentReliefFund());
                searchParams.setAdultFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAdultFare());
                searchParams.setAdultFemale_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAdultFemale());
                searchParams.setAdultMale_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAdultMale());
                searchParams.setAdultMales_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAdultMales());
                searchParams.setAdultTicketFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getAdultTicketFare());
                searchParams.setBasicFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getBasicFare());
                searchParams.setBridgeFee_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getBridgeFee());
                searchParams.setChildFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getChildFare());
                searchParams.setChildFemale_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getChildFemale());
                searchParams.setChildMale_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getChildMale());
                searchParams.setChildMales_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getChildMales());
                searchParams.setChildTicketFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getChildTicketFare());
                searchParams.setClassId_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getClassId());
                searchParams.setConcessionID_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getConcessionID());
                searchParams.setConcessionPercentage_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getConcessionPercentage());
                searchParams.setConcessionTypeId_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getConcessionTypeId());
                searchParams.setCounterCode_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getCounterCode());
                searchParams.setCreatedBy_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getCreatedBy());
                searchParams.setDiscountPercentage_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getDiscountPercentage());
                searchParams.setDiscounts_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getDiscounts());
                searchParams.setEndPlaceID_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getEndPlaceID());
                searchParams.setEntryFee_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getEntryFee());
                searchParams.setFareWithOutbasic_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getFareWithOutbasic());
                searchParams.setJourneyDate_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getJourneyDate());
                searchParams.setMessageFromCorp_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getMessageFromCorp());
                searchParams.setOnewayOrReturnTrip_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getOnewayOrReturnTrip());
                searchParams.setOnlineDifferenceFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getOnlineDifferenceFare());
                searchParams.setOtherLevies_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getOtherLevies());
                searchParams.setPickServiceId_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getPickServiceId());
                searchParams.setReservationFee_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getReservationFee());
                searchParams.setStartPlaceID_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getStartPlaceID());
                searchParams.setStatus_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getStatus());
                searchParams.setTollFee_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getTollFee());
                searchParams.setTotalAmount_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getTotalAmount());
                searchParams.setTotalFare_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getTotalFare());
                searchParams.setTotalNumberOfSeats_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getTotalNumberOfSeats());
                searchParams.setUserFee_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getUserFee());
                searchParams.setUserName_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getUserName());
                searchParams.setWeekdayConcession_GetCurrentTotalFareDetailsOfTicket(busSummaryDetails.getWeekdayConcession());

                vHolder.btnSaveTempButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(TextUtils.isEmpty(vHolder.editTextPassengerName.getText().toString()) && TextUtils.isEmpty(vHolder.editTextPassengerEmail.getText().toString()) && TextUtils.isEmpty(vHolder.editTextPassengerMobile.getText().toString())){
                            ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please enter Passenger Details");
                            return;
                        }else {

                            if(!ValidationUtil.isValidEmail(vHolder.editTextPassengerEmail.getText())){
                                ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please enter valid Email");
                                return;
                            }
                            else if(vHolder.editTextPassengerMobile.getText().length()==13)
                            {
                                String mobnum=vHolder.editTextPassengerMobile.getText().toString();

                                if (mobnum.startsWith("+"))
                                {
                                        String str_getMOBILE = mobnum.substring(3);
                                        vHolder.editTextPassengerMobile.setText(str_getMOBILE);
                                }
                                else
                                {
                                    ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please enter valid Mobile Number");
                                    return;
                                }
                            }
                            else if(vHolder.editTextPassengerMobile.getText().length() != 10){
                                ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please enter valid Mobile Number");
                                return;
                            }
                            else if(!ValidationUtil.isValidPhone(vHolder.editTextPassengerMobile.getText())){
                                ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please enter valid Mobile Number");
                                return;
                            }

                                searchParams.setPassengerName(vHolder.editTextPassengerName.getText().toString());
                                searchParams.setPassengerEmail(vHolder.editTextPassengerEmail.getText().toString());
                                searchParams.setPassengerMobile(vHolder.editTextPassengerMobile.getText().toString());

                            if(isCard!=null)
                            {
                                saveDetailsTemp(isCard);
                            }
                            else
                            {
                                    ActivityUtil.showToast(GetCurrentSummaryActivity.this, "Please select payment option");
                                    return;
                            }

                        }
                    }
                });

                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
            }

            @Override
            public void onError(Exception e) {
                ActivityUtil.showErrorToast(GetCurrentSummaryActivity.this, e);

            }
        });
    }


    public void saveDetailsTemp(String payType){

        showLoading(true);
        CustomisedProgressDialog.SHOW_CUST_DIA(GetCurrentSummaryActivity.this, "Please wait loading....");
        SoapClientUtil.saveTempDetails(searchParams,payType, new SoapClientUtil.SaveCurrentBookingDetailsTemporarilyCallback() {

            @Override
            public void onSuccess(SaveCurrentBookingDetailsTemporarilyResponse saveTempDetailsRES) {

                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();

                saveTempDetails = saveTempDetailsRES;

                searchParams.setAdultTicketFareSaveCurrentBookingDetailsTemporarilyResponse(saveTempDetailsRES.getAdultTicketFare());
                searchParams.setChildTicketFareSaveCurrentBookingDetailsTemporarilyResponse(saveTempDetailsRES.getChildTicketFare());
                searchParams.setGsrtcRefNoSaveCurrentBookingDetailsTemporarilyResponse(saveTempDetailsRES.getKsrtcRefNo());
                searchParams.setPnrMasterIDSaveCurrentBookingDetailsTemporarilyResponse(saveTempDetailsRES.getPnrMasterID());
                searchParams.setPnrNumberSaveCurrentBookingDetailsTemporarilyResponse(saveTempDetailsRES.getPnrNumber());
                searchParams.setGatewayLookupId(isCard);
//                if (isCard) {
//                    searchParams.setGatewayBILLDESK("BILLDESK");
//                } else {
//                    searchParams.setGatewayBILLDESK("HDFC");
//                }

                Bundle extra = new Bundle();
                extra.putString("textlang", textlang);
                extra.putString("name",vHolder.editTextPassengerName.getText().toString());
                extra.putString("mail",vHolder.editTextPassengerEmail.getText().toString());
                extra.putString("mobile",vHolder.editTextPassengerMobile.getText().toString());
                extra.putSerializable(AppConstants.INTENT_EXTRA.SEARCH_PARAM, searchParams);
                ActivityUtil.openNewActivity(GetCurrentSummaryActivity.this, CurrentReferenceNumberActivity.class, extra);
            }

            @Override
            public void onError(Exception e) {
                showLoading(false);
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(GetCurrentSummaryActivity.this, e);

            }
        });
    }

    public class ActivityViewHolder{
        View rootView, loadingLayout;
        ScrollView scrollViewPassengerInfo;
        TextView textViewDate;
        TextView textViewFrom;
        TextView textViewTo;
        TextView textViewService;
        TextView textViewNoSeats;
        TextView txtdate,txtfrom,txtto,txtservice,txtseats,txtcredit,txtnetbank,txtheader,txtsub_header;
        Button btnSaveTempButton;
        EditText editTextPassengerName;
        EditText editTextPassengerEmail;
        EditText editTextPassengerMobile;
        RadioGroup radio;
        RadioButton paygov;
        RadioButton phonepe;
        RadioButton aggrepay;
        RadioButton paytm;

        ActivityViewHolder(Activity activity){
            rootView = activity.findViewById(R.id.rootView);
            loadingLayout =  activity.findViewById(R.id.search_results_loading);
            scrollViewPassengerInfo = (ScrollView) activity.findViewById(R.id.scrollViewPassengerSummary);
            textViewDate = (TextView) activity.findViewById(R.id.tvSummaryDateValue);
            textViewFrom = (TextView) activity.findViewById(R.id.tvSummaryFromValue);
            textViewTo = (TextView) activity.findViewById(R.id.tvSummaryToValue);
            textViewService = (TextView) activity.findViewById(R.id.tvSummaryServiceValue);
            textViewNoSeats = (TextView) activity.findViewById(R.id.tvSummaryNoOfSeatsValue);
            editTextPassengerName = (EditText) activity.findViewById(R.id.edt_passenger_name);
            editTextPassengerEmail = (EditText) activity.findViewById(R.id.edtEmail);
            editTextPassengerMobile = (EditText) activity.findViewById(R.id.edtMobileNumber);
            radio = (RadioGroup) activity.findViewById(R.id.radio);
            paygov = (RadioButton) activity.findViewById(R.id.paygov);
            phonepe = (RadioButton) activity.findViewById(R.id.phonepe);
            aggrepay = (RadioButton) activity.findViewById(R.id.aggrepay);
            paytm = (RadioButton) activity.findViewById(R.id.paytm);
            btnSaveTempButton = (Button) activity.findViewById(R.id.btnConfirmSummary);

            txtdate = (TextView) activity.findViewById(R.id.tvSummaryDateHeading);
            txtfrom = (TextView) activity.findViewById(R.id.tvSummaryFromHeading);
            txtto = (TextView) activity.findViewById(R.id.tvSummaryToHeading);
            txtservice = (TextView) activity.findViewById(R.id.tvSummaryServiceHeading);
            txtseats = (TextView) activity.findViewById(R.id.tvSummaryNoOfSeatsHeading);
            txtheader = (TextView) activity.findViewById(R.id.title_header);
            txtsub_header = (TextView) activity.findViewById(R.id.sub_header);
//            txtcredit = (TextView) activity.findViewById(R.id.credit);
//            txtnetbank = (TextView) activity.findViewById(R.id.net_bank);

            if(textlang.equalsIgnoreCase("gujarati"))
            {
                editTextPassengerName.setHint(R.string.crtpas_pass_name);
                editTextPassengerEmail.setHint(R.string.crtpas_email);
                editTextPassengerMobile.setHint(R.string.crtpas_pahone);
                txtdate.setText(R.string.crtpas_date);
                txtfrom.setText(R.string.crtpas_from);
                txtto.setText(R.string.crtpas_to);
                txtservice.setText(R.string.crtpas_service);
                txtseats.setText(R.string.crtpas_no_seats);
                txtheader.setText(R.string.crtpas_pass_dtls);
                txtsub_header.setText(R.string.crtpas_summary);
//                txtcredit.setText(R.string.crtpas_credit_debit);
//                txtnetbank.setText(R.string.crtpas_netbank);
                btnSaveTempButton.setText(R.string.crtpas_pay);
            }
        }
    }

    public void showLoading(boolean show){
        if(show){
            vHolder.loadingLayout.setVisibility(View.VISIBLE);
        }
        else{
            vHolder.loadingLayout.setVisibility(View.GONE);
        }
    }
}
