package com.mars.cloud.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mars.cloud.rest.LoadMarsCloudUrls;
import com.mars.cloud.util.ServerListUtil;
import okhttp3.*;

import java.util.Map;
import java.util.Random;

public class RestRequest {

    public static Object request(String serverName, String methodName, Map<String, Object> params) throws Exception{
        String url = "";
        try {
            LoadMarsCloudUrls.loadServices();


            JSONArray urlList = ServerListUtil.get(serverName+"/"+methodName);

            url = getUrl(urlList);

            OkHttpClient httpClient = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

            String post = JSON.toJSONString(params);

            RequestBody requestBody = RequestBody.create(mediaType, post);

            Request request = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();

            Response response = httpClient.newCall(request).execute();

            if(response.isSuccessful()){
                return response.body();
            }
        } catch (Exception e){
            throw new Exception("请求"+url+"失败",e);
        }

        return null;
    }

    private static String getUrl(JSONArray urlList){
        Random random = new Random();
        int i = random.nextInt(urlList.size()-1);
        if(i<0){
            i = 0;
        }
        return urlList.getString(i);
    }
}
