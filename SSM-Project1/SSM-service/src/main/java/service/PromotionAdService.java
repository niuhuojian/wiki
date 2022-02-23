package service;

import com.github.pagehelper.PageInfo;
import domain.PromotionAd;
import domain.PromotionAdVO;

import java.util.List;

public interface PromotionAdService {
    public PageInfo<PromotionAd> findAllPromotionByPage(PromotionAdVO promotionAdVO);

    public void updatePromotionAdStatus(Integer id,Integer status);
}
