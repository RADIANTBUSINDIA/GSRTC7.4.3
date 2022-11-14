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
import com.gsrtc.mobileweb.models.booking_history.GetTrackTicketDetails;
import com.gsrtc.mobileweb.models.print_sms.PrintSmsData;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TrackTicket_details extends BaseNavigationDrawerActivity {

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
        setContentViewDrawer(R.layout.activity_track_ticket_details);

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

        searchResultAdapter = new SearchResultAdapter(this, Arrays.asList(ticketsms.getTrackTicketDetails));
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(),
                layoutManager.getOrientation() );
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        holder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();

    }

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

        private List<GetTrackTicketDetails> ticketList;
        Context context;

        public SearchResultAdapter(Context context, List<GetTrackTicketDetails> bookList) {
            if(bookList == null)
                bookList = new ArrayList<GetTrackTicketDetails>();
            this.ticketList = bookList;
            this.context = context;
        }
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.track_ticket_data_list, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }
        public void onBindViewHolder(final SearchResultAdapter.ViewHolder holder, int position) {
            final GetTrackTicketDetails ticket= this.ticketList.get(position);

            holder.container.setTag(position);

            holder.fare.setText(ticket.getTotalFare());
            holder.fromplace.setText(ticket.getFromPlace());
            holder.date.setText(ticket.getDateofJourney());
            holder.pickupName.setText(ticket.getPickupPointName());
            holder.pickupTime.setText(ticket.getPickupPointTime());
            holder.dropName.setText(ticket.getDropoffPoint());
            holder.toplace.setText(ticket.getToPlace());
            holder.tripcode.setText(ticket.getTripCode());
            holder.adult.setText(ticket.getAdultCount());
            holder.child.setText(ticket.getChildCount());
            holder.seatNo.setText(ticket.getSeatNo());
            holder.serv_class.setText(ticket.getClassofService());
            holder.transDate.setText(ticket.getDateTime());
            holder.pnr.setText(ticketsms.printSmsParam.pnrNo);

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
            TextView fare,fromplace,transDate,toplace,date,pickupName,pickupTime,dropName,tripcode,adult,child,seatNo,serv_class,pnr;
            TextView faretxt,fromplacetxt,toplacetxt,datetxt,pickupNametxt,pickupTimetxt,dropNametxt,tripcodetxt,adulttxt,childtxt,seatNotxt,serv_clstxt,pnrtxt,transDatetxt;
            Button sms;
            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;

                fare = (TextView) itemView.findViewById(R.id.total_fare);
                fromplace = (TextView) itemView.findViewById(R.id.from);
                date = (TextView) itemView.findViewById(R.id.journey_date);
                pickupName = (TextView) itemView.findViewById(R.id.pickName);
                pickupTime = (TextView) itemView.findViewById(R.id.pickTime);
                dropName = (TextView) itemView.findViewById(R.id.dropName);
                toplace= (TextView) itemView.findViewById(R.id.to);
                tripcode= (TextView) itemView.findViewById(R.id.tripco);
                adult= (TextView) itemView.findViewById(R.id.adult_count);
                child= (TextView) itemView.findViewById(R.id.child_count);
                seatNo= (TextView) itemView.findViewById(R.id.SeatNo);
                serv_class= (TextView) itemView.findViewById(R.id.serv_cls);
                pnr= (TextView) itemView.findViewById(R.id.pnr);
                transDate= (TextView) itemView.findViewById(R.id.trans_date);

                faretxt = (TextView) itemView.findViewById(R.id.fare);
                fromplacetxt = (TextView) itemView.findViewById(R.id.from_place);
                datetxt = (TextView) itemView.findViewById(R.id.journey);
                pickupNametxt = (TextView) itemView.findViewById(R.id.pickupName);
                pickupTimetxt = (TextView) itemView.findViewById(R.id.pickupTime);
                dropNametxt = (TextView) itemView.findViewById(R.id.droppoint);
                toplacetxt = (TextView) itemView.findViewById(R.id.to_place);
                tripcodetxt = (TextView) itemView.findViewById(R.id.tripcode);
                adulttxt = (TextView) itemView.findViewById(R.id.adult);
                childtxt = (TextView) itemView.findViewById(R.id.child);
                seatNotxt = (TextView) itemView.findViewById(R.id.seat);
                serv_clstxt = (TextView) itemView.findViewById(R.id.serv_clstxt);
                pnrtxt = (TextView) itemView.findViewById(R.id.pnrtxt);
                transDatetxt= (TextView) itemView.findViewById(R.id.transdatetxt);

                if(textlang.equalsIgnoreCase("gujarati")){

                    faretxt.setText("કુલ ભાડું");
                    fromplacetxt.setText(R.string.smsdet_from);
                    datetxt.setText(R.string.smsdet_journey);
                    pickupNametxt.setText(R.string.gPickup_Name);
                    pickupTimetxt.setText("દુકાન સમય  :-");
                    dropNametxt.setText("છોડો બિંદુ  :-");
                    toplacetxt.setText(R.string.smsdet_toplace);
                    tripcodetxt.setText(R.string.smsdet_tripco);
                    adulttxt.setText("પુખ્ત  :-"  );
                    childtxt.setText("બાળક  :-");
                    seatNotxt.setText("બેઠક નં  :-");
                    serv_clstxt.setText(R.string.gClass_of_Service);
                    pnrtxt.setText(R.string.smsdet_pnr);
                    transDatetxt.setText("વ્યવહાર તારીખ  :-");
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
                head.setText(R.string.gTicket_details);
            }
        }
    }
    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(ViewandSMSTicket.this, HomeActivity.class, null);
        Intent i = new Intent(TrackTicket_details.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}


