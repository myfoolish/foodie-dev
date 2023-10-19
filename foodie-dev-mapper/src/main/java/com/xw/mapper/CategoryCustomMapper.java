package com.xw.mapper;

import com.xw.pojo.vo.CategoryVO;
import com.xw.pojo.vo.ItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
public interface CategoryCustomMapper{

    public List<CategoryVO> querySubCategory(Integer rootCategoryId);

    public List<ItemsVO> querySixNewItems(@Param("paramsMap") Map<String, Object> map);
}