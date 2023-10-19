package com.xw.service.impl;

import com.xw.es.pojo.Items;
import com.xw.es.pojo.LEs;
import com.xw.service.ItemsElasticsearchService;
import com.xw.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/8
 */
public class ItemsElasticsearchServiceImpl implements ItemsElasticsearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {

        String preTag = "<font color='red'>";
        String postTag = "<\font>";

        String itemNameFiled = "itemName";
        SortBuilder sortBuilder = null;

        if (sort.equals("c")) {
            sortBuilder = new FieldSortBuilder("sellCounts").order(SortOrder.DESC);
        } else if (sort.equals("p")) {
            sortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);
        } else {
            sortBuilder = new FieldSortBuilder("itemName,keyword").order(SortOrder.ASC);
        }

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                .withPageable(PageRequest.of(page, pageSize)) //分页
                .withSort(sortBuilder)
                .withHighlightFields(new HighlightBuilder.Field(itemNameFiled)
                        // .preTags(preTag).postTags(postTag)
                )
                .build();

        AggregatedPage<Items> pagedItems = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                List<Items> itemHighlightList = new ArrayList<>();
                for (SearchHit hit : hits) {
                    String itemId = (String) hit.getSourceAsMap().get("itemId");
                    HighlightField highlightField = hit.getHighlightFields().get(itemNameFiled);
                    String itemName = highlightField.getFragments()[0].string();
                    Integer sellCounts = (Integer) hit.getSourceAsMap().get("sellCounts");
                    String imgUrl = (String) hit.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) hit.getSourceAsMap().get("price");
                    Items highlightLEs = new Items();
                    highlightLEs.setItemId(itemId);
                    highlightLEs.setItemName(itemName);
                    highlightLEs.setSellCounts(sellCounts);
                    highlightLEs.setImgUrl(imgUrl);
                    highlightLEs.setPrice(price);
                    itemHighlightList.add(highlightLEs);
                }
                return new AggregatedPageImpl<>((List<T>) itemHighlightList, pageable, searchResponse.getHits().getTotalHits());
            }
        });

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(pagedItems.getContent());
        pagedGridResult.setPage(page + 1);
        pagedGridResult.setTotal(pagedItems.getTotalPages());
        pagedGridResult.setRecords(pagedItems.getTotalElements());

        System.out.println(pagedItems.getTotalPages());
        List<Items> pagedList = pagedItems.getContent();
        for (Items items : pagedList) {
            System.out.println(items);
        }

        return null;
    }
}
