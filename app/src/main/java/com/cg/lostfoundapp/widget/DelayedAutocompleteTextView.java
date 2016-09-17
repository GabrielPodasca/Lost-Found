package com.cg.lostfoundapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Gabi on 9/17/2016.
 * http://garbtech.co.uk/tag/autocompletetextview/
 */
public class DelayedAutocompleteTextView extends AutoCompleteTextView{

    private static final int MESSAGE_TEXT_CHANGED = 1;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 700;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayedAutocompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public DelayedAutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {

        handler.removeMessages(MESSAGE_TEXT_CHANGED);
        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEXT_CHANGED, keyCode, 0, text), DEFAULT_AUTOCOMPLETE_DELAY);
    }
}
