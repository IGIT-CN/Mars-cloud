package com.mars.cloud.core.model;

import java.util.List;
import java.util.Random;

/**
 * urls
 */
public class UrlListModel {

    private Random random = new Random();

    /**
     * url列表
     */
    private List<String> urls;

    /**
     * 随机算法
     *
     * @return 下标
     */
    public synchronized int getRandomIndex() {
        int index = 0;
        if (urls.size() > 1) {
            index = random.nextInt(urls.size());
        }

        if (index < 0) {
            index = 0;
        }
        return index;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
