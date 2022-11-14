package com.gsrtc.mobileweb.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;


public class AccordionWidget extends LinearLayout {

    String data;

    private static final String SAVED_SUPER_STATE = "SAVED_SUPER_STATE";
    private static final String SAVED_ACCORDION_STATUS = "SAVED_ACCORDION_STATUS";

    private View expandLayout;
    private TextView headingText;
    TextView expand_indicator_icon;
    private ImageView infoIcon;
    private TextView secondaryHeadingText;
    private TextView contentText;
    private TextView expandIndicatorIcon;   //Shows plus or minus sign
    private boolean accordionExpanded;


    public AccordionWidget(Context context) {
        this(context, null);
    }

    public AccordionWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccordionWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs)
    {

        int layoutId = 1;
        int heading = 0;
        int secondaryHeading = 0;
        int content = 0;
        int expandLayoutId = 0;
        int headerLayoutId = 0;

        if (attrs != null) {
            final TypedArray a = context
                    .obtainStyledAttributes(attrs, R.styleable.AccordionWidget);
            layoutId = a.getInt(R.styleable.AccordionWidget_accordion_layout, 1);
            heading = a.getResourceId(R.styleable.AccordionWidget_accordion_heading, 0);
            secondaryHeading = a.getResourceId(R.styleable.AccordionWidget_accordion_secondary_heading, 0);
            content = a.getResourceId(R.styleable.AccordionWidget_accordion_content, 0);
            expandLayoutId = a.getResourceId(R.styleable.AccordionWidget_accordion_expand_layout, 0);
        }

        LayoutInflater inflater = LayoutInflater.from(context);

        View inflatedView = null;

        if(layoutId == 1){
            inflatedView = inflater.inflate(R.layout.accordion_widget_journey_details_header, this, false);
        }
        else{   //else 2
            inflatedView = inflater.inflate(R.layout.accordion_widget_route_details_header, this, false);
        }

        headingText = (TextView) inflatedView.findViewById(R.id.heading_text);

        if (heading != 0) {
            headingText.setText(getResources().getString(heading));
        }
        expandOnFullRow();


        inflatedView.findViewById(R.id.header_container).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandLayout != null) {
                    if (accordionExpanded)
                        collapse(expandLayout);
                    else
                        expand(expandLayout);
                    accordionExpanded = !accordionExpanded;
                }
            }
        });
        expandIndicatorIcon= (TextView)inflatedView.findViewById(R.id.expand_indicator_icon);
        expandIndicatorIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandLayout != null) {
                    if (accordionExpanded){
                        collapse(expandLayout);
                        expandIndicatorIcon.setText("+");
                    }
                    else{
                        expand(expandLayout);
                        expandIndicatorIcon.setText("-");
                    }

                    accordionExpanded = !accordionExpanded;
                }
            }
        });
        if (expandLayoutId != 0) {
            expandLayout = findViewById(expandLayoutId);
        }
        inflatedView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandLayout != null) {
                    if (accordionExpanded)
                        collapse(expandLayout);
                    else
                        expand(expandLayout);
                    accordionExpanded = !accordionExpanded;
                }
            }
        });

        if(infoIcon != null){   //For Settings Activity there will be no info icon
            infoIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (expandLayout != null) {
                        if (accordionExpanded)
                            collapse(expandLayout);
                        else
                            expand(expandLayout);
                        accordionExpanded = !accordionExpanded;
                    }
                }
            });
        }

        addView(inflatedView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void data(String textlang) {
        data = textlang;
        if(data.equalsIgnoreCase("gujarati")) {
            headingText.setText(R.string.fare_fare_details);
        }
    }

    public void updateText(String heading, String content) {
        if(data.equalsIgnoreCase("gujarati"))
        {
            heading = String.valueOf(R.string.fare_fare_details);
            if (headingText != null) {
                headingText.setText(heading);
            }
        }else {
            if (headingText != null) {
                headingText.setText(heading);
            }
        }
        updateContentText(content);
    }

    public void updateContentText(String content){
        if (contentText != null) {
            contentText.setText(content);
        }
    }

    public void updateText(String heading, String content, String secondaryHeading) {
        updateText(heading, content);

        if (secondaryHeadingText != null) {
            secondaryHeadingText.setText(secondaryHeading);
        }
    }

    /**
     The methods takes in accordion heading, sub contents list in the format of(title,content,secondary heading),
     boolean for extra text box case, and the extra text in case of any or else an empty string and an disclaimer text to be shown.
     And populates the accordion with the data.
     **/
    public void setExpandLayout(View expandLayout) {
        this.expandLayout = expandLayout;
    }

    public void expandOnFullRow(){

        headingText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandLayout != null) {
                    if (accordionExpanded){
                        collapse(expandLayout);
                        if(expandIndicatorIcon!=null)
                            expandIndicatorIcon.setText("+");
                    }
                    else {
                        expand(expandLayout);
                        if(expandIndicatorIcon!=null)
                            expandIndicatorIcon.setText("-");
                    }
                    accordionExpanded = !accordionExpanded;
                }
            }
        });
    }

    public void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialActualHeight = v.getMeasuredHeight();

        int initialHeightDp = (int)(initialActualHeight / v.getContext().getResources().getDisplayMetrics().density);

        final int initialHeight;

        if(initialHeightDp > 200){
            initialHeight = (int)(200 * v.getContext().getResources().getDisplayMetrics().density);
        }
        else {
            initialHeight = initialActualHeight;
        }

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED_SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(SAVED_ACCORDION_STATUS, accordionExpanded);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            //noinspection ResourceType
            // retrieve super state
            accordionExpanded = bundle.getBoolean(SAVED_ACCORDION_STATUS);
            state = bundle.getParcelable(SAVED_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    public void hide() {
        this.setVisibility(GONE);
        if (expandLayout != null && accordionExpanded) {
            accordionExpanded = false;
            expandLayout.setVisibility(GONE);
        }
    }

    public void show() {
        this.setVisibility(VISIBLE);
        if (expandLayout != null) {
            expandLayout.setVisibility(GONE);
            accordionExpanded = false;
        }
    }
}
