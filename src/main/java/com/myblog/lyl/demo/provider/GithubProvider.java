package com.myblog.lyl.demo.provider;

import com.alibaba.fastjson.JSON;
import com.myblog.lyl.demo.dto.AccessTokenDTO;
import com.myblog.lyl.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: demo
 * @Date: 2020/8/20 14:15
 * @Author: Yuling Li
 * @Description:
 */
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
        try (Response response = client.newCall(request).execute()) {
             String string = response.body().string();
             String access_token = string.split("&")[0].split("=")[1];
             return access_token;
        } catch (IOException e) {
        }
        return null;
    }
    public GithubUser getUser(String access_token) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + access_token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return  null;
    }
}
