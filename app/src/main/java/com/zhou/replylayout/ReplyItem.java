package com.zhou.replylayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ReplyItem {
    private String msg;
    private ArrayList<ReplyBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<ReplyBean> getList() {
        return list;
    }

    public void setList(ArrayList<ReplyBean> list) {
        this.list = list;
    }
}
