package com.androidadvance.androidsurvey.fragment;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.Space;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.customRadioGroup;
import com.androidadvance.androidsurvey.models.Question;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by kaungkhantthu on 10/31/16.
 */

public class FragmentMultiRadioGroup extends Fragment implements customRadioGroup.OnCheckedChangeListener {

    private static final int FIRSTCOLUMNWIDTH = 80;
    private static FragmentMultiRadioGroup multiRadioFragment;
    private LinearLayout mainLayout;
    private int numberOFradioGroup;
    private int numberOFradioButton;
    private int DEFAULTPADDING = 20;
    private int heighestWidth = 240;
    private Question q_data;
    public SparseArray<String> answerlist = new SparseArray<>();
    public SparseArray<String> mainAnswerlist = new SparseArray<>();
    private FragmentActivity mContext;
    private TextView txt_question;
    private LinearLayout linearFirstRow;
    private boolean isAnswersareAdded; //this boolean is use to handle the state of the on check change lisnter

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
        isAnswersareAdded = false;
        q_data = (Question) getArguments().getSerializable("data");
        mainAnswerlist = Answers.getInstance().get_answer(q_data.getId(), new SparseArray<String>());
        numberOFradioGroup = q_data.getRows().size();
        txt_question = (TextView) v.findViewById(R.id.textview_q_title);
        txt_question.setText(q_data.getQuestionTitle());
        mainLayout = (LinearLayout) v.findViewById(R.id.mainLinearLayout);
        linearFirstRow = (LinearLayout) v.findViewById(R.id.linear_firstRow);


        LinearLayout firstRow = generateFirstRow(q_data.getColumns());
        linearFirstRow.addView(firstRow);
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
        findChildViewAndApplyHighestwidth(mainLayout);      //for the whole table
        findChildViewAndApplyHighestwidth(linearFirstRow);  //for the first row
        if (mainAnswerlist.size() > 0) {
            checkAnswer();

        }

        //refillanswer(mainLayout);
        isAnswersareAdded = true;


        Log.e(TAG, "onViewCreated: ");

    }


    private void checkAnswer() {
        for (int i = 0; i < mainAnswerlist.size(); i++) {
            int rowTag = mainAnswerlist.keyAt(i);
            customRadioGroup group = (customRadioGroup) mainLayout.findViewWithTag(rowTag + "");
            String answertag = mainAnswerlist.get(rowTag);
            RadioButton rbtn = (RadioButton) group.findViewWithTag(answertag);
            rbtn.setChecked(true);

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


    private customRadioGroup generateCustomRadioGroup(int numberOfRadioButton, int columnindex) {
        customRadioGroup radioGroup = new customRadioGroup(getContext());
        radioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

        radioGroup.setTag("" + columnindex);
        for (int i = 0; i < numberOfRadioButton; i++) {
            int[] attrs = {android.R.attr.listChoiceIndicatorSingle};
            LinearLayout wrapLayout = new LinearLayout(getContext());

            wrapLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            wrapLayout.setBackgroundResource(R.drawable.textinputborder);
            wrapLayout.setGravity(Gravity.CENTER);
            RadioButton rb = new RadioButton(getContext());
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs);
            Drawable indicator = ta.getDrawable(0);

            rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, indicator);//?android:attr/listChoiceIndicatorSingle
            rb.setBottom(android.R.drawable.btn_radio);
            rb.setButtonDrawable(null);
            rb.setTag(columnindex + "" + i);
            LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            rb.setLayoutParams(params);
            rb.setGravity(Gravity.CENTER);
            wrapLayout.addView(rb);

            radioGroup.addView(wrapLayout);

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
        rowText.setBackgroundResource(R.drawable.textinputborder);
        rowlayout.addView(rowText);


        customRadioGroup radioGroup = generateCustomRadioGroup(numberOFradioButton, currentIndex);
        radioGroup.setOnCheckedChangeListener(this);


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
            columnText.setBackgroundResource(R.drawable.textinputborder);
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

    @Override
    public void onCheckedChanged(customRadioGroup group, int checkedId) {
        Log.e("onCheckedChanged: ", group.getTag() + "" + checkedId + "");
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i) instanceof LinearLayout) {
                LinearLayout wrapLayout = (LinearLayout) group.getChildAt(i);

                if (wrapLayout.getChildAt(0).getId() == checkedId) {
                    if (isAnswersareAdded) {
                        String answerKey = (String) ((RadioButton) wrapLayout.getChildAt(0)).getTag();
                        int column = Integer.parseInt("" + group.getTag());
                        answerlist.put(column, answerKey);
                        Answers.getInstance().put_answer(q_data.getId(), answerlist);

                        break;
                    }
                }
            }
        }

    }
}
