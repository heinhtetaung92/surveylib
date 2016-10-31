package androidadvance.com.androidsurveyexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kaungkhantthu on 10/31/16.
 */

public class MultiRadioGroupFragment extends Fragment {
    private multiQuestion _multiQuestion;
    private LinearLayout mainLayout;
    private int numberOFradioGroup;
    private int numberOFradioButton;
    private int FIRSTCOLUMNWIDTH = 80;
    private int heighestWidth = 0;
    // private int NORMALCOLUMNWIDTH = 150;
    public MultiRadioGroupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.radiotable_layout,container,false);
        setup_multiQuestion();
        mainLayout = (LinearLayout) v.findViewById(R.id.mainLinearLayout);

        numberOFradioGroup = _multiQuestion.getRows().size();

        mainLayout.addView(generateFirstRow(_multiQuestion.getColumns())); //create the first row
        for (int i = 0; i < numberOFradioGroup; i++) {
            LinearLayout rowLayout = generateRow(_multiQuestion.getRows().get(i), i);
            mainLayout.addView(rowLayout);
        }
        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findChildViewgetHighestWidth(mainLayout);
        findChildViewAndApplyHighestwidth(mainLayout);
    }
    private void setup_multiQuestion() {
        String questionName = "what is your breakfast";
        ArrayList<String> rowlist = new ArrayList<>();
        ArrayList<String> columnList = new ArrayList<>();

        rowlist.add("foodsdadsdsdsdsd");
        rowlist.add("drinksdsdsd");

        columnList.add("a littlsde");
        columnList.add("toomsdsdsdsjdbsdjbuch");

        this._multiQuestion = new multiQuestion(questionName, rowlist, columnList);
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

    private RadioGroup generateRadioGroup(int numberOfRadioButton, int currentIndex) {
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setTag("" + currentIndex);
        for (int i = 0; i < numberOfRadioButton; i++) {
            RadioButton rbtn = new RadioButton(getContext());
            rbtn.setGravity(Gravity.CENTER);

            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(rbtn);

        }

        return radioGroup;

    }

    private LinearLayout generateRow(String rowTitle, int currentIndex) {
        numberOFradioButton = _multiQuestion.getColumns().size();
        LinearLayout rowlayout = new LinearLayout(getContext());
        rowlayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView rowText = new TextView(getContext());
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
                        Toast.makeText(getActivity(), _multiQuestion.getColumns().get(i), Toast.LENGTH_SHORT).show();
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

        Space space = new Space(getContext());

        space.setLayoutParams(new ViewGroup.LayoutParams(FIRSTCOLUMNWIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowlayout.addView(space); //add column for the first view
        for (int i = 0; i < columnList.size(); i++) {
            TextView columnText = new TextView(getContext());
            columnText.setText(columnList.get(i));
            columnText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rowlayout.addView(columnText);

        }

        ;


        return rowlayout;
    }
}
