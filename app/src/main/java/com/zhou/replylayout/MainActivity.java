package com.zhou.replylayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements ListAdapter.ReplyClickListener {
    private ListView listView = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private  ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<ReplyItem> replyItems=new ArrayList<ReplyItem>();
        ReplyItem replyItem=null;
        listView = (ListView) findViewById(R.id.listview);
        ArrayList<ReplyBean> list = new ArrayList<>();
        ReplyBean bean = new ReplyBean();
        bean.setContent("neirong");
        bean.setDateString("shijian");
        bean.setFirstName("343434324");
        bean.setSecondName("4324234324");
        ReplyBean bean2 = new ReplyBean();
        bean2.setContent("neirong2222");
        bean2.setDateString("shijian2222");
        bean2.setFirstName("343434324");
        bean2.setSecondName("4324234324");
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean);
        list.add(bean2);
        list.add(bean2);
       for (int i=0;i<20;i++){
           replyItem=new ReplyItem();
           replyItem.setMsg("这是第"+i+"条数据");
           replyItem.setList(list);
           replyItems.add(replyItem);
       }
        adapter=new ListAdapter(replyItems,this,this);
        listView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onReplyClick(int type, int msgPosition, int replyPosition) {
        switch (type){
            case  ReplyLayout.REPLY_FIRSTNAME_CLICK:
                Toast.makeText(this,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的第一个名字被点击", Toast.LENGTH_LONG).show();
                break;
            case  ReplyLayout.REPLY_SECONDNAME_CLICK:
                Toast.makeText(this,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的第二个名字被点击", Toast.LENGTH_LONG).show();
                break;
            case  ReplyLayout.REPLY_LAYOUT_CLICK:
                Toast.makeText(this,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的布局被点击", Toast.LENGTH_LONG).show();
                break;

        }
    }
}