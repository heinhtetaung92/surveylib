package com.androidadvance.androidsurvey.fragment;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Choice;
import com.androidadvance.androidsurvey.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hein Htet on 10/28/2016.
 */

public class FragmentLinearScale extends Fragment {

    private Question q_data;
    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private RadioGroup radioGroup;
    private final ArrayList<RadioButton> allRb = new ArrayList<>();
    private boolean at_leaset_one_checked = false;
    private String nextObjId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_linearscale, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SurveyActivity) mContext).go_to_next(nextObjId);
            }
        });

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        String the_choice = "";
        at_leaset_one_checked = false;
        for (RadioButton rb : allRb) {
            if (rb.isChecked()) {
                at_leaset_one_checked = true;
                the_choice = (String) rb.getTag(R.string.objId);
                nextObjId = (String) rb.getTag(R.string.nextObjId);
            }
        }

        if (the_choice.length() > 0) {
            Answers.getInstance().put_answer(q_data.getId(), the_choice);
        }


        if (q_data.getRequired()) {
            if (at_leaset_one_checked) {
                button_continue.setVisibility(View.VISIBLE);
            } else {
                button_continue.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mContext = getActivity();
        q_data = (Question) getArguments().getSerializable("data");

        textview_q_title.setText(q_data.getQuestionTitle());

        String answer = Answers.getInstance().get_answer(q_data.getId(), "");

        List<Choice> qq_data = q_data.getChoices();
        if (q_data.getRandomChoices()) {
            Collections.shuffle(qq_data);
        }

        allRb.clear();
        radioGroup.removeAllViews();
        radioGroup.clearCheck();
        for (Choice choice : qq_data) {
            RadioButton rb = new RadioButton(mContext);
            rb.setText(Html.fromHtml(choice.getValue() == null ? "" : choice.getValue()));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            int[] attrs = { android.R.attr.listChoiceIndicatorSingle };
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs);
            Drawable indicator = ta.getDrawable(0);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, indicator);//?android:attr/listChoiceIndicatorSingle
            rb.setBottom(android.R.drawable.btn_radio);
            rb.setButtonDrawable(null);

            rb.setGravity(Gravity.CENTER);
            rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb.setTag(R.string.objId, choice.getId());
            rb.setTag(R.string.nextObjId, choice.getGoTo());
            radioGroup.addView(rb);
            allRb.add(rb);

            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    collect_data();
                }
            });
        }

        for(RadioButton rb : allRb) {
            if (answer.equals(rb.getTag(R.string.objId))) {
                rb.setChecked(true);
            } else {
                rb.setChecked(false);
            }
        }

        if (q_data.getRequired()) {
            if (at_leaset_one_checked) {
                button_continue.setVisibility(View.VISIBLE);
            } else {
                button_continue.setVisibility(View.GONE);
            }
        }


    }

}
