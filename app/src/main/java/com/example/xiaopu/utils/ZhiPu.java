//package com.example.xiaopu.utils;
//
//import static com.zhipu.oapi.demo.V4OkHttpClientTest.mapStreamToAccumulator;
//
//
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//
//import android.os.Handler;
//
//import com.example.xiaopu.adapter.MsgAdapter;
//import com.zhipu.oapi.ClientV4;
//import com.zhipu.oapi.Constants;
//import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
//import com.zhipu.oapi.service.v4.model.ChatMessage;
//import com.zhipu.oapi.service.v4.model.ChatMessageRole;
//import com.zhipu.oapi.service.v4.model.ModelApiResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class ZhiPu {
//    // 请先填写自己的apiKey再调用demo查看效果
//
//
//    private static final String API_KEY = "656b355d0dc5b84c1468c8e487aee219";
//
//    private static final String API_SECRET = "dArvqLVMbbQ1uNqC";
//    private static final String requestIdTemplate = "xiaopu-%d";
//
//
//    private static final ClientV4 client = new ClientV4.Builder(API_KEY,API_SECRET).build();
//
//
//    public void Sse(String content, Handler myHandler) {
//
//
//
//        List<ChatMessage> messages = new ArrayList<>();
//        ChatMessage chatMessage1 = new ChatMessage(ChatMessageRole.ASSISTANT.value(), "假设你是一位提供情感陪伴和支持的好朋友，你的名字叫小朴。请根据问题回答真实有用的答复");
//        ChatMessage chatMessage2 = new ChatMessage(ChatMessageRole.SYSTEM.value(), "您好，我是小朴，很高兴为您提供情感陪伴和支持。请问您有什么问题或烦恼想要分享呢？我们可以一起来面对和解决。");
//        ChatMessage chatMessage3 = new ChatMessage(ChatMessageRole.USER.value(), content);
//
//        messages.add(chatMessage1);
//        messages.add(chatMessage2);
//        messages.add(chatMessage3);
//        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
//
//
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .model(Constants.ModelChatGLM4)
//                .stream(Boolean.TRUE)
//                .messages(messages)
//                .requestId(requestId)
//                .toolChoice("auto")
//                .build();
//        ModelApiResponse sseModelApiResp = client.invokeModelApi(chatCompletionRequest);
//        if (sseModelApiResp.isSuccess()) {
//            AtomicBoolean isFirst = new AtomicBoolean(true);
//            mapStreamToAccumulator(sseModelApiResp.getFlowable())
////                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext(accumulator -> {
////                                            if (isFirst.getAndSet(false)) {
////                                                // 这里可以处理第一次的逻辑
////                                            }
////                                            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null) {
////                                                // 处理 delta 不为 null 且 tool_calls 不为 null 的情况
////                                            }
//                        if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null) {
////                                                String msg = accumulator.getDelta().getContent();
//                            myHandler.sendMessage(myHandler.obtainMessage(1, accumulator.getDelta().getContent()));
////                                                ((Main)getActivity()).runOnUiThread
//                        }
//                    })
//                    .doOnComplete(()->{
//                        myHandler.sendMessage(myHandler.obtainMessage(2));
//                    }).subscribe();
//        }
////        if (sseModelApiResp.isSuccess()) {
////            new Thread(){
////                @Override
////                public void run() {
////                    AtomicBoolean isFirst = new AtomicBoolean(true);
////                    ChatMessageAccumulator chatMessageAccumulator = mapStreamToAccumulator(sseModelApiResp.getFlowable())
////                            .doOnNext(accumulator -> {
////                                {
////                                    new Thread() {
////                                        @Override
////                                        public void run() {
////                                            if (isFirst.getAndSet(false)) {
//////                                System.out.print("Response: ");
////                                            }
////                                            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null) {
//////                                String jsonString = mapper.writeValueAsString(accumulator.getDelta().getTool_calls());
//////                                System.out.println("tool_calls: " + jsonString);
////                                            }
////                                            if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null) {
//////                                System.out.print(accumulator.getDelta().getContent());
//////                                JSONObject jsonObject = JSON.parseObject(accumulator.getDelta().getContent());
////                                                String msg = accumulator.getDelta().getContent();
//////                                Log.d("hand", msg);
////                                                handler.sendMessage(handler.obtainMessage(1, msg));
//////                                msgAdapter.notifyItemChanged(position);
//////                                logger.info("data:{}", msg);
//////                                this.outputText = this.outputText + data;
////                                            }
////                                        }
////                                    }.start();
////                                }
////                            })
////                            .doOnComplete(() -> {
////                                handler.sendMessage(handler.obtainMessage(2));
////                            })
////                            .lastElement()
////                            .blockingGet();
////                }
////            }.start();
//
////            Choice choice = new Choice(chatMessageAccumulator.getChoice().getFinishReason(), 0L, chatMessageAccumulator.getDelta());
////            List<Choice> choices = new ArrayList<>();
////            choices.add(choice);
////            ModelData data = new ModelData();
////            data.setChoices(choices);
////            data.setUsage(chatMessageAccumulator.getUsage());
////            data.setId(chatMessageAccumulator.getId());
////            data.setCreated(chatMessageAccumulator.getCreated());
////            data.setRequestId(chatCompletionRequest.getRequestId());
////            sseModelApiResp.setFlowable(null);
////            sseModelApiResp.setData(data);
////        }
//    }
//
//}


package com.example.xiaopu.utils;

import android.os.Handler;

import com.zhipu.oapi.ClientV3;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.core.httpclient.OkHttpTransport;
import com.zhipu.oapi.service.v3.ModelApiRequest;
import com.zhipu.oapi.service.v3.ModelConstants;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

//import okhttp3.OkHttpClient;

//import okhttp3.OkHttpClient;

public class ZhiPu {
    // 请先填写自己的apiKey再调用demo查看效果
    public static OkHttpClient getInstance() {
        OkHttpClient okHttpClient=new OkHttpClient.Builder()//构建器
                .proxy(Proxy.NO_PROXY) //来屏蔽系统代理
                .retryOnConnectionFailure(true)
                .connectTimeout(300, TimeUnit.SECONDS)//连接超时
                .writeTimeout(300, TimeUnit.SECONDS)//写入超时
                .readTimeout(300, TimeUnit.SECONDS)//读取超时
                .build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(200); //设置最大并发请求数，避免等待延迟
        okHttpClient.dispatcher().setMaxRequests(200);
        return okHttpClient;
    }

    private static final ClientV3 client = new ClientV3.Builder("656b355d0dc5b84c1468c8e487aee219","dArvqLVMbbQ1uNqC")
            .httpTransport(new OkHttpTransport(getInstance()))
            //.devMode(true)
            .build();

    private static final String requestIdTemplate = "xiaopu-%d";

    public void Sse(String content, Handler handler) {
        ModelApiRequest modelApiRequest = new ModelApiRequest();
        modelApiRequest.setModelId("glm-3-turbo");
        modelApiRequest.setInvokeMethod(Constants.invokeMethodSse);
        // 可自定义sse listener
        MyStandardEventSourceListener listener = new MyStandardEventSourceListener();
        listener.setIncremental(true);
        listener.setHandler(handler);
        modelApiRequest.setSseListener(listener);


        // 构建prompt
        ModelApiRequest.Prompt prompt1 = new ModelApiRequest.Prompt(ModelConstants.roleUser, "假设你是一位提供情感陪伴和支持的好朋友，你的名字叫小朴。请根据问题回答真实有用的答复");
        ModelApiRequest.Prompt prompt2 = new ModelApiRequest.Prompt(ModelConstants.roleAssistant, "您好，我是小朴，很高兴为您提供情感陪伴和支持。请问您有什么问题或烦恼想要分享呢？我们可以一起来面对和解决。");
        ModelApiRequest.Prompt prompt3 = new ModelApiRequest.Prompt(ModelConstants.roleUser, content);
        List<ModelApiRequest.Prompt> prompts = new ArrayList<>();
        prompts.add(prompt1);
        prompts.add(prompt2);
        prompts.add(prompt3);
        modelApiRequest.setPrompt(prompts);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        modelApiRequest.setRequestId(requestId);

        client.invokeModelApi(modelApiRequest);
    }

}
