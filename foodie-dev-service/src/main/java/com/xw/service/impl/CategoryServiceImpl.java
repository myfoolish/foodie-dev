package com.xw.service.impl;

import com.xw.enums.CategoryLevel;
import com.xw.mapper.CategoryCustomMapper;
import com.xw.mapper.CategoryMapper;
import com.xw.pojo.Category;
import com.xw.pojo.vo.CategoryVO;
import com.xw.pojo.vo.ItemsVO;
import com.xw.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
@Service
public class CategoryServiceImpl  implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryCustomMapper categoryCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryRootCategory() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", CategoryLevel.ROOT.type);
        List<Category> result = categoryMapper.selectByExample(example);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> querySubCategory(Integer rootCategoryId) {
        List<CategoryVO> result = categoryCustomMapper.querySubCategory(rootCategoryId);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsVO> querySixNewItems(Integer rootCategoryId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rootCategoryId", rootCategoryId);
        List<ItemsVO> result = categoryCustomMapper.querySixNewItems(map);
        return result;
    }
}
