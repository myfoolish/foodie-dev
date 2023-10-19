package com.xw.service.impl;

import com.github.pagehelper.PageInfo;
import com.xw.utils.PagedGridResult;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/28
 */
public class BaseService {

    /**
     * 分页通用方法
     * @param list
     * @param page
     * @return
     */
    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList=new PageInfo<>(list);
        PagedGridResult grid=new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
