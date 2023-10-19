package com.xw.service.center;

import com.xw.pojo.OrderItems;
import com.xw.pojo.bo.center.OrderItemsCommentBO;
import com.xw.utils.PagedGridResult;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/28
 */
public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     * @param orderId
     * @return
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param userId
     * @param orderId
     * @param commentList
     */
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> commentList);


    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
