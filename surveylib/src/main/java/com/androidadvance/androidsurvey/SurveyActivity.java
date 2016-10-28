package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.androidadvance.androidsurvey.fragment.FragmentCheckboxes;
import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentLinearScale;
import com.androidadvance.androidsurvey.fragment.FragmentMultiline;
import com.androidadvance.androidsurvey.fragment.FragmentNumber;
import com.androidadvance.androidsurvey.fragment.FragmentRadioboxes;
import com.androidadvance.androidsurvey.fragment.FragmentStart;
import com.androidadvance.androidsurvey.fragment.FragmentTextSimple;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.models.SurveyPojo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    private SurveyPojo mSurveyPojo;
    private FrameLayout mContainer;
    //private ViewPager mPager;
    private String style_string = null;
    private int fragIndex = 0;
    ArrayList<Fragment> arraylist_fragments = new ArrayList<>();
    HashMap<String, Integer> skipMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_survey);



        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mSurveyPojo = new Gson().fromJson(bundle.getString("json_survey"), SurveyPojo.class);
            if (bundle.containsKey("style")) {
                style_string = bundle.getString("style");
            }
        }


        Log.i("json Object = ", String.valueOf(mSurveyPojo.getQuestions()));



        //- START -
        if (!mSurveyPojo.getSurveyProperties().getSkipIntro()) {
            FragmentStart frag_start = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable("survery_properties", mSurveyPojo.getSurveyProperties());
            sBundle.putString("style", style_string);
            frag_start.setArguments(sBundle);
            arraylist_fragments.add(frag_start);
        }

        //- FILL -
        for (Question mQuestion : mSurveyPojo.getQuestions()) {

            if (mQuestion.getQuestionType().equals("String")) {
                FragmentTextSimple frag = new FragmentTextSimple();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mQuestion.getQuestionType().equals("Checkboxes")) {
                FragmentCheckboxes frag = new FragmentCheckboxes();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mQuestion.getQuestionType().equals("Radioboxes")) {
                FragmentRadioboxes frag = new FragmentRadioboxes();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mQuestion.getQuestionType().equals("LinearScale")) {
                FragmentLinearScale frag = new FragmentLinearScale();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mQuestion.getQuestionType().equals("Number")) {
                FragmentNumber frag = new FragmentNumber();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

            if (mQuestion.getQuestionType().equals("StringMultiline")) {
                FragmentMultiline frag = new FragmentMultiline();
                Bundle xBundle = new Bundle();
                xBundle.putSerializable("data", mQuestion);
                xBundle.putString("style", style_string);
                frag.setArguments(xBundle);
                arraylist_fragments.add(frag);
            }

        }

        //- END -
        FragmentEnd frag_end = new FragmentEnd();
        Bundle eBundle = new Bundle();
        eBundle.putSerializable("survery_properties", mSurveyPojo.getSurveyProperties());
        eBundle.putString("style", style_string);
        frag_end.setArguments(eBundle);
        arraylist_fragments.add(frag_end);


        setFragmentToView(fragIndex);
        /*mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        AdapterFragmentQ mPagerAdapter = new AdapterFragmentQ(getSupportFragmentManager(), arraylist_fragments);
        mPager.setAdapter(mPagerAdapter);*/


    }

    public void setFragmentToView(int index){
        fragIndex = index;

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        fragTransaction.replace(R.id.container, arraylist_fragments.get(index) , "fragment" + index);
        fragTransaction.commit();
    }

    public void go_to_next(){
        go_to_next(null);
    }

    public void go_to_next(String nextObjId) {
        if(nextObjId == null) {
            setFragmentToView(fragIndex + 1);
            //mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }else{
            List<Question> questions = mSurveyPojo.getQuestions();
            int currentPos = fragIndex;
            for(;currentPos<questions.size();currentPos++){
                Question question = questions.get(currentPos);
                if(question.getId().equals(nextObjId)){
                    skipMap.put(nextObjId, currentPos - fragIndex);
                    setFragmentToView(currentPos);
                    break;
                }

            }
        }
    }


    @Override
    public void onBackPressed() {
        if (fragIndex == 0) {
            super.onBackPressed();
        } else {
            List<Question> questions = mSurveyPojo.getQuestions();
            Question currQuestion = questions.get(fragIndex);

            try {
                int skipCount = skipMap.get(currQuestion.getId());
                Log.i("SkipCount", skipCount + "");
                skipMap.remove(currQuestion.getId());
                setFragmentToView(fragIndex - skipCount);
            }catch (Exception ex){
                Log.i("SkipCount", "0");
                setFragmentToView(fragIndex - 1);
            }
        }
    }

    public void event_survey_completed(Answers instance) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("answers", instance.get_json_object());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
