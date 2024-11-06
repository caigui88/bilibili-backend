package com.bilibili.chat.ppt;

import com.alibaba.fastjson.JSON;
import com.bilibili.chat.domain.vo.ProgressVO;
import com.bilibili.chat.domain.vo.CreateVO;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 输入个人appId
        String appId = "xxx";
        String secret = "xxx";
        long timestamp = System.currentTimeMillis()/1000;
        String ts = String.valueOf(timestamp);
        // 获得鉴权信息
        ApiAuthAlgorithm auth = new ApiAuthAlgorithm();
        String signature = auth.getSignature(appId, secret, timestamp);
        System.out.println(signature);

        // 建立链接
        ApiClient client = new ApiClient("https://zwapi.xfyun.cn");

        // 查询PPT模板信息
        String templateResult = client.getTemplateList(appId, ts, signature);
        System.out.println(templateResult);

        // 发送生成PPT请求
        String query = "这是一个测试";
        String resp = client.createPPT(appId, ts, signature,query);
        System.out.println(resp);
        CreateVO response = JSON.parseObject(resp, CreateVO.class);

        // 利用sid查询PPT生成进度
        int progress = 0;
        ProgressVO progressVO;
        while (progress < 100) {
            String progressResult = client.checkProgress(appId, ts, signature, response.getData().getSid());
            progressVO = JSON.parseObject(progressResult, ProgressVO.class);
            progress = progressVO.getData().getProcess();
            System.out.println(progressResult);

            if (progress < 100) {
                Thread.sleep(5000); // 暂停2秒
            }
        }

        // 大纲生成
        String outlineQuery = "这是一个大纲生成的测试";
        String outlineResp = client.createOutline(appId, ts, signature,query);
        System.out.println(outlineResp);
        CreateVO outlineResponse = JSON.parseObject(outlineResp, CreateVO.class);
        System.out.println("生成的大纲如下：");
        System.out.println(outlineResponse.getData().getOutline());

//        // 基于sid和大纲生成ppt
//        String sidResp = client.createPptBySid(appId, ts, signature, outlineResponse.getData().getSid());
//        System.out.println(sidResp);
//        CreateVO sidResponse = JSON.parseObject(sidResp, CreateVO.class);
//        sidResp = client.createPptBySid(appId, ts, signature, outlineResponse.getData().getSid());
//        System.out.println(sidResp);
//        sidResponse = JSON.parseObject(sidResp, CreateVO.class);
//        // 利用sid查询PPT生成进度
//        progress = 0;
//        while (progress < 100) {
//            String progressResult = client.checkProgress(appId, ts, signature, sidResponse.getData().getSid());
//            progressVO = JSON.parseObject(progressResult, ProgressVO.class);
//            progress = progressVO.getData().getProcess();
//            System.out.println(progressResult);
//
//            if (progress < 100) {
//                Thread.sleep(5000); // 暂停2秒
//            }
//        }

        // 基于大纲生成ppt
        String pptResp = client.createPptByOutline(appId, ts, signature, outlineQuery, outlineResponse.getData().getOutline());
        System.out.println(pptResp);
        CreateVO pptResponse = JSON.parseObject(pptResp, CreateVO.class);
        // 利用sid查询PPT生成进度
        progress = 0;
        while (progress < 100) {
            String progressResult = client.checkProgress(appId, ts, signature, pptResponse.getData().getSid());
            progressVO = JSON.parseObject(progressResult, ProgressVO.class);
            progress = progressVO.getData().getProcess();
            System.out.println(progressResult);

            if (progress < 100) {
                Thread.sleep(5000); // 暂停2秒
            }
        }
    }
}