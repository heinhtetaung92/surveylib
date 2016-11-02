package androidadvance.com.androidsurveyexample;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kaungkhantthu on 10/28/16.
 */

public class SampleActivity extends AppCompatActivity {
    private multiQuestion _multiQuestion;
    private LinearLayout mainLayout;
    private int numberOFradioGroup;
    private int numberOFradioButton;
    private int FIRSTCOLUMNWIDTH = 80;
    private int heighestWidth = 0;
    // private int NORMALCOLUMNWIDTH = 150;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radiotable_layout);
        setup_multiQuestion();
        mainLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);

        numberOFradioGroup = _multiQuestion.getRows().size();

        mainLayout.addView(generateFirstRow(_multiQuestion.getColumns())); //create the first row
        for (int i = 0; i < numberOFradioGroup; i++) {
            LinearLayout rowLayout = generateRow(_multiQuestion.getRows().get(i), i);
            mainLayout.addView(rowLayout);
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


                if (v instanceof RadioButton) {

                }
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {


        findChildViewgetHighestWidth(mainLayout);
        findChildViewAndApplyHighestwidth(mainLayout);
    }

    public void setup_multiQuestion() {
        String questionName = "what is your breakfast";
        ArrayList<String> rowlist = new ArrayList<>();
        ArrayList<String> columnList = new ArrayList<>();

        rowlist.add("foodsdadsdsdsdsd");
        rowlist.add("drinksdsdsd");

        columnList.add("a littlsde");
        columnList.add("toomsdsdsdsjdbsdjbuch");

        this._multiQuestion = new multiQuestion(questionName, rowlist, columnList);
    }

    private RadioGroup generateRadioGroup(int numberOfRadioButton, int currentIndex) {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setTag("" + currentIndex);
        for (int i = 0; i < numberOfRadioButton; i++) {
            int[] attrs = { android.R.attr.listChoiceIndicatorSingle };

            RadioButton rb = new RadioButton(this);

            TypedArray ta = this.getTheme().obtainStyledAttributes(attrs);
            Drawable indicator = ta.getDrawable(0);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, indicator);//?android:attr/listChoiceIndicatorSingle


            rb.setBottom(android.R.drawable.btn_radio);
            rb.setButtonDrawable(null);
            rb.setGravity(Gravity.CENTER_VERTICAL);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rb.setLayoutParams(params);
            radioGroup.addView(rb);

        }

        return radioGroup;

    }

    private LinearLayout generateRow(String rowTitle, int currentIndex) {
        numberOFradioButton = _multiQuestion.getColumns().size();
        LinearLayout rowlayout = new LinearLayout(this);
        rowlayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView rowText = new TextView(this);
        rowText.setText(rowTitle);
        rowText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowlayout.addView(rowText);


        RadioGroup radioGroup = generateRadioGroup(numberOFradioButton, currentIndex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("onCheckedChanged: ", group.getTag() + "" + checkedId + "");
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        Toast.makeText(SampleActivity.this, _multiQuestion.getColumns().get(i), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        });
        rowlayout.addView(radioGroup);


        return rowlayout;
    }

    private LinearLayout generateFirstRow(ArrayList<String> columnList) {

        LinearLayout rowlayout = new LinearLayout(this);
        rowlayout.setOrientation(LinearLayout.HORIZONTAL);

        Space space = new Space(this);

        space.setLayoutParams(new ViewGroup.LayoutParams(FIRSTCOLUMNWIDTH, ViewGroup.LayoutParams.MATCH_PARENT));
        rowlayout.addView(space); //add column for the first view
        for (int i = 0; i < columnList.size(); i++) {
            TextView columnText = new TextView(this);
            columnText.setText(columnList.get(i));
            columnText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rowlayout.addView(columnText);

        }

        ;


        return rowlayout;
    }
}
