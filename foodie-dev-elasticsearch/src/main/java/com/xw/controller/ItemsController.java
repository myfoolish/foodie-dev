package com.xw.controller;

import com.xw.service.ItemsElasticsearchService;
import com.xw.utils.JSONResult;
import com.xw.utils.PagedGridResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/7
 */
@RestController
@RequestMapping("/items")
public class ItemsController {

    @Autowired
    private ItemsElasticsearchService itemsElasticsearchService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Elasticsearch";
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/es/search")
    public JSONResult search(String keywords, String sort, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JSONResult.errorMsg("搜索内容不能为空");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }

        page--;
        PagedGridResult grid = itemsElasticsearchService.searchItems(keywords, sort, page, pageSize);
        return JSONResult.ok(grid);
    }
}
