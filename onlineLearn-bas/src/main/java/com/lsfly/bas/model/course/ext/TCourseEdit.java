package com.lsfly.bas.model.course.ext;

import com.lsfly.bas.model.course.TCourse;
import com.lsfly.bas.model.course.TCourseWithBLOBs;

/**
 * 创建TCourse的编辑实体，可增加一些编辑页面需要的实体
 * @date 2018/8/24.
 */
public class TCourseEdit extends TCourseWithBLOBs {
    //查询用户与课程的关系
    private String type;
    private String isHas;//有值表示有关系

    private String isInsertVideo;//判断是否是提交课程视频信息

    public String getIsInsertVideo() {
        return isInsertVideo;
    }

    public void setIsInsertVideo(String isInsertVideo) {
        this.isInsertVideo = isInsertVideo;
    }

    private String isStudy="0";//查看是否观看视频的关系，有值表示有关系

    public String getIsStudy() {
        return isStudy;
    }

    public void setIsStudy(String isStudy) {
        this.isStudy = isStudy;
    }

    public String getIsHas() {
        return isHas;
    }

    public void setIsHas(String isHas) {
        this.isHas = isHas;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
