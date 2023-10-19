package com.xw.service;

import com.xw.pojo.Items;
import com.xw.pojo.ItemsImg;
import com.xw.pojo.ItemsParam;
import com.xw.pojo.ItemsSpec;
import com.xw.pojo.vo.CommentLevelCountsVO;
import com.xw.pojo.vo.ShopCartVO;
import com.xw.utils.PagedGridResult;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
public interface ItemsService {

    /**
     * 根据商品 ID 查询详情
     * @param itemId 商品 ID
     * @return
     */
    public Items queryItemsById(String itemId);

    /**
     * 根据商品 ID 查询商品图片列表
     * @param itemId 商品 ID
     * @return
     */
    public List<ItemsImg> queryItemsImgList(String itemId);

    /**
     * 根据商品 ID 查询商品规格
     * @param itemId 商品 ID
     * @return
     */
    public List<ItemsSpec> queryItemsSpecList(String itemId);

    /**
     * 根据商品 ID 查询商品参数
     * @param itemId 商品 ID
     * @return
     */
    public ItemsParam queryItemsParam(String itemId);

    /**
     * 根据商品 ID 查询商品的评价等级数量
     * @param itemId 商品 ID
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品 ID 查询商品的评价（分页）
     * @param itemId 商品 ID
     * @param level 商品评价等级
     * @param page  第几页
     * @param pageSize  每页显示条数
     * @return
     */
    public PagedGridResult queryPagedItemsComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据商品分类 ID 搜索商品列表
     * @param categoryId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItemsByThirdCategoryId(Integer categoryId, String sort, Integer page, Integer pageSize);

    /**
     * 根据商品规格 ids 查询最新的购物车商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIds
     * @return
     */
    public List<ShopCartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品规格 id 查询规格对象的具体信息
     * @param specId
     * @return
     */
    public ItemsSpec queryItemsBySpecId(String specId);

    /**
     * 根据商品 id 查询商品图片中主图的 url
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemsSpecStock(String specId, int buyCounts);
}
