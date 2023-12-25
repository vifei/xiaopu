package com.example.xiaopu;

import java.util.ArrayList;
import android.app.AlertDialog;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.xiaopu.exam.DBService;
import com.example.xiaopu.exam.Question;


public class Exam extends Activity {
    private int count;
    private int current;
    private boolean wrongMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        DBService dbService=new DBService();
        final List<Question> list=dbService.getQuestions();

        count=list.size();
        current=0;
//问题描述
        for (int i = 0; i < count; i++) {
            Log.d("listlist", list.get(i).toString());
        }
        final TextView tv_question=(TextView) findViewById(R.id.question);

        final RadioButton[]radioButtons=new RadioButton[2];

        //四个选项按钮

        radioButtons[0]=(RadioButton) findViewById(R.id.AnswerA);
        radioButtons[1]=(RadioButton) findViewById(R.id.AnswerB);


        //两个前一题和后一题的按钮

        Button btn_next=(Button) findViewById(R.id.btn_next);
        Button btn_previous=(Button) findViewById(R.id.btn_previous);
        final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.radioGroup);

        //将第一题先赋值
        Question q=list.get(0);
        tv_question.setText(q.getTestcontent());
        radioButtons[0].setText("A."+q.getAnswera());
        radioButtons[1].setText("B."+q.getAnswera());


        //下一题

        btn_next.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String result = TestResult(list);

                if(current<count-1){
                    Log.d("click", "onClick: ");
                    current++;
//list是存储对象的数组，通过get(i)获得每个对象

                    Question q=list.get(current);


//将所用的question对象各个属性归位。

                    tv_question.setText(q.getTestcontent());
                    radioButtons[0].setText("A."+q.getAnswera());
                    radioButtons[1].setText("B."+q.getAnswera());


                    radioGroup.clearCheck();

                    if(q.getSelectedAnswer()!=-1){
                        radioButtons[q.getSelectedAnswer()].setChecked(true);
                    }

                }
                else
                {
                    final List<Integer> wrongList = checkAnswer(list);
                    if(wrongList.size() == 0)
                    {
                        new AlertDialog.Builder(Exam.this)
                                .setTitle("测试完成")
                                .setMessage("您的MBTI人格是" + result)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        Exam.this.finish();
                                        Intent intent = new Intent(Exam.this, Exam.class);
                                        startActivity(intent);

                                        // 关闭当前实例
                                        finish();
                                    }
                                })
                                .show();
                    }
                    else {
                        new AlertDialog.Builder(Exam.this)
                                .setTitle("提示")
                                .setMessage("您还有" + wrongList.size() +
                                        "道题目没有完成")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }

            }
        });


        //前一个按钮
        btn_previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(current>0){
                    current--;
                    Question q=list.get(current);

                    tv_question.setText(q.getTestcontent());

                    radioButtons[0].setText("A."+q.getAnswera());
                    radioButtons[1].setText("B."+q.getAnswera());


                    radioGroup.clearCheck();
                    if(q.getSelectedAnswer()!=-1){
                        radioButtons[q.getSelectedAnswer()].setChecked(true);
                    }

                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<2;++i){
                    if(radioButtons[i].isChecked()==true){

                        list.get(current).setSelectedAnswer(i);
                        break;
                    }
                }
            }
        });


    }

    public String  TestResult(List<Question> list) {
        int categoryE = 0, categoryI = 0, categoryN = 0, categoryS = 0, categoryF = 0, categoryT = 0, categoryJ = 0, categoryP = 0;

        for (Question question : list) {
            if (question.getSelectedAnswer() == 0) { // A
                if (question.getTestid() >= 1 && question.getTestid() <= 7) {
                    categoryE++;
                } else if (question.getTestid() >= 8 && question.getTestid() <= 14) {
                    categoryN++;
                } else if (question.getTestid() >= 15 && question.getTestid() <= 21) {
                    categoryF++;
                } else if (question.getTestid() >= 22 && question.getTestid() <= 28) {
                    categoryJ++;
                }
            } else if (question.getSelectedAnswer() == 1) { // B
                if (question.getTestid() >= 1 && question.getTestid() <= 7) {
                    categoryI++;
                } else if (question.getTestid() >= 8 && question.getTestid() <= 14) {
                    categoryS++;
                } else if (question.getTestid() >= 15 && question.getTestid() <= 21) {
                    categoryT++;
                } else if (question.getTestid() >= 22 && question.getTestid() <= 28) {
                    categoryP++;
                }
            }
        }

        // Determine the dominant category for each group
        String result = determineDominantCategory(categoryE, categoryI, "E", "I") + " " +
                determineDominantCategory(categoryN, categoryS, "N", "S") + " " +
                determineDominantCategory(categoryF, categoryT, "F", "T") + " " +
                determineDominantCategory(categoryJ, categoryP, "J", "P");


        return result;
    }

    private String determineDominantCategory(int categoryA, int categoryB, String A, String B) {
        return (categoryA > categoryB) ? A : B;
    }



    private List<Integer> checkAnswer(List<Question> list ){
        List<Integer> wrongList=new ArrayList<Integer>();

        for(int i=0;i<list.size();++i)
        {
            if(list.get(i).getSelectedAnswer() == -1)
            {
                wrongList.add(i);
            }
        }
        return wrongList;
    }


}
