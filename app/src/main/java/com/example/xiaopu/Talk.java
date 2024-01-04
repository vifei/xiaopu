package com.example.xiaopu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.xiaopu.adapter.MsgAdapter;
import com.example.xiaopu.bean.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Talk#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Talk extends Fragment {

    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private ImageButton send;
    private ImageButton voice;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;
    private View rootView;

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

        msgRecyclerView = rootView.findViewById(R.id.msg_recycler_view);
        inputText = rootView.findViewById(R.id.input_text);
        send = rootView.findViewById(R.id.send);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        adapter = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

/*       我们还需要为button建立一个监听器，我们需要将编辑框的内容发送到 RecyclerView 上：
            ①获取内容，将需要发送的消息添加到 List 当中去。
            ②调用适配器的notifyItemInserted方法，通知有新的数据加入了，赶紧将这个数据加到 RecyclerView 上面去。
            ③调用RecyclerView的scrollToPosition方法，以保证一定可以看的到最后发出的一条消息。*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!content.equals("")) {
                    msgList.add(new Msg(content,Msg.TYPE_SEND));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");//清空输入框中的内容
                }
//                自定义一问一答
                if(msgList.size() == 2){
                    msgList.add(new Msg("What's your name?",Msg.TYPE_RECEIVED));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
                if(msgList.size() == 4){
                    msgList.add(new Msg("Nice to meet you,Bye!",Msg.TYPE_RECEIVED));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
            }
        });

        return rootView;
    }

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        list.add(new Msg("Hello",Msg.TYPE_RECEIVED));
        return list;
    }
}