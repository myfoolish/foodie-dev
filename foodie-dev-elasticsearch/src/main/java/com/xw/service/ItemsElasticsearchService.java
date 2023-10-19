package com.xw.service;

import com.xw.utils.PagedGridResult;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/8
 */
public interface ItemsElasticsearchService {

    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
}
