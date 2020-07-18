package com.appcommandos.historica.model;

public class Question {
    private String answer;
    private boolean snswerTrue;

    public Question(){

    }
    public Question(String answer, boolean snswerTrue) {
        this.answer = answer;
        this.snswerTrue = snswerTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSnswerTrue() {
        return snswerTrue;
    }

    public void setSnswerTrue(boolean snswerTrue) {
        this.snswerTrue = snswerTrue;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", snswerTrue=" + snswerTrue +
                '}';
    }
}
