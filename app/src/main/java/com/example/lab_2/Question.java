package com.example.lab_2;

import java.util.ArrayList;

public class Question {
    private String question;
    private int index;
    private ArrayList<String> answers;
    private int given_answer;
    private int correct_answer;

    public Question(String question, ArrayList<String> questions, int correct_answer, int index) {
        this.question = question;
        this.answers = questions;
        this.correct_answer = correct_answer;
        this.index = index;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getGiven_answer() {
        return given_answer;
    }

    public void setGiven_answer(int given_answer) {
        this.given_answer = given_answer;
    }

    public boolean CheckCorrect(){
        return correct_answer == given_answer;
    }
}
