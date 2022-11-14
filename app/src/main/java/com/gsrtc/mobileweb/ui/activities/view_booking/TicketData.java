package com.gsrtc.mobileweb.ui.activities.view_booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.print_sms.GetPrintSmsDetailslResponse;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsData;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.AppConstants;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.BookingHistoryNetworkUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TicketData extends BaseNavigationDrawerActivity {

    PrintSmsData ticketsms;
    ActivityViewHolder holder;
    boolean dataFetched = false;
    SearchResultAdapter searchResultAdapter;
    private String pnrNo;
    private String currentDate;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_ticket_data);

        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle extras =  getIntent().getExtras();
            textlang = extras.getString("textlang");
            ticketsms = (PrintSmsData) extras.getSerializable(AppConstants.INTENT_EXTRA.PRINT_SMS_TICKET);
            setup();
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd",Locale.getDefault());
        int cur_year = Integer.valueOf(df1.format(c.getTime()));
        String cur_month = String.valueOf(df.format(c.getTime()));
        String cur_day = String.valueOf(df2.format(c.getTime()));
        currentDate = cur_day+"/"+cur_month+"/"+cur_year;
    }

    public void setup() {

        holder = new ActivityViewHolder(this);

        searchResultAdapter = new SearchResultAdapter(this, Arrays.asList(ticketsms.getPrintSmsDetailslResponses));
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(),
                layoutManager.getOrientation() );
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        holder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();

    }

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

        private List<GetPrintSmsDetailslResponse> ticketList;
        Context context;

        public SearchResultAdapter(Context context, List<GetPrintSmsDetailslResponse> bookList) {
            if(bookList == null)
                bookList = new ArrayList<GetPrintSmsDetailslResponse>();
            this.ticketList = bookList;
            this.context = context;
        }
        public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.track_data_list, parent, false);

            // Return a new holder instance
            return new SearchResultAdapter.ViewHolder(contactView);
        }
        public void onBindViewHolder(final SearchResultAdapter.ViewHolder holder, int position) {
            final GetPrintSmsDetailslResponse ticket= this.ticketList.get(position);

            holder.container.setTag(position);

            holder.pnr.setText(ticket.getPnrNumber());
            holder.date.setText(ticket.getJourneyDate ());
            holder.fare.setText(ticket.getGatewayAmount()+".00");
            holder.fromplace.setText(ticket.getStartPlaceName());
            holder.toplace.setText(ticket.getEndPlaceName());
            holder.tripcode.setText(ticket.getTripCode());

            holder.sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String jrnydate = ticket.getJourneyDate().trim().toString();
                        try{

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


                            String str1 = jrnydate;
                            Date date1 = formatter.parse(str1);

                            String str2 = currentDate;
                            Date date2 = formatter.parse(str2);

                            if (date1.compareTo(date2)>=0)
                            {

                                String getpnr=holder.pnr.getText().toString();
                                ticketsms.printSmsParam.pnrNo= getpnr;
                                resend_sms(getpnr);
                               // System.out.println("date2 is Greater than my date1");
                            }
                            else
                            {
                                ActivityUtil.showToast(TicketData.this, "Sorry!Ticket is not valid for the Journey");
                            }


                    }catch (ParseException e1){
                        e1.printStackTrace();
                    }

                }
            });
        }

       /*** Calling sendSMS method ***/

        private void resend_sms(String getpnr){

            if(getpnr.equalsIgnoreCase("")){
                ActivityUtil.showToast(TicketData.this, "Invalid PNR Number");
                return;
            }

            if(getpnr.isEmpty()){
                ActivityUtil.showToast(TicketData.this, "Invalid PNR Number");
                return;
            }

            CustomisedProgressDialog.SHOW_CUST_DIA(TicketData.this, "Please wait loading...");

            BookingHistoryNetworkUtils.GetResendSms(ticketsms, new BookingHistoryNetworkUtils.GetResendSmsCallback() {

                public void onSuccess(String response) {

                    if (response != null)
                    {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(TicketData.this, String.valueOf(response));
                        ActivityUtil.showToast(TicketData.this, "SMS Sent Successfully");

                    } else
                    {
                        CustomisedProgressDialog.STOP_CUST_DIA();
                        ActivityUtil.showToast(TicketData.this, "SMS Sent Successfully");

                    }
                }

                public void onError(Exception e) {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showErrorToast(TicketData.this, e);
                }
            });
        }


        // Clean all elements of the recycler
        public void clear() {
            ticketList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return ticketList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView fare,fromplace,toplace,date, pnr,tripcode;
            TextView faretxt,fromplacetxt,toplacetxt,datetxt, pnrtxt,tripcodetxt,titletxt;
            Button sms;
            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                sms= (Button)itemView.findViewById(R.id.send);
                fare = (TextView) itemView.findViewById(R.id.amount);
                fromplace = (TextView) itemView.findViewById(R.id.from);
                date = (TextView) itemView.findViewById(R.id.journey_date);
                pnr = (TextView) itemView.findViewById(R.id.pnr);
                toplace= (TextView) itemView.findViewById(R.id.to);
                tripcode= (TextView) itemView.findViewById(R.id.tripco);

                faretxt = (TextView) itemView.findViewById(R.id.fare);
                fromplacetxt = (TextView) itemView.findViewById(R.id.from_place);
                datetxt = (TextView) itemView.findViewById(R.id.journey);
                pnrtxt = (TextView) itemView.findViewById(R.id.pnr_no);
                toplacetxt= (TextView) itemView.findViewById(R.id.to_place);
                tripcodetxt= (TextView) itemView.findViewById(R.id.tripcode);

                if(textlang.equalsIgnoreCase("gujarati")){
                    faretxt.setText(R.string.smsdet_amount);
                    fromplacetxt.setText(R.string.smsdet_from);
                    datetxt.setText(R.string.smsdet_journey);
                    pnrtxt.setText(R.string.smsdet_pnr);
                    toplacetxt.setText(R.string.smsdet_toplace);
                    tripcodetxt.setText(R.string.smsdet_tripco);
                }
            }
        }
    }

    public class ActivityViewHolder{

        RecyclerView recyclerView;
        SwipeRefreshLayout swipeContainer;
        TextView head;
        ActivityViewHolder(Activity activity) {

            head=(TextView) activity.findViewById(R.id.header_text);
            recyclerView = (RecyclerView) activity.findViewById(R.id.print_sms_recycler_view);
            if(textlang.equalsIgnoreCase("gujarati")) {
                head.setText(R.string.smsdet_title);
            }
        }
    }
    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(ViewandSMSTicket.this, HomeActivity.class, null);
        Intent i = new Intent(TicketData.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
