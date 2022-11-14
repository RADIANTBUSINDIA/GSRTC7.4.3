package com.gsrtc.mobileweb.ui.activities.booking_history;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.booking_history.BookingHistory;
import com.gsrtc.mobileweb.models.booking_history.GetViewBookingHistoryDetailsResponse;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.AppConstants;

/**
 * Created by Ayush on 23-May-17.
 */

public class BookingHistorySearchResultActivity extends BaseNavigationDrawerActivity {

    BookingHistory booking;
    ActivityViewHolder holder;
    boolean dataFetched = false;
    SearchResultAdapter searchResultAdapter;
    ProgressDialog progressDialog;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_booking_history_result);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle extras =  getIntent().getExtras();
            textlang =extras.getString("textlang");
            booking = (BookingHistory)extras.getSerializable(AppConstants.INTENT_EXTRA.BOOKING_HISTORY);
            setup();
        }
    }

    public void setup() {

        holder = new ActivityViewHolder(this);
        if(textlang.equalsIgnoreCase("gujarati")){
            holder.head.setText("   વઇતિહાસ બુકિંગ\n" + booking.bookingHistorySearchParam.fromDate + "      માટે      " + booking.bookingHistorySearchParam.toDate);
        }else {
            holder.head.setText("   BOOKING HISTORY\n" + booking.bookingHistorySearchParam.fromDate + "      TO      " + booking.bookingHistorySearchParam.toDate);
        }

        searchResultAdapter = new SearchResultAdapter(this, Arrays.asList(booking.getViewBookingHistoryDetailsResponse));
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this);
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(),
                layoutManager.getOrientation() );
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        holder.recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.notifyDataSetChanged();
    }

    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

        private List<GetViewBookingHistoryDetailsResponse> bookList;
        Context context;

        public SearchResultAdapter(Context context, List<GetViewBookingHistoryDetailsResponse> bookList) {
            if(bookList == null)
                bookList = new ArrayList<GetViewBookingHistoryDetailsResponse>();
            this.bookList = bookList;
            this.context = context;
        }
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.booking_history_list_item, parent, false);

            // Return a new holder instance
            return new ViewHolder(contactView);
        }
        public void onBindViewHolder(ViewHolder holder, int position) {
            GetViewBookingHistoryDetailsResponse book= this.bookList.get(position);


            holder.container.setTag(position);
            if(textlang.equalsIgnoreCase("gujarati")){

                holder.bookedOn.setText("બુક કરાવ્યું : - "+book.getBookedOn ());
                holder.date.setText("પ્રવાસ તારીખ : - "+book.getJourneyDate ());
                holder.fare.setText("રૂપિયા-"+book.getFare());
                holder.place.setText(book.getStartPlace()+" - "+book.getEndPlace());
                holder.status.setText("સ્થિતિ : - "+book.getTicketStatusDescription());
                holder.pnr.setText("ી.એન.આર.નં. : - "+book.getPnrNo());
                holder.tripCode.setText("");
                holder.obrefno.setText("રેફ. ના. : - " + book.getGsrtcOBRefNo());
                holder.mobleno.setText("મોબાઈલ નં : - " + book.getMobileNo());
            }else {
                holder.bookedOn.setText("Booked On - " + book.getBookedOn());
                holder.date.setText("Journey Date - " + book.getJourneyDate());
                holder.fare.setText("INR- " + book.getFare());
                holder.place.setText(book.getStartPlace() + " - " + book.getEndPlace());
                holder.status.setText("Status - " + book.getTicketStatusDescription());
                holder.pnr.setText("PNR - " + book.getPnrNo());
                holder.tripCode.setText("");
                holder.obrefno.setText("Ref. No. - " + book.getGsrtcOBRefNo());
                holder.mobleno.setText("Mobile No. - " + book.getMobileNo());
            }
        }


        // Clean all elements of the recycler
        public void clear() {
            bookList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView bookedOn,fare,place,status,date, pnr, tripCode, seatNo;
            TextView obrefno,mobleno;
            View container;

            ViewHolder(View itemView) {
                super(itemView);
                container = itemView;
                bookedOn = (TextView) itemView.findViewById(R.id.bookedOn);
                fare = (TextView) itemView.findViewById(R.id.fare);
                place = (TextView) itemView.findViewById(R.id.place);
                status = (TextView) itemView.findViewById(R.id.status);
                date = (TextView) itemView.findViewById(R.id.date);
                pnr = (TextView) itemView.findViewById(R.id.pnr);
                tripCode = (TextView) itemView.findViewById(R.id.trip_code);
                //seatNo = (TextView) itemView.findViewById(R.id.seat_no);
                obrefno = (TextView) itemView.findViewById(R.id.obrefno);
                mobleno = (TextView) itemView.findViewById(R.id.mobleno);
            }
        }
    }


    public static class ActivityViewHolder{

        RecyclerView recyclerView;
        SwipeRefreshLayout swipeContainer;
        TextView head;

        ActivityViewHolder(Activity activity) {

            head=(TextView) activity.findViewById(R.id.header_text);

            recyclerView = (RecyclerView) activity.findViewById(R.id.history_search_results_recycler_view);
            }
        }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(BookingHistoryActivity.this, HomeActivity.class, null);
        Intent i = new Intent(BookingHistorySearchResultActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
