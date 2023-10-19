package com.xw.pojo.vo;

import java.util.Date;

/**
 * @author liuxiaowei
 * @Description 6个最新商品的简单数据类型
 * @date 2022/1/4
 */
public class SimpleItemsVO {
    private String itemId;
    private String itemName;
    private Date createdTime;
    private String itemUrl;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
