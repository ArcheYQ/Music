package com.music.lrc;

/**
 * Created by 雅倩宝宝 on 2017/10/12.
 */

public interface ILrcViewListener {
    /**
     * 拖动歌词的时候调用
     *
     * @param newPosition
     * @param row
     */
    void onLrcSeeked(int newPosition, LrcRow row);
}
