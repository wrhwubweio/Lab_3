package com.example.lab_2;

import java.util.ArrayList;

public class Question {
    private String question;
    private int index;
    private ArrayList<String> answers;

    public Question(String question, ArrayList<String> questions) {
        this.question = question;
        this.answers = questions;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
