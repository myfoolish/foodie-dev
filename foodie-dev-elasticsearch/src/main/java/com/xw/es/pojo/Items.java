package com.xw.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/11/8
 */
@Document(indexName = "foodie_items", type = "doc", createIndex = false)
public class Items {

    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String itemId;
    @Field(store = true, type = FieldType.Text)
    private String itemName;
    @Field(store = true, type = FieldType.Integer)
    private Integer sellCounts;
    @Field(store = true, type = FieldType.Text, index = false)
    private String imgUrl;
    @Field(store = true, type = FieldType.Integer)
    private Integer price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
