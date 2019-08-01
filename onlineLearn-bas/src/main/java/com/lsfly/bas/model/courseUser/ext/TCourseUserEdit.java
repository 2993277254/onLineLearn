package com.lsfly.bas.model.courseUser.ext;

import com.lsfly.bas.model.courseUser.TCourseUser;

/**
 * 创建TCourseUser的编辑实体，可增加一些编辑页面需要的实体
 * @date 2018/8/24.
 */
public class TCourseUserEdit extends TCourseUser {
    private String type;
    private String teacherId;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
