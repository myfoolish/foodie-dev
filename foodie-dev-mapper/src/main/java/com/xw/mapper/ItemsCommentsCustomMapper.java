package com.xw.mapper;

import com.xw.pojo.vo.MyCommentVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/28
 */
public interface ItemsCommentsCustomMapper {

    public void saveComments(Map<String, Object> map);

    // todo 此处的 userId 无法传入xml
//    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);  // 对应的xml - user_id = #{paramsMap.userId}
    public List<MyCommentVO> queryMyComments(Map<String, Object> map);
}
