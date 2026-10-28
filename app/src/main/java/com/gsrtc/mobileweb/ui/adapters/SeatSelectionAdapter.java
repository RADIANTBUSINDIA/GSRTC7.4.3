package com.gsrtc.mobileweb.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.SearchParams;
import com.gsrtc.mobileweb.models.Seat;
import com.gsrtc.mobileweb.models.SeatRow;
import com.gsrtc.mobileweb.models.ServiceSeatDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.recyclerview.widget.RecyclerView;

import static com.gsrtc.mobileweb.utils.AppConstants.SEAT_ROW_COUNT;

/**
 * Created by ajith on 19/4/17.
 */

public abstract class SeatSelectionAdapter extends RecyclerView.Adapter<SeatSelectionAdapter.ViewHolder> {


    public interface OnSeatActionListener {
        void onSeatSelected(Seat seat);
        void onSeatUnselected(Seat seat);
        void onSeatAlreadyBooked(Seat seat);
        void onSeatUnavailable(Seat seat);
    }

    public boolean isSleeperLayout() {
        if (class_layout_id != null && (
                class_layout_id.equalsIgnoreCase("64") ||
                class_layout_id.equalsIgnoreCase("143") ||
                class_layout_id.equalsIgnoreCase("188") ||
                class_layout_id.equalsIgnoreCase("191") ||
                class_layout_id.equalsIgnoreCase("169"))) {
            return true;
        }
        if (srv_class_id != null && (
                srv_class_id.equalsIgnoreCase("21") ||
                srv_class_id.equalsIgnoreCase("22") ||
                srv_class_id.equalsIgnoreCase("42") ||
                srv_class_id.equalsIgnoreCase("45"))) {
            return true;
        }
        return false;
    }
    Context context;
    Set<Integer> reservedSeats;     //Reserved seats will be fetched from the server and passed to the adapter
    Set<Integer> selectedSeats;
    Set<Integer> ladiesSeats;
    List<SeatRow> seatRowList;
    private OnSeatActionListener actionListener;
    Map<Integer, Seat> allSeats;
    ServiceSeatDetails serviceSeatDetails;
    SearchParams searchParams;
    String class_layout_id, srv_class_id;


    public SeatSelectionAdapter(Context context, List<SeatRow> seatRowList, ServiceSeatDetails serviceSeatDetails, SearchParams searchParams, String layout_id,
                                String class_id, OnSeatActionListener listener) {
        this.actionListener = listener;
        this.seatRowList = new ArrayList<SeatRow>();
        this.seatRowList = seatRowList;
        this.selectedSeats = new HashSet<>();
        this.reservedSeats = new HashSet<>();
        this.allSeats = new LinkedHashMap<>();
        this.serviceSeatDetails = serviceSeatDetails;
        this.searchParams = searchParams;
        this.context = context;
        this.class_layout_id = layout_id;
        this.srv_class_id = class_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.seat_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (position == 0) {    //For driver row
            for (int i = 0; i < 5; i++) {
                holder.seatNumberTexts[i].setVisibility(View.GONE);
                holder.seatCheckboxes[i].setVisibility(View.GONE);
            }
            holder.seatCheckboxes[5].setImageResource(R.drawable.steering);
            holder.seatNumberTexts[5].setVisibility(View.GONE);
            holder.seatLayouts[5].setVisibility(View.VISIBLE);
            return;
        }

        SeatRow seatRow = seatRowList.get(position - 1);
        for (int i = 0; i < SEAT_ROW_COUNT; i++) {
            int seatPosition = i + 1;
            Seat seat = seatRow.getSeat(seatPosition);

            if (seat.getSeatNo() == null || seat.getSeatNo() == 0) {
                holder.seatCheckboxes[i].setVisibility(View.GONE);
                holder.seatNumberTexts[i].setVisibility(View.GONE);
            } else {

                allSeats.put(seat.getSeatNo(), seat);
                holder.seatCheckboxes[i].setVisibility(View.VISIBLE);
                holder.seatCheckboxes[i].setTag(seat);
                holder.seatNumberTexts[i].setText("" + seat.getSeatNo() + " " + seat.getDescription());
                setSeatImage(holder.seatCheckboxes[i], seat);

                final int seatIdx = i;
                final Seat seatObj = seat;
                View.OnClickListener seatClickListener = view -> {
                    Seat seat1 = seatObj;
                    if (seat1 == null) return;
                    int seatNo = seat1.getSeatNo();

                    // AVAILABLE seat
                    if (serviceSeatDetails != null && (
                            (serviceSeatDetails.getAvailableSeats() != null && serviceSeatDetails.getAvailableSeats().contains(seatNo)) ||
                            (serviceSeatDetails.getAvailableLadiesSeats() != null && serviceSeatDetails.getAvailableLadiesSeats().contains(seatNo)))) {

                        if (selectedSeats.contains(seatNo)) {
                            setSeatImage(holder.seatCheckboxes[seatIdx], seat1);
                            selectedSeats.remove(seatNo);
                            if (actionListener != null) {
                                actionListener.onSeatUnselected(seat1);
                            }
                        } else {
                            if (selectedSeats.size() <= searchParams.getNumOfPassengers()) {

                                if (isSleeperLayout()) {
                                    holder.seatCheckboxes[seatIdx].setImageResource(R.drawable.selected_sleeper);
                                } else {
                                    holder.seatCheckboxes[seatIdx].setImageResource(R.drawable.selected_seat);
                                }

                                selectedSeats.add(seatNo);

                                if (actionListener != null) {
                                    actionListener.onSeatSelected(seat1);
                                }

                                if (getSelectedSeats().size() == searchParams.getNumOfPassengers()) {
                                    allSeatsSelected(getSelectedSeats());
                                }
                            }
                        }
                    }

                    // BOOKED seat
                    else if (serviceSeatDetails != null && (
                            (serviceSeatDetails.getBookedSeats() != null && serviceSeatDetails.getBookedSeats().contains(seatNo)) ||
                            (serviceSeatDetails.getBookedLadiesSeats() != null && serviceSeatDetails.getBookedLadiesSeats().contains(seatNo)) ||
                            (serviceSeatDetails.getConductorSeats() != null && serviceSeatDetails.getConductorSeats().contains(seatNo)))) {

                        if (actionListener != null) {
                            actionListener.onSeatAlreadyBooked(seat1);
                        }
                    }

                    // UNAVAILABLE
                    else {
                        if (actionListener != null) {
                            actionListener.onSeatUnavailable(seat1);
                        }
                    }
                };

                holder.seatCheckboxes[i].setOnClickListener(seatClickListener);
                if (holder.seatLayouts[i] != null) {
                    holder.seatLayouts[i].setOnClickListener(seatClickListener);
                }
                if (holder.seatNumberTexts[i] != null) {
                    holder.seatNumberTexts[i].setOnClickListener(seatClickListener);
                }
            }
        }
    }

    public void setSeatImage(ImageView view, Seat seat) {

        if (isSleeperLayout()) {

            if (serviceSeatDetails.getBookedLadiesSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_ladies_sleeper);
            } else if (serviceSeatDetails.getBookedSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_sleeper);
            } else if (serviceSeatDetails.getConductorSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_sleeper);
            } else if (serviceSeatDetails.getAvailableLadiesSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.available_ladies_sleeper);
            } else if (serviceSeatDetails.getAvailableSeats().contains(seat.getSeatNo())) {
                // view.setImageResource(R.drawable.available_sleeper);
//                if (searchParams.getDivyangBooking().equalsIgnoreCase("Divyang") || searchParams.getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
//                    if (srv_class_id.equalsIgnoreCase("21") || srv_class_id.equalsIgnoreCase("22")
//                            || srv_class_id.equalsIgnoreCase("42") || srv_class_id.equalsIgnoreCase("45")) {
//                        if (seat.getSeatNo() == 1 || seat.getSeatNo() == 2 || seat.getSeatNo() == 3 || seat.getSeatNo() == 4) {
//                            view.setImageResource(R.drawable.available_sleeper);
//                            view.setFocusable(true);
//                            view.setClickable(true);
//                            view.setEnabled(true);
//                        } else {
//                            view.setImageResource(R.drawable.available_sleeper);
//
//
//                            view.setFocusable(false);
//                            view.setClickable(false);
//                            view.setEnabled(false);
//                        }
//                    } else {
//                        view.setImageResource(R.drawable.available_sleeper);
//                        view.setFocusable(true);
//                        view.setClickable(true);
//                        view.setEnabled(true);
//                    }
//                } else {
                    view.setImageResource(R.drawable.available_sleeper);
                    view.setFocusable(true);
                    view.setClickable(true);
                    view.setEnabled(true);
                }


        } else {

            if (serviceSeatDetails.getBookedLadiesSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_ladies_seat);
            } else if (serviceSeatDetails.getBookedSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_seat_new);
            } else if (serviceSeatDetails.getConductorSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.booked_seat_new);
            } else if (serviceSeatDetails.getAvailableLadiesSeats().contains(seat.getSeatNo())) {
                view.setImageResource(R.drawable.ladies_seat);
            } else if (serviceSeatDetails.getAvailableSeats().contains(seat.getSeatNo())) {
//                view.setImageResource(R.drawable.available_seat_new);

//                if (searchParams.getDivyangBooking().equalsIgnoreCase("Divyang") || searchParams.getDivyangBooking().equalsIgnoreCase("Divyang with Helper")) {
//                    if (srv_class_id.equalsIgnoreCase("21") || srv_class_id.equalsIgnoreCase("22")
//                            || srv_class_id.equalsIgnoreCase("42") || srv_class_id.equalsIgnoreCase("45")) {
//                        if (seat.getSeatNo() == 1 || seat.getSeatNo() == 2 || seat.getSeatNo() == 3 || seat.getSeatNo() == 4) {
//                            view.setImageResource(R.drawable.available_seat_new);
//                            view.setFocusable(true);
//                            view.setClickable(true);
//                            view.setEnabled(true);
//                        } else {
//                            view.setImageResource(R.drawable.available_seat_new);
//                            view.setFocusable(false);
//                            view.setClickable(false);
//                            view.setEnabled(false);
//
//                        }
//                    } else {
//                        view.setImageResource(R.drawable.available_seat_new);
//                        view.setFocusable(true);
//                        view.setClickable(true);
//                        view.setEnabled(true);
//                    }
//                } else {
                    view.setImageResource(R.drawable.available_seat_new);
                    view.setFocusable(true);
                    view.setClickable(true);
                    view.setEnabled(true);

            }
        }
    }

    @Override
    public int getItemCount() {
        return (this.seatRowList != null) ? this.seatRowList.size() + 1 : 0; // 1 for driver row
    }

    public ArrayList<Seat> getSelectedSeats() {
        ArrayList<Seat> seatList = new ArrayList<>();
        for (Integer selected : selectedSeats) {
            if (allSeats.containsKey(selected)) {
                seatList.add(allSeats.get(selected));
            }
        }
        return seatList;
    }

    abstract public void allSeatsSelected(ArrayList<Seat> selectedSeats);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View[] seatLayouts;
        ImageView[] seatCheckboxes;
        TextView[] seatNumberTexts;

        public ViewHolder(View itemView) {
            super(itemView);
            seatLayouts = new View[SEAT_ROW_COUNT];
            seatCheckboxes = new ImageView[SEAT_ROW_COUNT];
            seatNumberTexts = new TextView[SEAT_ROW_COUNT];
            seatLayouts[0] = itemView.findViewById(R.id.seat1_layout);
            seatLayouts[1] = itemView.findViewById(R.id.seat2_layout);
            seatLayouts[2] = itemView.findViewById(R.id.seat3_layout);
            seatLayouts[3] = itemView.findViewById(R.id.seat4_layout);
            seatLayouts[4] = itemView.findViewById(R.id.seat5_layout);
            seatLayouts[5] = itemView.findViewById(R.id.seat6_layout);

            seatCheckboxes[0] = (ImageView) itemView.findViewById(R.id.seat1);
            seatCheckboxes[1] = (ImageView) itemView.findViewById(R.id.seat2);
            seatCheckboxes[2] = (ImageView) itemView.findViewById(R.id.seat3);
            seatCheckboxes[3] = (ImageView) itemView.findViewById(R.id.seat4);
            seatCheckboxes[4] = (ImageView) itemView.findViewById(R.id.seat5);
            seatCheckboxes[5] = (ImageView) itemView.findViewById(R.id.seat6);

            seatNumberTexts[0] = (TextView) itemView.findViewById(R.id.seat1_text);
            seatNumberTexts[1] = (TextView) itemView.findViewById(R.id.seat2_text);
            seatNumberTexts[2] = (TextView) itemView.findViewById(R.id.seat3_text);
            seatNumberTexts[3] = (TextView) itemView.findViewById(R.id.seat4_text);
            seatNumberTexts[4] = (TextView) itemView.findViewById(R.id.seat5_text);
            seatNumberTexts[5] = (TextView) itemView.findViewById(R.id.seat6_text);

        }
    }
}
