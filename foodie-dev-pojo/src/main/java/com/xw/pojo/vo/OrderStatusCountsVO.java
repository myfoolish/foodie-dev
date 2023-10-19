package com.xw.pojo.vo;

/**
 * @author liuxiaowei
 * @Description 订单状态概览数量VO
 * @date 2022/4/28
 */
public class OrderStatusCountsVO {
    private int waitPayCounts;
    private int waitDeliverCounts;
    private int waitReceiveCounts;
    private int waitCommentCounts;

    public OrderStatusCountsVO(int waitPayCounts, int waitDeliverCounts, int waitReceiveCounts, int waitCommentCounts) {
        this.waitPayCounts = waitPayCounts;
        this.waitDeliverCounts = waitDeliverCounts;
        this.waitReceiveCounts = waitReceiveCounts;
        this.waitCommentCounts = waitCommentCounts;
    }

    public int getWaitPayCounts() {
        return waitPayCounts;
    }

    public void setWaitPayCounts(int waitPayCounts) {
        this.waitPayCounts = waitPayCounts;
    }

    public int getWaitDeliverCounts() {
        return waitDeliverCounts;
    }

    public void setWaitDeliverCounts(int waitDeliverCounts) {
        this.waitDeliverCounts = waitDeliverCounts;
    }

    public int getWaitReceiveCounts() {
        return waitReceiveCounts;
    }

    public void setWaitReceiveCounts(int waitReceiveCounts) {
        this.waitReceiveCounts = waitReceiveCounts;
    }

    public int getWaitCommentCounts() {
        return waitCommentCounts;
    }

    public void setWaitCommentCounts(int waitCommentCounts) {
        this.waitCommentCounts = waitCommentCounts;
    }
}
