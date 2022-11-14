package com.gsrtc.mobileweb.ui.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.IdRes;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.ui.activities.base.BaseNavigationDrawerActivity;
import com.gsrtc.mobileweb.utils.ActivityUtil;
import com.gsrtc.mobileweb.utils.ValidationUtil;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class FeedbackActivity extends BaseNavigationDrawerActivity {

    EditText issues, feed_email,feed_mobile;
    Button sendmail;
    private boolean feedback = false;
    RadioButton feedback_one, feed_back_two;
    RadioGroup radio;
    TextView title;
    ProgressDialog progressDialog;
    String textlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewDrawer(R.layout.activity_feedback);

        Bundle mBun = getIntent().getExtras();
        textlang =mBun.getString("textlang");

        issues = (EditText) findViewById(R.id.feedback);
        feed_email = (EditText) findViewById(R.id.feedback_email);
        feed_mobile =(EditText) findViewById(R.id.feedback_phone);
        sendmail = (Button) findViewById(R.id.sendmail);
        feedback_one = (RadioButton) findViewById(R.id.feedback_one);
        feed_back_two = (RadioButton) findViewById(R.id.feedback_two);
        title = (TextView) findViewById(R.id.title_feedback);
        radio = (RadioGroup) findViewById(R.id.radio);

        if(textlang.equalsIgnoreCase("gujarati")){
            issues.setHint(R.string.feed_descrie_issue);
            feed_email.setHint(R.string.feed_enter_email);
            feed_mobile.setHint(R.string.feed_enter_mobile);
            sendmail.setText(R.string.feed_submit);
            feedback_one.setText(R.string.feed_issue);
            feed_back_two.setText(R.string.feed_suggestion);
            title.setText(R.string.feed_feedback);
        }
        mailview();
    }

    public void mailview() {

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if ("Issue".equalsIgnoreCase("" + button.getText())) {
                    feedback = false;
                } else {
                    feedback = true;
                }
            }
        });

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                sendMessage();

            }
        });
    }

    private void sendMessage() {

        if (TextUtils.isEmpty(issues.getText())) {
            hideProgressDialog();
            ActivityUtil.showToast(this, "issues and suggestion cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(feed_email.getText())) {
            hideProgressDialog();
            ActivityUtil.showToast(this, "Email cannot be empty");
            return;
        }
        if (!ValidationUtil.isValidEmail(feed_email.getText())) {
            hideProgressDialog();
            ActivityUtil.showToast(this, "Please enter valid email");
            return;
        }
        if (TextUtils.isEmpty(feed_mobile.getText())) {
            hideProgressDialog();
            ActivityUtil.showToast(this, "Mobile Number cannot be empty");
            return;
        }
        if (!ValidationUtil.isValidPhone(feed_mobile.getText())) {
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            hideProgressDialog();
            return;
        }
        if (feed_mobile.getText().length()<10) {
            hideProgressDialog();
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        if(feed_mobile.getText().toString().trim().length()==13)
        {
            String mobnum=feed_mobile.getText().toString();

            if (mobnum.startsWith("+"))
            {
                if (mobnum.length() == 13)
                {
                    String str_getMOBILE = mobnum.substring(3);
                    feed_mobile.setText(str_getMOBILE);
                }
            }
            else
            {
                hideProgressDialog();
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }
        else if(feed_mobile.getText().toString().trim().length()>10)
        {
            hideProgressDialog();
            ActivityUtil.showToast(this, "Please enter valid Mobile Number");
            return;
        }
        else if(feed_mobile.getText().toString().trim().length()==10) {

            String mobileno = feed_mobile.getText().toString();

            if (mobileno.startsWith("+")) {
                hideProgressDialog();
                ActivityUtil.showToast(this, "Please enter valid Mobile Number");
                return;
            }
        }

        String user="GSRTCAppComplaints@gmail.com";
        String pass="GSRTC@pp123";
        String mymailid="gsrtcoprs@gmail.com";
        String[] recipients = {mymailid};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.activity = this;
        email.m = new GMailSender(user, pass);
        email.m.set_from(user);
        email.m.setBody("|MESSAGE| ::  "+issues.getText().toString()+"   |USER_EMAIL| :: "+feed_email.getText().toString() +"    |USER_MOBILE|  :: "+feed_mobile.getText().toString());
        email.m.set_to(recipients);
        email.m.set_subject("USERS ISSUE and SUGGESTION");
        email.execute();

        issues.setText("");
        feed_email.setText("");
        feed_mobile.setText("");
    }

    @SuppressLint("WrongViewCast")
    public void displayMessage(String message) {
        Snackbar.make(findViewById(R.id.sendmail), message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        //ActivityUtil.openNewActivityAsOnlyActivity(FeedbackActivity.this, HomeActivity.class, null);
        Intent i = new Intent(FeedbackActivity.this, HomeActivity.class);
        Bundle data = new Bundle();
        data.putString("textlang", textlang);
        i.putExtras(data);
        startActivity(i);
    }

    @SuppressLint("StaticFieldLeak")
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        GMailSender m;
        FeedbackActivity activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                if (m.send()) {
                    hideProgressDialog();
                    activity.displayMessage("your feedback submitted.");
                } else {
                    hideProgressDialog();
                    activity.displayMessage("Please try again once.");
                }
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                hideProgressDialog();
                activity.displayMessage("Authentication failed.");
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
                hideProgressDialog();
                activity.displayMessage("Email failed to send.");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
                activity.displayMessage("Unexpected error occured.");
                return false;
            }
        }
    }

    public void showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                progressDialog.dismiss();
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {
        }
    }

    public void hideProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }
}