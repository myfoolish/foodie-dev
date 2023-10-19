package com.xw.pojo.vo;

import com.xw.pojo.Items;
import com.xw.pojo.ItemsImg;
import com.xw.pojo.ItemsParam;
import com.xw.pojo.ItemsSpec;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description 商品详情 VO
 * @date 2021/12/31
 */
public class ItemsInfoVO {

    private Items item;
    List<ItemsImg> itemImgList;
    List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
