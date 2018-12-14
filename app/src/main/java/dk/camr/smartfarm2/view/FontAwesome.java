package dk.camr.smartfarm2.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontAwesome extends android.support.v7.widget.AppCompatTextView {


    public FontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesome(Context context) {
        super(context);
        init();
    }

    private void init() {

        System.out.println("AWESOME");

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Font Awesome 5 Free-Solid-900.otf");
        setTypeface(tf);
    }

    public void setIcon(String icon) {
        int decimal = Integer.parseInt(icon, 16);
        char b = (char) decimal;
        String ss = String.valueOf(b);
        setText(ss);
    }
}