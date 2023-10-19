package com.xw.service;

import com.xw.pojo.Category;
import com.xw.pojo.vo.CategoryVO;
import com.xw.pojo.vo.ItemsVO;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
public interface CategoryService {

    /**
     * 查询一级分类
     * @return
     */
    public List<Category> queryRootCategory();

    /**
     * 根据一级分类 id 查询子分类信息
     * @param rootCategoryId
     * @return
     */
    public List<CategoryVO> querySubCategory(Integer rootCategoryId);

    /**
     * 查询首页每个一级分类下的6条最新商品数据
     * @param rootCategoryId
     * @return
     */
    public List<ItemsVO> querySixNewItems(Integer rootCategoryId);
}
