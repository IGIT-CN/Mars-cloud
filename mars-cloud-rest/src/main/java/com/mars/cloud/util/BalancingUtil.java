package com.mars.cloud.util;

import com.mars.cloud.core.model.UrlListModel;

import java.util.List;

/**
 * 负载均衡算法
 */
public class BalancingUtil {

    /**
     * 获取此次请求的URL,自动根据配置选择负载均衡策略
     *
     * @param urlListModel 参数
     * @return 结果
     */
    public static String getUrl(UrlListModel urlListModel) {
        return random(urlListModel);
    }

    /**
     * 随机算法
     *
     * @param urlListModel 参数
     * @return 结果
     */
    private static String random(UrlListModel urlListModel) {
        List<String> urls = urlListModel.getUrls();
        return urls.get(urlListModel.getRandomIndex());
    }
}
