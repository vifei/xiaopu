package com.example.xiaopu.utils;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.zhipu.oapi.service.v3.ModelEventSourceListener;
import com.zhipu.oapi.service.v3.SseMeta;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyStandardEventSourceListener extends ModelEventSourceListener {
    private static final Logger logger = LoggerFactory.getLogger(MyStandardEventSourceListener.class);
    private String outputText = "";
    private boolean incremental;
    protected CountDownLatch countDownLatch = new CountDownLatch(1);
    private final Gson gson = new Gson();
    private SseMeta meta;



    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    public MyStandardEventSourceListener() {
    }


    public void onOpen(EventSource eventSource, Response response) {
        logger.info("server start sending events");
    }

    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if ("finish".equals(type)) {
            JSONObject jsonObject = JSON.parseObject(data);
            String meta = jsonObject.getString("meta");
            this.meta = this.gson.fromJson(meta, SseMeta.class);
            handler.sendMessage(handler.obtainMessage(2));

        }

        if (this.isIncremental()) {
            JSONObject jsonObject = JSON.parseObject(data);
            String msg = jsonObject.getString("data");
            handler.sendMessage(handler.obtainMessage(1, msg));
            logger.info("data:{}", msg);
            this.outputText = this.outputText + data;
        } else {
            logger.info("data:{}", data);
            this.outputText = data;
        }



    }

    public void onClosed(EventSource eventSource) {
        logger.info("server stream closed");

        try {
            eventSource.cancel();
        } finally {
            this.countDownLatch.countDown();
        }

    }

    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        try {
            logger.error("sse connection fail");
            eventSource.cancel();
        } finally {
            this.countDownLatch.countDown();
        }

    }

    public String getOutputText() {
        return this.outputText;
    }

    public SseMeta getMeta() {
        return this.meta;
    }

    public CountDownLatch getCountDownLatch() {
        return this.countDownLatch;
    }

    public boolean isIncremental() {
        return this.incremental;
    }

    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }
}