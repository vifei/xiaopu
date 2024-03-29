package com.example.xiaopu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaopu.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Login extends AppCompatActivity {

    //控件
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;

    //全局变量
    private boolean password_currect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        copyPrepopulatedDatabase();
    }

    private void copyPrepopulatedDatabase() {
        File databaseFile = getApplicationContext().getDatabasePath("mydatabase.db");
        if (!databaseFile.exists()) {
            // 如果数据库文件不存在，复制预填充数据库文件到内部存储
            try {
                InputStream inputStream = getApplicationContext().getAssets().open("databases/mydatabase.db");
                OutputStream outputStream = new FileOutputStream(databaseFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initView(){
        et_username = this.findViewById(R.id.ed_login_account);
        et_password = this.findViewById(R.id.edt_login_password);
        btn_login = this.findViewById(R.id.btn_login);
        btn_register = this.findViewById(R.id.btn_register);

    }

    public void initEvent(){
        //给登录按钮添加点击事件(登录)
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户名和密码
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                JSONObject jsonData  = new JSONObject();
                try {
                    jsonData.put("number", username);
                    jsonData.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                String jsonString = jsonData.toString();

                //调用API验证用户名密码是否正确
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = HttpUtils.postJsonContent("http://849p815u54.zicp.fun:80/user/login", jsonString);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if(jsonObject.getInt("code") == 200) {
                                JSONObject userDataJson = jsonObject.getJSONObject("data").getJSONObject("user");
                                //获取SharedPreferences对象
                                SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                                //获取Editor对象的引用
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //将获取过来的值放入文件
                                editor.putString("user_data", userDataJson.toString());
                                // 提交数据
                                editor.apply();

                                password_currect = true;
                            } else {
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
                    Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
                    Login.this.finish();
                } else {
                    Toast.makeText(Login.this, "密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                Login.this.finish();
            }
        });
    }

}