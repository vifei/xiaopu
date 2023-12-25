package com.example.xiaopu.exam;

public class Question {
    private String testcontent;
    private String answera;
    private String answerb;
    private int testid;
    private int selectedAnswer;

    public Question() {

    }

    @Override
    public String toString() {
        return "Question{" +
                "testcontent='" + testcontent + '\'' +
                ", answera='" + answera + '\'' +
                ", answerb='" + answerb + '\'' +
                ", testid=" + testid +
                ", selectedAnser=" + selectedAnswer +
                '}';
    }

    public String getTestcontent() {
        return testcontent;
    }

    public void setTestcontent(String testcontent) {
        this.testcontent = testcontent;
    }

    public String getAnswera() {
        return answera;
    }

    public void setAnswera(String answera) {
        this.answera = answera;
    }

    public String getAnswerb() {
        return answerb;
    }

    public void setAnswerb(String answerb) {
        this.answerb = answerb;
    }

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Question(String testcontent, String answera, String answerb, int testid, int selectedAnswer) {
        this.testcontent = testcontent;
        this.answera = answera;
        this.answerb = answerb;
        this.testid = testid;
        this.selectedAnswer = selectedAnswer;
    }
}