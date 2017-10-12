package com.music.lrc;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/10/12.
 * 展示歌词的接口
 */

public interface ILrcView {
    /**
     * 设置要展示的歌词行集合
     * @param lrcRows
     */
    void setLrc(List<LrcRow> lrcRows);

    /**
     * 音乐播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
      * @param time
     */
    void seekLrcToTime(long time);

    /**
     * 设置歌词拖动时候的监听类
     * @param iLrcViewListener
     */
    void setListener(ILrcViewListener iLrcViewListener);
}
