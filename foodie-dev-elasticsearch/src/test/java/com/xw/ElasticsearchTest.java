package com.xw;

import com.xw.es.pojo.LEs;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/10/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchTest {

    /**
     * 不建议使用 ElasticsearchTemplate 对索引进行管理（创建索引、更新映射、删除索引）
     *      1、属性（FieldType）类型不灵活
     *      2、主分片和与副本分片数无法设置
     * 索引就像数据库或者数据库中的表，平时很少通过java代码去创建修改数据库或者数据库中的表，
     * 只会针对表中的数据进行crud操作，es中同理，ElasticsearchTemplate 主要是用来对文档进行crud
     */
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Test
    public void createIndex() {
        LEs lEs = new LEs();
        lEs.setlId(2001L);
        lEs.setlName("es");
        lEs.setlAge(27);
        IndexQuery query = new IndexQueryBuilder().withObject(lEs).build();
        // 创建索引
//        esTemplate.createIndex(LEs.class);  // 创建索引
        esTemplate.index(query);    // 创建索引并插入数据
    }

    @Test
    public void deleteIndex() {
        esTemplate.deleteIndex(LEs.class);
    }

//    -------------- 对文档进行操作 ------------------
    @Test
    public void delete() {
        esTemplate.delete(LEs.class, "1001");
    }

    @Test
    public void update() {
        Map<String, Object> map = new HashMap<>();
        map.put("lName", "les");

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(map);

        UpdateQuery query = new UpdateQueryBuilder()
                .withClass(LEs.class)
                .withId("1001")
                .withIndexRequest(indexRequest)
                .build();
        esTemplate.update(query);
    }

    @Test
    public void query() {
        GetQuery query = new GetQuery();
        query.setId("1001");
        LEs lEs = esTemplate.queryForObject(query, LEs.class);
        System.out.println(lEs);
    }

    @Test
    public void queryForPage() {
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("lName", "lEs"))
                .withPageable(PageRequest.of(0, 1)) //分页
                .build();
        AggregatedPage<LEs> paged = esTemplate.queryForPage(query, LEs.class);
        System.out.println(paged.getTotalPages());
        List<LEs> pagedList = paged.getContent();
        for (LEs lEs : pagedList) {
            System.out.println(lEs);
        }
    }

    @Test
    public void queryForHighLight() {
        String preTag = "<font color='red'>";
        String postTag = "<\font>";

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("lName", "lEs"))
                .withPageable(PageRequest.of(0, 1)) //分页
                .withHighlightFields(new HighlightBuilder.Field("lName").preTags(preTag).postTags(postTag))
                .build();

        AggregatedPage<LEs> paged = esTemplate.queryForPage(query, LEs.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                List<LEs> highlightList = new ArrayList<>();
                for (SearchHit hit : hits) {
                    Integer lId = (Integer) hit.getSourceAsMap().get("lId");
                    HighlightField field = hit.getHighlightFields().get("lName");
                    Integer lAge = (Integer) hit.getSourceAsMap().get("lAge");
                    String highlightData = field.getFragments()[0].string();
                    LEs highlightLEs = new LEs();
                    highlightLEs.setlId(Long.valueOf(lId));
                    highlightLEs.setlName(highlightData);
                    highlightLEs.setlAge(lAge);
                    highlightList.add(highlightLEs);
                }
                if (highlightList.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) highlightList);
                }
                return null;
            }
        });
        System.out.println(paged.getTotalPages());
        List<LEs> pagedList = paged.getContent();
        for (LEs lEs : pagedList) {
            System.out.println(lEs);
        }
    }

    @Test
    public void queryForSort() {

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("lName", "lEs"))
                .withPageable(PageRequest.of(0, 3)) //分页
                .withSort(new FieldSortBuilder("lAge").order(SortOrder.ASC))
                .withSort(new FieldSortBuilder("lId").order(SortOrder.ASC)) // 可以有多个条件排序
                .build();

        AggregatedPage<LEs> paged = esTemplate.queryForPage(query, LEs.class);
        System.out.println(paged.getTotalPages());
        List<LEs> pagedList = paged.getContent();
        for (LEs lEs : pagedList) {
            System.out.println(lEs);
        }
    }
}
