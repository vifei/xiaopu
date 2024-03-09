package com.example.xiaopu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaopu.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {


    //控件
    private EditText et_account;
    private EditText et_name;
    private EditText et_password;
    private EditText et_password_again;
    private Button btn_register;

    private String msg;

    //全局变量
    private boolean password_currect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        filter();
        initEvent();
    }

    public void initView(){
        et_account = this.findViewById(R.id.edt_login_account);
        et_name = this.findViewById(R.id.edt_login_name);
        et_password = this.findViewById(R.id.edt_login_password);
        et_password_again = this.findViewById(R.id.edt_login_password_again);
        btn_register = this.findViewById(R.id.btn_register);
    }

    public void filter() {
        // 设置输入长度限制
        int maxLength = 16;
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        et_account.setFilters(filters);
        et_name.setFilters(filters);
        et_password.setFilters(filters);
        et_password_again.setFilters(filters);
    }

    public void initEvent(){
        //给登录按钮添加点击事件(登录)
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户名和密码
                String number = et_account.getText().toString();
                String name = et_name.getText().toString();
                String password = et_password.getText().toString();
                if(!et_password.getText().toString().equals(et_password_again.getText().toString())) {
                    Toast.makeText(Register.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonData  = new JSONObject();
                try {
                    jsonData.put("number", number);
                    jsonData.put("name", name);
                    jsonData.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                String jsonString = jsonData.toString();
                //调用API验证用户名密码是否正确
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = HttpUtils.postJsonContent("http://849p815u54.zicp.fun:80/user/register", jsonString);
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            if(jsonObject.getInt("code") == 200) {
                                password_currect = true;
                            } else {
                                msg = jsonObject.getString("msg");
                                password_currect = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //登录事件
                if(password_currect) {
                    Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Main.class);
                    startActivity(intent);
                    Register.this.finish();
                } else {
                    Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}