package com.mars.cloud.util;

import com.mars.cloud.core.util.CloudConfigUtil;
import com.mars.cloud.model.UrlListModel;

import java.util.List;

/**
 * 负载均衡算法
 */
public class LoadBalancingUtil {

    private static String strategy;

    /**
     * 获取此次请求的URL,自动根据配置选择负载均衡策略
     * @param urlListModel
     * @return
     */
    public static String getUrl(UrlListModel urlListModel){
        init();
        switch (strategy){
            case "1":
                return poll(urlListModel);
            case "2":
                return random(urlListModel);
        }
        return null;
    }

    /**
     * 随机算法
     * @param urlListModel
     * @return
     */
    private static String random(UrlListModel urlListModel){
        List<String> urls = urlListModel.getUrls();
        return urls.get(urlListModel.getRandomIndex());
    }

    /**
     * 轮询算法
     * @param urlListModel
     * @return
     */
    private static String poll(UrlListModel urlListModel){
        List<String> urls = urlListModel.getUrls();
        return urls.get(urlListModel.getIndex());
    }

    /**
     * 加载策略
     */
    private static void init(){
        try {
            if(strategy == null){
                strategy = CloudConfigUtil.getCloudConfig("strategy").toString();
            }
        } catch (Exception e){
            strategy = "1";
        }
    }
}
