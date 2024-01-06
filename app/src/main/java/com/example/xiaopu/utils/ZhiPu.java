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

    private static ClientV3 client = new ClientV3.Builder("092257dd28de805e0b37f10b7761c707","6JrwI26Td12V3PDG")
            .httpTransport(new OkHttpTransport(getInstance()))
            //.devMode(true)
            .build();

    private static final String requestIdTemplate = "xiaopu-%d";

    public void Sse(String content, Handler handler) {
        ModelApiRequest modelApiRequest = new ModelApiRequest();
        modelApiRequest.setModelId(Constants.ModelChatGLMTRUBO);
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
