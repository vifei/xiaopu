package com.example.xiaopu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.xiaopu.adapter.MsgAdapter;
import com.example.xiaopu.bean.Msg;
import com.example.xiaopu.bean.Talks;
import com.example.xiaopu.utils.HttpUtils;
import com.example.xiaopu.utils.TalksDbOpenHelper;
import com.example.xiaopu.utils.ZhiPu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Talk extends Fragment {

    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private ImageButton send;
    private ImageButton voice;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;
    private View rootView;
    List<Talks> talksList = new ArrayList<>();
    private static final ZhiPu zhipu = new ZhiPu();

    private SharedPreferences sharedPreferences;
    private int uid;


    private Future<Boolean> future;
    private Future<Boolean> future_gettalks;
    private TalksDbOpenHelper talksDbOpenHelper;


    private ThreadPoolExecutor  executor = new ThreadPoolExecutor(5, 10, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());

    class MyHandler extends Handler {
        private String content = "";

        public MyHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("handler", String.valueOf(msg.what));
            if (msg.what == 1) {
                this.content = this.content + msg.obj.toString();
                Msg mymsg = new Msg(content, Msg.TYPE_RECEIVED);
                if (msgList.get(msgList.size() - 1 ).getType() == Msg.TYPE_SEND) {
                    msgList.add(mymsg);
                }
                else {
                    msgList.get(msgList.size() - 1).setContent(content);
                }
                adapter.notifyItemChanged(msgList.size()-1);
            } else if (msg.what == 2) {
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        }



        Context context = getActivity();
        talksDbOpenHelper = new TalksDbOpenHelper(context);
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_data = sharedPreferences.getString("user_data", "");
        try {
            JSONObject jsonObject = new JSONObject(user_data);
            uid = jsonObject.getInt("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        msgRecyclerView = rootView.findViewById(R.id.msg_recycler_view);
        inputText = rootView.findViewById(R.id.input_text);
        send = rootView.findViewById(R.id.send);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        adapter = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!content.equals("")) {
                    msgList.add(new Msg(content,Msg.TYPE_SEND));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");//清空输入框中的内容
//                    zhipu.Sse(msgList.get(msgList.size() - 1).getContent(), new MyObserver(), handler);

                    future = executor.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            send.setEnabled(false);
                            zhipu.Sse(msgList.get(msgList.size() - 1).getContent(), new MyHandler(Looper.getMainLooper()));


                            List<Msg> list = new ArrayList<>();
                            list.add(msgList.get(msgList.size() - 2));
                            list.add(msgList.get(msgList.size() - 1));

                            // 使用 org.json 库将 List 转换为 JSON 字符串
                            JSONArray jsonArray = new JSONArray();
                            for (Msg msg : list) {
                                JSONObject msgJson = new JSONObject();
                                try {
                                    msgJson.put("type", msg.getType());
                                    msgJson.put("content", msg.getContent());
                                    msgJson.put("uid", 10);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                jsonArray.put(msgJson);
                            }

                            String json = jsonArray.toString();
                            String response = HttpUtils.postJsonContent("http://849p815u54.zicp.fun:48340/talks/save", json);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("code") == 200) {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject talkObject = dataArray.getJSONObject(i);

                                        Talks talks = new Talks();
                                        talks.setId(String.valueOf(talkObject.getInt("id")));
                                        talks.setType(String.valueOf(talkObject.getInt("type")));
                                        talks.setContent(talkObject.getString("content"));
                                        talks.setUid(String.valueOf(talkObject.getInt("uid")));
                                        talksDbOpenHelper.insertData(talks);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            send.setEnabled(true);
                            return true;
                        }
                    });
                }
            }


        });

        String url = "http://849p815u54.zicp.fun:48340/talks/list?uid=" + uid;
        future_gettalks = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String response = HttpUtils.getJsonContent(url);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("code") == 200) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject talkObject = dataArray.getJSONObject(i);

                            Talks talks = new Talks();
                            talks.setId(String.valueOf(talkObject.getInt("id")));
                            talks.setType(String.valueOf(talkObject.getInt("type")));
                            talks.setContent(talkObject.getString("content"));
                            talks.setUid(String.valueOf(talkObject.getInt("uid")));

                            talksList.add(talks);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });



        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();


        try {
            if (future_gettalks.get()) {

                List<Talks> list = talksDbOpenHelper.queryAllFromDbByUid(String.valueOf(uid));
                if (!list.equals(talksList)) {
                    int length_1 = talksList.size();
                    int length_2 = list.size();
                    if (length_1 > length_2) {
                        for (int i = length_2; i < length_1; i++) {
                            talksDbOpenHelper.insertData(talksList.get(i));
                        }
                    } else if (length_1 < length_2) {
                        talksDbOpenHelper.deleteByUid(String.valueOf(uid));
                        for (int i = 0; i < length_1; i++) {
                            talksDbOpenHelper.insertData(talksList.get(i));
                        }
                    }

                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        adapter.setList(msgList = getData());


    }


    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        for (Talks talks : talksList) {
            // 创建 Msg 对象并设置 content 和 type，忽略 id 和 uid
            Msg msg = new Msg(talks.getContent(), talks.getType().equals("0") ? Msg.TYPE_RECEIVED : Msg.TYPE_SEND);
            list.add(msg);
        }

        list.add(new Msg("您好，我是小朴，很高兴为您提供情感陪伴和支持。请问您有什么问题或烦恼想要分享呢？我们可以一起来面对和解决。",Msg.TYPE_RECEIVED));
        return list;
    }
}