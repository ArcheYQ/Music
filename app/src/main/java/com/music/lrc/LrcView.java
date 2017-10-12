package com.music.lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
    private List<LrcRow> mLrcRows;
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

    }

    /**
     * 播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     * @param time
     */
    @Override
    public void seekLrcToTime(long time) {
    if (mLrcRows == null || mLrcRows.size() == 0){
        return;
    }
    if (mDisplayMode != DISPLAY_MODE_NORMAL){
        return;
    }
    for (int i = 0;i<mLrcRows.size();i++){
        LrcRow current = mLrcRows.get(i);
        LrcRow next = i + 1 ==mLrcRows.size() ? null : mLrcRows.get(i + 1);
        /**
         *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
         *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
         */
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

    }
}
