package com.businessreviewshub.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by shrinivas on 12-12-2016.
 */
public class RobotoBoldTextView extends TextView {
    public RobotoBoldTextView(Context context) {
        super(context);
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/roboto.bold.ttf"));
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/roboto.bold.ttf"));
    }
}
