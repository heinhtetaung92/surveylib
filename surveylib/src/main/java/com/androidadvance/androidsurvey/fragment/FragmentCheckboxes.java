package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Choice;
import com.androidadvance.androidsurvey.models.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FragmentCheckboxes extends Fragment {

    private Question q_data;
    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private EditText txt_new_suggestion;
    private LinearLayout linearLayout_checkboxes;
    private final ArrayList<CheckBox> allCb = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_checkboxes, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        txt_new_suggestion = (EditText) rootView.findViewById(R.id.new_suggestion);
        linearLayout_checkboxes = (LinearLayout) rootView.findViewById(R.id.linearLayout_checkboxes);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SurveyActivity) mContext).go_to_next(null);
            }
        });

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        String the_choices = "";
        boolean at_leaset_one_checked = false;
        for (CheckBox cb : allCb) {
            if (cb.isChecked()) {
                at_leaset_one_checked = true;
                the_choices = the_choices + cb.getTag() + ",";
            }
        }

        if (the_choices.length() > 2) {
            the_choices = the_choices.substring(0, the_choices.length() - 1);
            Answers.getInstance().put_answer(q_data.getId(), the_choices);
            Log.i("Choices", the_choices);
        }else{
            Answers.getInstance().put_answer(q_data.getId(), the_choices);
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

        textview_q_title.setText(q_data != null ? q_data.getQuestionTitle() : "");

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
        }

        List<Choice> qq_data = q_data.getChoices();
        if (q_data.getRandomChoices()) {
            Collections.shuffle(qq_data);
        }

        String answer = Answers.getInstance().get_answer(q_data.getId(), "");
        Log.i("Answer", answer);
        List<String> answerList = Arrays.asList(answer.split(","));

        allCb.clear();
        for (final Choice choice : qq_data) {
            CheckBox cb = new CheckBox(mContext);
            cb.setText(Html.fromHtml(choice.getValue() == null ? "" : choice.getValue()));
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cb.setTag(choice.getId());
            if(answerList.contains(choice.getId())){
                cb.setChecked(true);
            }else{
                cb.setChecked(false);
            }
            linearLayout_checkboxes.addView(cb);
            allCb.add(cb);
            if(choice.allowNewSuggestion()) {
                if (cb.isChecked()) {
                    //show input box
                    txt_new_suggestion.setVisibility(View.VISIBLE);
                    String new_suggestion = Answers.getInstance().get_answer(q_data.getId()+"_"+choice.getId(), "");
                    txt_new_suggestion.setText(new_suggestion);
                } else {
                    txt_new_suggestion.setVisibility(View.GONE);
                }

                txt_new_suggestion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        Answers.getInstance().put_answer(q_data.getId()+"_"+choice.getId(), editable.toString());
                    }
                });

            }else{
                txt_new_suggestion.setVisibility(View.GONE);
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(choice.allowNewSuggestion()) {
                        if (isChecked) {
                            //show input box
                            txt_new_suggestion.setVisibility(View.VISIBLE);
                        } else {
                            txt_new_suggestion.setVisibility(View.GONE);
                            //Answers.getInstance().clear_answer(q_data+"_"+choice.getId());
                        }
                    }

                    collect_data();
                }
            });
        }



    }


}