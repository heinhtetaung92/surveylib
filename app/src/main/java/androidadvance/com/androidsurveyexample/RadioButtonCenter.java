package androidadvance.com.androidsurveyexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

public class RadioButtonCenter extends RadioButton {
    Drawable buttonDrawable;

    public RadioButtonCenter(Context context) {
        super(context);
        final TypedArray a = context.obtainStyledAttributes(R.styleable.CompoundButton);
        if (buttonDrawable != null) {
            setButtonDrawable(buttonDrawable);
        }

    }

    public RadioButtonCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
        //final TypedArray a = context.obtainStyledAttributes(com.android.internal.R.styleable.CompoundButton);
        //buttonDrawable = a.getDrawable(com.android.internal.R.styleable.CompoundButton_button);
        if (buttonDrawable != null) {
            setButtonDrawable(buttonDrawable);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buttonDrawable = getButtonDrawable();
        if (buttonDrawable != null) {
            buttonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
    }
}