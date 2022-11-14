package com.gsrtc.mobileweb.ui.activities.ticket_cancellation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.models.ticket_cancellation.BookingCancellation;
import com.gsrtc.mobileweb.models.ticket_cancellation.CancellationSearchParams;
import com.gsrtc.mobileweb.ui.activities.HomeActivity;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.customdialog.CustomisedProgressDialog;
import com.gsrtc.mobileweb.utils.net.TicketCancelNetworkUtil;

public class Forgot_Txn_Pass extends BaseNavigationDrawerActivity {

    ViewHolder holder;
    BookingCancellation booking;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_forgot__txn__pass);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        setup();
    }

    public void setup(){
        holder = new ViewHolder(this);
        booking = new BookingCancellation();
        booking.cancellationSearchParams = new CancellationSearchParams();

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgottransBtn();
            }
        });
    }

    public void forgottransBtn(){

        if(TextUtils.isEmpty(holder.pnr.getText().toString())){
            ActivityUtil.showToast(this, "PNR can not be empty");
            return;
        }
        booking.cancellationSearchParams.pnr = holder.pnr.getText().toString();

        goTranspass();
    }

    private void goTranspass() {

        holder.pnr.setText("");
        //holder.submitBtn.clearFocus();
        holder.submitBtn.setEnabled(false);
        //holder.submitBtn.setVisibility(View.INVISIBLE);

        CustomisedProgressDialog.SHOW_CUST_DIA(Forgot_Txn_Pass.this, "Please wait loading...");

        TicketCancelNetworkUtil.GetRetrivetxnPass(booking, new TicketCancelNetworkUtil.GetRetrivetxnPassCallback() {

            public void onSuccess(String response) {
                if (response != null)
                {
                    CustomisedProgressDialog.STOP_CUST_DIA();
                    ActivityUtil.showToast(Forgot_Txn_Pass.this, String.valueOf(response));

                } else
                    {
                    ActivityUtil.showToast(Forgot_Txn_Pass.this, "Invalid Pnr Number");
                    CustomisedProgressDialog.STOP_CUST_DIA();
                }
            }

            public void onError(Exception e) {
                CustomisedProgressDialog.STOP_CUST_DIA();
                ActivityUtil.showErrorToast(Forgot_Txn_Pass.this, e);
            }
        });
    }

    public class ViewHolder{
        EditText pnr;
        Button submitBtn;
        TextView txtpnr,title,note;

        ViewHolder(Activity activity){
            pnr = (EditText) activity.findViewById(R.id.pnr);
            txtpnr = (TextView) activity.findViewById(R.id.pnrtxt);
            title = (TextView) activity.findViewById(R.id.title_forgetpass);
            note = (TextView) activity.findViewById(R.id.forget_note);
            submitBtn = (Button) activity.findViewById(R.id.txnpassBtn);

            if(textlang.equalsIgnoreCase("gujarati")){
                pnr.setHint(R.string.rtvtrpas_enter_pnr);
                txtpnr.setText(R.string.rtvtrpas_pnr);
                title.setText(R.string.rtvtrpas_title);
                note.setText(R.string.rtvtrpas_note);
                submitBtn.setText(R.string.rtvtrpas_retrv_pass);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(Forgot_Txn_Pass.this, HomeActivity.class, null);
        Intent i = new Intent(Forgot_Txn_Pass.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }
}
