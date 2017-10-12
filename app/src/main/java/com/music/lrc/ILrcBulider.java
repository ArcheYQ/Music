package com.music.lrc;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/10/11.
 */

public interface ILrcBulider {
    List<LrcRow> getLrcRows(String rawLrc);
}
