package com.xw.service.impl.sync;

import com.xw.mapper.ItemsCustomMapper;
import com.xw.pojo.vo.SearchItemsVO;
import com.xw.service.impl.BaseService;
import com.xw.service.sync.SyncElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/4
 */
@Service
public class SyncElasticsearchServiceImpl extends BaseService implements SyncElasticsearchService {

    @Autowired
    private ItemsCustomMapper itemsCustomMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<SearchItemsVO> searchItems() {
        Map<String, Object> map = new HashMap<>();
        return itemsCustomMapper.searchItems(map);
    }
}
