package com.zhou.replylayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ListAdapter extends BaseAdapter implements ReplyLayout.OnReplyLayoutClick {
    private ArrayList<ReplyItem> replylist;
    private Context mContext;
    private HashMap<Integer,Boolean> positionMap =new HashMap<Integer,Boolean>(); //用于存储对应位置是否展开
    private  ReplyClickListener mListener; //用于将点击事件回调给activity
    ListAdapter(ArrayList<ReplyItem> list, ReplyClickListener listener, Context context){
        mContext=context;
        replylist=list;
        mListener=listener;
    }
    @Override
    public int getCount() {
        return replylist.size();
    }

    @Override
    public Object getItem(int position) {
        return replylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
      ReplyItem item = replylist.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.layout_listitem, null);
            holder.tv = (TextView) convertView
                    .findViewById(R.id.list_tv);
            holder.replyLayout = (ReplyLayout) convertView
                    .findViewById(R.id.list_reply);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(item.getMsg()+"");
      holder.replyLayout.initReplyLayout(position,item.getList(),this
              ,positionMap.get(position)==null?false:positionMap.get(position),true);
        return convertView;
    }

    @Override
    public void onItemClick(int type, int msgPosition, int replyPosition) {
        /*将点击事件传给调用的Activity*/
        if(null!=mListener){
            mListener.onReplyClick(type,msgPosition,replyPosition);
        }
        /*测试使用*/
       /* switch (type){
            case  ReplyLayout.REPLY_FIRSTNAME_CLICK:
                Toast.makeText(mContext,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的第一个名字被点击",Toast.LENGTH_LONG).show();
                break;
            case  ReplyLayout.REPLY_SECONDNAME_CLICK:
                Toast.makeText(mContext,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的第二个名字被点击",Toast.LENGTH_LONG).show();
                break;
            case  ReplyLayout.REPLY_LAYOUT_CLICK:
                Toast.makeText(mContext,"第"+msgPosition+"条留言的第"+replyPosition+"条回复的布局被点击",Toast.LENGTH_LONG).show();
                break;

        }*/

    }

    @Override
    public void onOptButtonClick(int type, int msgPosition) {
        if(ReplyLayout.REPLY_OPT_CLOSED==type){
            positionMap.put(msgPosition,false);
        }else if(ReplyLayout.REPLY_OPT_OPENED==type){
            positionMap.put(msgPosition,true);
        }

    }

    class  ViewHolder {
       TextView tv;
        ReplyLayout replyLayout;
    }

    public  interface  ReplyClickListener{
        void onReplyClick(int type, int msgPosition, int replyPosition);
    }
}
