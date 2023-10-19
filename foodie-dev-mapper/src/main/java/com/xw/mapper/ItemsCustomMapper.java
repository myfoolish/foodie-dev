package com.xw.mapper;

import com.xw.pojo.vo.ItemsCommentsVO;
import com.xw.pojo.vo.SearchItemsVO;
import com.xw.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCustomMapper {

    public List<ItemsCommentsVO> queryItemsComments(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItemsByThirdCategoryId(@Param("paramsMap") Map<String, Object> map);

    public List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList") List specIdList);

    public int decreaseItemsSpecStock(@Param("specId") String specId, @Param("pendingCounts") int pendingCounts);
}