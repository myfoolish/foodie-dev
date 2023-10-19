package com.xw.service.sync;

import com.xw.pojo.vo.SearchItemsVO;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/4
 */
public interface SyncElasticsearchService {

    public List<SearchItemsVO> searchItems();
}
