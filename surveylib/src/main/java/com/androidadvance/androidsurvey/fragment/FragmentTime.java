package com.androidadvance.androidsurvey.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Question;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hein Htet on 10/28/2016.
 */

public class FragmentTime extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private FragmentActivity mContext;
    private Button button_continue;
    private TextView textview_q_title;
    private EditText editText_answer;
    Question q_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_text_simple, container, false);

        button_continue = (Button) rootView.findViewById(R.id.button_continue);
        textview_q_title = (TextView) rootView.findViewById(R.id.textview_q_title);
        editText_answer = (EditText) rootView.findViewById(R.id.editText_answer);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Answers.getInstance().put_answer(q_data.getId(), editText_answer.getText().toString().trim());
                ((SurveyActivity) mContext).go_to_next();
            }
        });

        return rootView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        q_data = (Question) getArguments().getSerializable("data");
        String answer = Answers.getInstance().get_answer(q_data.getId(), "");

        if (q_data.getRequired()) {
            button_continue.setVisibility(View.GONE);
            /*editText_answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        button_continue.setVisibility(View.VISIBLE);
                    } else {
                        button_continue.setVisibility(View.GONE);
                    }
                }
            });*/

        }

        editText_answer.setFocusable(false);
        editText_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
                Toast.makeText(mContext, "Focused", Toast.LENGTH_SHORT).show();
            }
        });
        textview_q_title.setText(Html.fromHtml(q_data.getQuestionTitle()));
        editText_answer.setText(answer);

    }

    private void showDatePickerDialog()
    {
        if(editText_answer.getText().toString().isEmpty()){
            showDefaultDatePickerDialog(Calendar.getInstance());
        }else{
            try {
                String selected_Date = editText_answer.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date date = formatter.parse(selected_Date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                showDefaultDatePickerDialog(calendar);
            }catch (Exception ex){
                showDefaultDatePickerDialog(Calendar.getInstance());
            }
        }

    }

    private void showDefaultDatePickerDialog(Calendar calendar){
        new TimePickerDialog(mContext, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hrOfDay, int min) {
        button_continue.setVisibility(View.VISIBLE);
        editText_answer.setText(hrOfDay + ":" + min);
    }
}
