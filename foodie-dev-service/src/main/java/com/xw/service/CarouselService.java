package com.xw.service;

import com.xw.pojo.Carousel;

import java.util.List;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/31
 */
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}
