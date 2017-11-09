package com.music.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/11/9.
 */

public class Feedback extends BmobObject {

    /**
     * 判断是产品建议还是程序错误
     */
    private boolean isSuggestion;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 联系方式
     */
    private String way;


    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean suggestion) {
        isSuggestion = suggestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}