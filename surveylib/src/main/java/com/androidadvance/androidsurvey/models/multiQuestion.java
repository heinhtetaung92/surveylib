package com.androidadvance.androidsurvey.models;

import java.util.ArrayList;

/**
 * Created by kaungkhantthu on 10/28/16.
 */

public class multiQuestion {
    private String QuestionName;
    private ArrayList<String> rows;
    private ArrayList<String> columns;

    public String getQuestionName() {
        return QuestionName;
    }

    public void setQuestionName(String questionName) {
        QuestionName = questionName;
    }

    public multiQuestion(String questionName, ArrayList<String> rows, ArrayList<String> columns) {
        QuestionName = questionName;
        this.rows = rows;
        this.columns = columns;
    }

    public ArrayList<String> getRows() {
        return rows;
    }

    public void setRows(ArrayList<String> rows) {
        this.rows = rows;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
}
