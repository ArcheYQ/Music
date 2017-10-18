package com.music.lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/10/12.
 */

public class LrcView extends android.view.View implements ILrcView {
    /**
     * 正常歌词模式
     */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /**
     * 拖动歌词模式
     */
    public final static int DISPLAY_MODE_SEEK = 1;
    /**
     * 缩放歌词模式
     */
    public final static int DISPLAY_MODE_SCALE = 2;
    /**
     * 歌词的当前展示模式
     */
    private int mDisplayMode = DISPLAY_MODE_NORMAL;
    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的字体颜色
     **/
    private int mSeekLineColor = Color.CYAN;

    /**
     * 两行歌词之间的间距
     **/
    private int mPaddingY = 10;
    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的起始位置
     **/
    private int mSeekLinePaddingX = 0;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体颜色
     **/
    private int mSeekLineTextColor = Color.CYAN;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小默认值
     **/
    private int mSeekLineTextSize = 15;
    /**
     * 歌词集合，包含所有行的歌词
     */
    private static List<LrcRow> mLrcRows;
    /**
     * 当前高亮歌词的字体颜色为黄色
     */
    private int mHignlightRowColor = Color.YELLOW;
    /**
     * 不高亮歌词的字体颜色为白色
     */
    private int mNormalRowColor = Color.WHITE;
    /**
     * 当前高亮歌词的行数
     */
    private int mHignlightRow = 0;
    /**
     * 歌词字体大小默认值
     **/
    private int mLrcFontSize = 23;    // font size of lrc
    /**
     * 当没有歌词的时候展示的内容
     **/
    private String mLoadingLrcTip = "Downloading lrc...";
    /**
     * 拖动歌词的监听类，回调LrcViewListener类的onLrcSeeked方法
     **/
    private ILrcViewListener mLrcViewListener;
    private Paint mPaint;
    public LrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();//height of this view
        final int width = getWidth();//width of this view
        //当没有歌词的时候
        if(mLrcRows == null || mLrcRows.size() == 0){
            if(mLoadingLrcTip != null){
                //draw tip when no Lrc
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(mLoadingLrcTip,width/2,height/2-mLrcFontSize,mPaint);
            }
            return;
        }
        int rowY=0;// vertical point of each row.
        final int rowX = width/2;
        int rowNum = 0;
        //1.高亮地画出正在要高亮的那句歌词
        String highlightText = mLrcRows.get(mHignlightRow).content;
        int highlightRowY = height/2-mLrcFontSize;
        mPaint.setColor(mHignlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(highlightText,rowX,highlightRowY,mPaint);
        //上下拖动歌词的时候 画出拖动要高亮的那句歌词的时间 和 高亮的那句下面的一条直线
        if (mDisplayMode == DISPLAY_MODE_SEEK){
            //画出高亮的那句歌词的下面的一条直线
            mPaint.setColor(mSeekLineColor);
            //该直线的X左边从O到屏幕宽度 Y坐标为高亮歌词和下一行歌词中间
            canvas.drawLine(mSeekLinePaddingX,highlightRowY +mPaddingY,width - mSeekLinePaddingX,highlightRowY +mPaddingY,mPaint);
            //画出高亮的那句歌词的时间
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(mLrcRows.get(mHignlightRow).strTime,0,highlightRowY,mPaint);
        }
        //2.画出正在播放的那句歌词的上面可以展示出来的歌词
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        rowNum = mHignlightRow-1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        //画出正在播放的那句歌词的上面所有的歌词
        while(rowY > -mLrcFontSize && rowNum >=0){
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text,rowX,rowY,mPaint);
            rowY -= (mPaddingY+mLrcFontSize);
            rowNum--;
        }
        //3.画出正在播放的那句歌词的下面的可以展示出来的歌词
        rowNum = mHignlightRow + 1;
        rowY = highlightRowY + mPaddingY +mLrcFontSize;
        //画出正在播放的那句歌词的所有下面的可以展示出来的歌词
        while(rowY <height && rowNum < mLrcRows.size()){
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text,rowX,rowY,mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum ++;
        }
        super.onDraw(canvas);
    }

    @Override
    public void setLrc(List<LrcRow> lrcRows) {
        mLrcRows = lrcRows;
        invalidate();
    }

    /**
     * 播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     * @param time
     */
    @Override
    public void seekLrcToTime(long time) {
        Log.i("TAG", "run: 12"+time);
    if (mLrcRows == null || mLrcRows.size() == 0){
        return;
    }
    if (mDisplayMode != DISPLAY_MODE_NORMAL){
        return;
    }
    for (int i = 0;i<mLrcRows.size(); i++){
        LrcRow current = mLrcRows.get(i);

        LrcRow next = i + 1 ==mLrcRows.size() ? null : mLrcRows.get(i + 1);
        /**
         *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
         *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
         */
        Log.i("TAG", "run1: "+current.time+i);
        Log.i("TAG", "run1size: "+mLrcRows.size());
        if ((time >= current.time && next != null && time < next.time)
                || (time > current.time && next == null)){
            seekLrc(i,false);
            return;
        }
    }
    }

    /**
     * 设置要高亮的歌词为第几行歌词
     * @param position 要高亮的歌词行数
     * @param cb   是否是手指拖动后要高亮的歌词
     */
    public void seekLrc(int position, boolean cb){
        if(mLrcRows == null || position <0 ||position > mLrcRows.size()){
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHignlightRow = position;
        invalidate();//刷新
        //如果是手指拖动歌词后
        if (mLrcViewListener != null && cb){
            //回调onLrcSeeked方法，将音乐播放器的位置移动到高亮歌词的位置
            mLrcViewListener.onLrcSeeked(position,lrcRow);
        }
    }
    @Override
    public void setListener(ILrcViewListener iLrcViewListener) {
        mLrcViewListener = iLrcViewListener;
    }
    private float mLastMotionY;
    /**
     * 第一个手指的坐标
     **/
    private PointF mPointerOneLastMotion = new PointF();
    /**
     * 第二个手指的坐标
     **/
    private PointF mPointerTwoLastMotion = new PointF();
    /**
     * 是否是第一次移动，当一个手指按下后开始移动的时候，设置为true,
     * 当第二个手指按下的时候，即两个手指同时移动的时候，设置为false
     */
    private boolean mIsFirstMove = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mLrcRows == null || mLrcRows.size() == 0){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            //手指按下
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mIsFirstMove = true;
                invalidate();
                break;
            //手指移动
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    doScale(event);
                    return true;
                }
                if (mDisplayMode == DISPLAY_MODE_SCALE){
                    return true;
                }
                doSeek(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                //手指抬起
            case MotionEvent.ACTION_UP:
                if (mDisplayMode == DISPLAY_MODE_SEEK){
                    //高亮手指抬起时的歌词   (bofan)
                    seekLrc(mHignlightRow,true);

                }
                mDisplayMode  = DISPLAY_MODE_NORMAL;
                invalidate();
                break;
        }
        return true;
    }
    /**
     * 最小移动的距离，当拖动歌词时如果小于该距离不做处理
     */
    private int mMinSeekFiredOffset = 10;
    /**
     * 处理单指在屏幕移动时，歌词上下滚动
     * @param event
     */
    private void doSeek (MotionEvent event){
        float y = event.getY();//手指当前位置的Y坐标
        float offsetY = y - mLastMotionY;//第一次按下的Y坐标和目前移动位置的y坐标之差
        //如果移动距离小于10，不做任何处理
        if (Math.abs(offsetY) < mMinSeekFiredOffset ){
            return ;
        }
        //将模式设置为拖动歌词模式
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY/mLrcFontSize);
        if (offsetY < 0){
            //手指向上移动，歌词向下滚动
            mHignlightRow += rowOffset;//设置要高亮的歌词为 当前高亮歌词 向下滚动rowOffset行后的歌词
        } else if (offsetY > 0) {
            mHignlightRow -= rowOffset;
        }
        //设置要高亮的歌词为0和mHignlightRow的较大值。即如果 mHignlightRow《0，mHignlightRow = 0
        mHignlightRow = Math.max(0,mHignlightRow);
        //设置要高亮的歌词为0和mHignlightRow的较小值。即如果 mHignlight > RowmLrcRows.size()-1，mHignlightRow=mLrcRows.size()-1
        mHignlightRow = Math.min(mHignlightRow,mLrcRows.size() - 1);
        //如果歌词要滚动的行数大于0，重画LrcView
        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    /**
     * 处理双指在屏幕移动时的歌词大小缩放
     * @param event
     */
    private void doScale(MotionEvent event){
        //如果歌词的模式为：拖动歌词模式
        if (mDisplayMode == DISPLAY_MODE_SEEK){
            //如果是单指按下，在进行歌词上下滚动，然后按下另一个手指，则吧歌词模式从 拖动歌词模式 变为缩放歌词模式
            mDisplayMode = DISPLAY_MODE_SCALE;
            return;
        }
        if (mIsFirstMove){
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            mIsFirstMove = false;
            //两个手指的X坐标和Y坐标
            setTwoPointerLocation(event);
        }
        //获取歌词大小要缩放的比例
        int scaleSize = getScale(event);
        //如果缩放大小不等于0，进行缩放，重回LRCVIEW
        if (scaleSize != 0){
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);
    }
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最小值
     **/
    private int mMinSeekLineTextSize = 13;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最大值
     **/
    private int mMaxSeekLineTextSize = 18;
    /**
     * 歌词字体大小最小值
     **/
    private int mMinLrcFontSize = 15;
    /**
     * 歌词字体大小最大值
     **/
    private int mMaxLrcFontSize = 35;
    /**
     * 设置当前两个手指的X,Y坐标
     * @param event
     */
    private void setTwoPointerLocation(MotionEvent event){
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }
    /**
     * 设置缩放后的字体大小
     */

    private void setNewFontSize (int scaleSize) {
        //设置歌词缩放后的最新字体大小
        mLrcFontSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize,mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize,mMaxLrcFontSize);
        //设置显示高亮的那句歌词的时间最新字体大小
        mSeekLineTextSize += scaleSize;
        mSeekLineTextSize = Math.max(mSeekLineTextSize,mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize,mMaxSeekLineTextSize);
    }
    /**
     * 获取歌词大小要缩放的比例
     */
    private int getScale (MotionEvent event){
        float x0 = event.getX(0);
        float y0= event.getY(0);
        float x1 = event.getX(1);
        float y1= event.getY(1);
        float maxOffset = 0; // max offset between x or y axis,used to decide scale size
        boolean zoomin = false;
        //第一次双指之间的X坐标的差距
        float oldXOffset = Math.abs(mPointerOneLastMotion.x - mPointerTwoLastMotion.x);
        //第二次
        float newXOffset = Math.abs(x1 - x0);
        float oldYOffset = Math.abs(mPointerOneLastMotion.y - mPointerTwoLastMotion.y);
        float newYOffset = Math.abs(y1 - y0);
        //双指移动后。判断双指之间移动的最大差距
        maxOffset = Math.max(Math.abs(newXOffset- oldXOffset),Math.abs(newYOffset -oldYOffset));
        //如果x坐标移动的多一些
        if (maxOffset == Math.abs(newXOffset - oldXOffset)){
            //如果第二次双指之间的x坐标的差距大于第一次双指之间的x坐标的差距则是放大，反之则缩小
            zoomin = newXOffset >oldXOffset ?true : false;
        }   //如果y坐标移动的多一些
        else {
            //如果第二次双指之间的y坐标的差距大于第一次双指之间的y坐标的差距则是放大，反之则缩小
            zoomin = newYOffset > oldYOffset ? true : false;
        }
        if (zoomin){
            return (int) (maxOffset/10);
        }else {
            return (int) - (maxOffset/10);
        }
    }
}
