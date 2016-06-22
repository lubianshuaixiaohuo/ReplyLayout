package com.zhou.replylayout;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ReplyLayout extends LinearLayout {
    private boolean isMoving = false;  //是否正在移动
    private boolean isOpened = false; //是否展开
    private int showNum = 5;  //默认展示的留言数量
    private LinearLayout firstReplyLayout = null;  //默认展示的回复布局
    private LinearLayout secondReplyLayout = null;  //剩下的回复外层布局
    private LinearLayout secondInnerLayout = null;  //剩下的回复内层布局
    private LinearLayout operationLayout = null;  //按钮布局
    private OnReplyLayoutClick listener = null;
    private LinearLayout.LayoutParams lineParams = null;
    private LayoutInflater inflater = null;
    private Context mContext;
    private ImageView optImg = null;
    private TextView optTv = null;
    private int firstLayoutHeight = 0;
    private int secondLayoutHeight = 0;
    public static final int VIEW_SHOW_LINE = 1;
    public static final int VIEW_UNSHOW_LINE = 2;
    public static final int REPLY_LAYOUT_CLICK = 0;
    public static final int REPLY_FIRSTNAME_CLICK = 1;
    public static final int REPLY_SECONDNAME_CLICK = 2;
    public static final int REPLY_OPT_CLOSED = 1;
    public static final int REPLY_OPT_OPENED = 2;
    private ValueAnimator mValueAnimator;//展开收起动画

    public ReplyLayout(Context context) {
        super(context);
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public ReplyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        init(context);
    }


    public ReplyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        init(context);
    }

    private void init(Context context) {
        if (null == context) {
            return;
        }
        this.setOrientation(LinearLayout.VERTICAL);
        firstReplyLayout = new LinearLayout(context);
        secondReplyLayout = new LinearLayout(context);
        operationLayout = new LinearLayout(context);
        secondInnerLayout = new LinearLayout(context);
        lineParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                .WRAP_CONTENT);
        firstReplyLayout.setLayoutParams(lineParams);
        secondReplyLayout.setLayoutParams(lineParams);
        operationLayout.setLayoutParams(lineParams);
        secondInnerLayout.setLayoutParams(lineParams);
        firstReplyLayout.setOrientation(LinearLayout.VERTICAL);
        secondReplyLayout.setOrientation(LinearLayout.VERTICAL);
        operationLayout.setOrientation(LinearLayout.VERTICAL);
        secondInnerLayout.setOrientation(LinearLayout.VERTICAL);
        View tmpView = inflater.inflate(R.layout.layout_replyopt, null);
        optImg = (ImageView) tmpView.findViewById(R.id.reply_optImg);
        optTv = (TextView) tmpView.findViewById(R.id.reply_optTv);
        operationLayout.addView(tmpView);
        this.addView(firstReplyLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        this.addView(secondReplyLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        this.addView(operationLayout, lineParams);
    }

    /***
     *
     * @param msgPosition 留言的位置，主要是回调时使用，用于区分是哪条留言
     * @param list   留言的回复数据
     * @param repayLayoutClick   回调接口，用于获取回复布局的点击事件
     * @param isOpen  是否展开
     * @param isAnimation  是否使用动画
     */
    public void initReplyLayout(final int msgPosition, ArrayList<ReplyBean> list, OnReplyLayoutClick
            repayLayoutClick, boolean isOpen, final boolean isAnimation) {
        if (null == list || 0 == list.size()) {
            this.setVisibility(View.GONE);
            return;
        }
        if (null != firstReplyLayout) {
            firstReplyLayout.removeAllViews();
        }
        if (null != secondReplyLayout) {
            secondReplyLayout.removeAllViews();
            secondReplyLayout.getLayoutParams().height=0;
        }
        if (null != secondInnerLayout) {
            secondInnerLayout.removeAllViews();
        }
        operationLayout.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
        if (null != repayLayoutClick) {
            this.listener = repayLayoutClick;
        }
        boolean enoughFlag = list.size() > showNum ? true : false;//是否超过规定的个数
        if (enoughFlag) {
            for (int i = 0; i < showNum; i++) {
                View tmpView = createReplyView(msgPosition, i, list.get(i), VIEW_SHOW_LINE);
                if (null != tmpView) {
                    firstReplyLayout.addView(tmpView, lineParams);
                }
            }

            for (int i = showNum; i < list.size(); i++) {
                View tmpView = createReplyView(msgPosition, i, list.get(i), VIEW_SHOW_LINE);
                if (null != tmpView) {
                    if (null != secondInnerLayout) {
                        secondInnerLayout.addView(tmpView, lineParams);
                    }
                }
            }
            secondInnerLayout.measure(0, 0);
            secondLayoutHeight = secondInnerLayout.getMeasuredHeight();
            if (isOpen) {
                if (null != secondInnerLayout && null != secondReplyLayout) {
                    isOpened = true;
                    secondReplyLayout.addView(secondInnerLayout, lineParams);
                    secondReplyLayout.getLayoutParams().height = secondLayoutHeight;
                    //如果展开则第二个回复布局可见
                }
            }else {
                isOpened=false;
            }
            operationLayout.setVisibility(View.VISIBLE);
            operationLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptClicked(msgPosition, isAnimation);
                }
            });
            buttonEffectChange(isOpen);
        } else {
            for (int i = 0; i < list.size(); i++) {
                /*如果不够展示，则最后一条线不显示*/
                int tmpShowState = VIEW_SHOW_LINE;
                if (i == list.size() - 1) {
                    tmpShowState = VIEW_UNSHOW_LINE;
                }
                View tmpView = createReplyView(msgPosition, i, list.get(i), tmpShowState);
                if (null != tmpView) {
                    firstReplyLayout.addView(tmpView, lineParams);
                }
            }
            operationLayout.setVisibility(View.GONE);
        }
        firstReplyLayout.measure(0,0);
        firstLayoutHeight=firstReplyLayout.getMeasuredHeight();
        firstReplyLayout.getLayoutParams().height=firstLayoutHeight;
    }

    /***
     *  回复的收起或展开按钮点击
     * @param msgPosition  哪个留言的回复布局的按钮被点击
     * @param isAnimation  是否使用动画
     */
    private void onOptClicked(int msgPosition, boolean isAnimation) {
        if (isMoving) {
            return;
        }
        if (null == secondReplyLayout || null == secondInnerLayout) {
            return;
        }
        if (isOpened) {
            if (isAnimation) {
                if (secondReplyLayout != null) {
                    if (mValueAnimator != null) {
                        mValueAnimator.end();
                    }
                    performAnimate(secondReplyLayout, secondLayoutHeight, 0, 500);
                }
            } else {
                secondReplyLayout.getLayoutParams().height=0;
                secondReplyLayout.removeAllViews();
                buttonEffectChange(!isOpened);
            }
            isOpened = false;
            if (null != listener) {
                listener.onOptButtonClick(REPLY_OPT_CLOSED, msgPosition);
            }
        } else {
            if (isAnimation) {
                secondReplyLayout.addView(secondInnerLayout, lineParams);
                performAnimate(secondReplyLayout, 0, secondLayoutHeight, 500);
            } else {
                secondReplyLayout.addView(secondInnerLayout, lineParams);
                secondReplyLayout.getLayoutParams().height=secondLayoutHeight;
                buttonEffectChange(!isOpened);
            }
            isOpened = true;
            if (null != listener) {
                listener.onOptButtonClick(REPLY_OPT_OPENED, msgPosition);
            }
        }
    }


    /**
     * 动画改变一个View的高度
     *
     * @param target    目标View
     * @param start     开始高度
     * @param end       结束高度
     * @param nDuration 持续时间
     */
    private void performAnimate(final View target,
                                final int start,
                                final int end,
                                final int nDuration) {

        mValueAnimator = ValueAnimator.ofInt(1, 100);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                // Log.d(TAG, current value:  + currentValue);

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();

            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isMoving = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mValueAnimator = null;
                buttonEffectChange(isOpened);
                if (!isOpened) {
                    target.getLayoutParams().height = 0;
                    if (target instanceof ViewGroup) {
                        ((ViewGroup) target).removeAllViews();
                    }
                }
                isMoving = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isMoving = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(nDuration).start();
    }

    /***
     *  生成单条回复布局
     * @param msgPosition  回复的留言的位置，主要为了点击事件回调时使用
     * @param replyPosition  回复的位置，该条留言的第几条回复
     * @param bean   回复的相关内容
     * @param type  类型，用了区分最后一条数据是否需要横线
     * @return  回复布局
     */
    private View createReplyView(final int msgPosition, final int replyPosition, ReplyBean bean, int type) {
        if (null == bean) {
            return null;
        }
        if (null == inflater) {
            return null;
        }
        View view = inflater.inflate(R.layout.layout_replyitem, null);
        TextView firstName = (TextView) view.findViewById(R.id.reply_firstName);
        TextView replyString = (TextView) view.findViewById(R.id.reply_replyString);
        TextView secondName = (TextView) view.findViewById(R.id.reply_secondName);
        TextView replyTime = (TextView) view.findViewById(R.id.reply_time);
        TextView replyContent = (TextView) view.findViewById(R.id.reply_content);
        TextView replyLine = (TextView) view.findViewById(R.id.reply_line);
        if (VIEW_SHOW_LINE == type) {
            replyLine.setVisibility(View.VISIBLE);
        } else {
            replyLine.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(bean.getFirstName())) {
            firstName.setText(bean.getFirstName());
        }
        firstName.setText(bean.getFirstName());
        if (TextUtils.isEmpty(bean.getSecondName())) {
            secondName.setVisibility(View.GONE);
            replyString.setVisibility(View.GONE);
        } else {
            secondName.setVisibility(View.VISIBLE);
            replyString.setVisibility(View.VISIBLE);
            secondName.setText(bean.getSecondName());
        }
        if (!TextUtils.isEmpty(bean.getDateString())) {
            replyTime.setText(bean.getDateString());
        }
        if (!TextUtils.isEmpty(bean.getContent())) {
            replyContent.setText(bean.getContent());
        }
        firstName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(REPLY_FIRSTNAME_CLICK, msgPosition, replyPosition);
                }
            }
        });
        secondName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(REPLY_SECONDNAME_CLICK, msgPosition, replyPosition);
                }
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(REPLY_LAYOUT_CLICK, msgPosition, replyPosition);
                }
            }
        });
        return view;
    }

    private void buttonEffectChange(boolean openFlag) {
        if (openFlag) {
            optTv.setText("收起");
        } else {
            optTv.setText("展开");
        }
    }


    /***
     * 点击事件回调接口
     */
    public interface OnReplyLayoutClick {
        void onItemClick(int type, int msgPosition, int replyPosition); // 留言被点击 type 0为整个条目点击 1为第一个人名点击 ，2为第二个人名点击

        void onOptButtonClick(int type, int msgPosition); //展开关闭按钮 ，type 1为展开 2为关闭

    }


    public static class WrapperView {
        private View mTarget;

        public WrapperView(View target) {
            mTarget = target;
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setWidth(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }


    }


}
