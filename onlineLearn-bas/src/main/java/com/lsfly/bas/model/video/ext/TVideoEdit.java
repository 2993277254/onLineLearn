package com.lsfly.bas.model.video.ext;

import com.lsfly.bas.model.video.TVideo;

import java.util.List;
import java.util.Map;

/**
 * 创建TVideo的编辑实体，可增加一些编辑页面需要的实体
 * @date 2018/8/24.
 */
public class TVideoEdit extends TVideo {
    private List<Map<String,String>> videoList;

    public List<Map<String, String>> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Map<String, String>> videoList) {
        this.videoList = videoList;
    }
}
