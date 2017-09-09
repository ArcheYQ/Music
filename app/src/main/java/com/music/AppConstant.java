package com.music;

/**
 * Created by 雅倩宝宝 on 2017/9/8.
 */

public class AppConstant {

        public class PlayerMsg{
            public static final int PLAY_MSG = 1;                      //开始播放
            public static final int PAUSE = 2;                         //暂停播放
            public static final int PREVIOUS_MUSIC = 3;                //上一首
            public static final int NEXT_MUSIC = 4;                    //下一首
            public static final int LOOP_MODE = 5;                     //循环播放
            public static final int RANDOM_MODE = 6;                   //随机播放
        }

        public class NotificationMsg{
            public static final String NOTIFICATION_PREVIOUS_MUSIC = "PREVIOUS";
            public static final String NOTIFICATION_NEXT_MUSIC = "NEXT";
            public static final String NOTIFICATION_PAUSE_MUSIC = "PLAY";
            public static final String NOTIFICATION_EXIT = "EXIT";
        }
}

