package com.example.revendiquons.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.CompoundButton;

public class myCompoundBtn extends CompoundButton implements Checkable {

    public myCompoundBtn(Context context) {
        this(context, null);
        Log.i("Tom", "myCompoundBtn creation1");
    }

    public myCompoundBtn(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        Log.i("Tom", "myCompoundBtn creation2");
    }
}
