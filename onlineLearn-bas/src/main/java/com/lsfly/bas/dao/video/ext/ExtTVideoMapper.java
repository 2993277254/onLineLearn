package com.lsfly.bas.dao.video.ext;

import com.lsfly.bas.model.video.TVideo;
import com.lsfly.bas.model.video.ext.TVideoList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 如果需要写sql语句，不要再TVideoMapper写，自己建一个ext文件和ext的xml文件，
 * 比如ExtTVideoMapper.java和ExtTVideoMapper.xml
 */
public interface ExtTVideoMapper {

    List<TVideoList> list(TVideoList tVideoList, RowBounds rowBounds);
    int insertList(@Param("list") List<TVideo> list);
}