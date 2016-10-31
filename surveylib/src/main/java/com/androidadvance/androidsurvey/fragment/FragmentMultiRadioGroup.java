package com.androidadvance.androidsurvey.fragment;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.models.Question;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by kaungkhantthu on 10/31/16.
 */

public class FragmentMultiRadioGroup extends Fragment {

    private static final int FIRSTCOLUMNWIDTH = 80;
    private static FragmentMultiRadioGroup multiRadioFragment;
    private LinearLayout mainLayout;
    private int numberOFradioGroup;
    private int numberOFradioButton;
    private int DEFAULTPADDING = 10;
    private int heighestWidth = 180;
    private Question q_data;
    private FragmentActivity mContext;
    private TextView txt_question;
    private FrameLayout frameLayout;

    // private int NORMALCOLUMNWIDTH = 150;
    private FragmentMultiRadioGroup() {
    }

    public static FragmentMultiRadioGroup getInstance() {
        if (multiRadioFragment == null) {
            multiRadioFragment = new FragmentMultiRadioGroup();

        }
        return multiRadioFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_multiradiogroup, container, false);
        mContext = getActivity();

        q_data = (Question) getArguments().getSerializable("data");
        numberOFradioGroup = q_data.getRows().size();
        txt_question = (TextView) v.findViewById(R.id.textview_q_title);
        txt_question.setText(q_data.getQuestionTitle());
        mainLayout = (LinearLayout) v.findViewById(R.id.mainLinearLayout);
        frameLayout = (FrameLayout) v.findViewById(R.id.frame_firstRow);

        LinearLayout firstRow = generateFirstRow(q_data.getColumns());
        frameLayout.addView(firstRow);
        for (int i = 0; i < numberOFradioGroup; i++) {
            LinearLayout rowLayout = generateRow(q_data.getRows().get(i), i);


            mainLayout.addView(rowLayout);
        }
        Log.e(TAG, "onCreateView: ");

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        findChildViewAndApplyHighestwidth(mainLayout);
        findChildViewAndApplyHighestwidth(frameLayout);
        Drawable d = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            d = mContext.getDrawable(R.drawable.textinputborder);
        } else {
            d = mContext.getResources().getDrawable(R.drawable.textinputborder);

        }

        applyDrawableBackGroundToAllView(mainLayout);
        applyDrawableBackGroundToAllView(frameLayout);
        Log.e(TAG, "onViewCreated: ");

    }

    private void applyDrawableBackGroundToAllView(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) vg.getChildAt(i);
                applyDrawableBackGroundToAllView(viewGroup);
            } else {
                final View v = vg.getChildAt(i);
                if (v instanceof TextView) {

                    v.setBackgroundResource(R.drawable.textinputborder);


                }
            }
        }
    }


    private void findChildViewAndApplyHighestwidth(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) vg.getChildAt(i);
                findChildViewAndApplyHighestwidth(viewGroup);
            } else {
                final View v = vg.getChildAt(i);
                final int applywidth = this.heighestWidth;
                ViewGroup.LayoutParams param = v.getLayoutParams();
                param.width = applywidth;

                v.setLayoutParams(param);
            }
        }
    }

    private void findChildViewgetHighestWidth(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) vg.getChildAt(i);
                findChildViewgetHighestWidth(viewGroup);
            } else {
                View v = vg.getChildAt(i);
                int viewwidth = v.getWidth();

                this.heighestWidth = (this.heighestWidth > viewwidth) ? this.heighestWidth : viewwidth;
            }
        }
    }

    private RadioGroup generateRadioGroup(int numberOfRadioButton, int currentIndex) {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

        radioGroup.setTag("" + currentIndex);
        for (int i = 0; i < numberOfRadioButton; i++) {
            int[] attrs = {android.R.attr.listChoiceIndicatorSingle};

            RadioButton rb = new RadioButton(getContext());
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs);
            Drawable indicator = ta.getDrawable(0);
            rb.setGravity(Gravity.CENTER);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, indicator);//?android:attr/listChoiceIndicatorSingle
            rb.setBottom(android.R.drawable.btn_radio);
            rb.setButtonDrawable(null);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rb.setLayoutParams(params);

            radioGroup.addView(rb);

        }

        return radioGroup;

    }

    private LinearLayout generateRow(String rowTitle, int currentIndex) {
        numberOFradioButton = q_data.getColumns().size();
        LinearLayout rowlayout = new LinearLayout(getContext());
        rowlayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView rowText = new TextView(getContext());

        rowText.setText(rowTitle);
        rowText.setPadding(DEFAULTPADDING, DEFAULTPADDING, DEFAULTPADDING, DEFAULTPADDING);
        rowText.setGravity(Gravity.CENTER);
        rowText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowlayout.addView(rowText);


        RadioGroup radioGroup = generateRadioGroup(numberOFradioButton, currentIndex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("onCheckedChanged: ", group.getTag() + "" + checkedId + "");
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        Toast.makeText(getActivity(), q_data.getColumns().get(i), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        });
        rowlayout.addView(radioGroup);


        return rowlayout;
    }

    private LinearLayout generateFirstRow(ArrayList<String> columnList) {

        LinearLayout rowlayout = new LinearLayout(getContext());
        rowlayout.setOrientation(LinearLayout.HORIZONTAL);
        rowlayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        rowlayout.setGravity(Gravity.CENTER_VERTICAL);

        Space space = new Space(getContext());

        space.setLayoutParams(new ViewGroup.LayoutParams(FIRSTCOLUMNWIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowlayout.addView(space); //add column for the first view
        for (int i = 0; i < columnList.size(); i++) {
            TextView columnText = new TextView(getContext());
            columnText.setText(columnList.get(i));
            columnText.setPadding(DEFAULTPADDING, DEFAULTPADDING, DEFAULTPADDING, DEFAULTPADDING);

            columnText.setGravity(Gravity.CENTER);
            columnText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rowlayout.addView(columnText);

        }


        return rowlayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
