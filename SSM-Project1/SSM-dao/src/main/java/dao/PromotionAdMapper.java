package dao;

import domain.PromotionAd;

import java.util.List;

public interface PromotionAdMapper {
    public List<PromotionAd> findAllPromotionByPage();

    public void updatePromotionAdStatus(PromotionAd promotionAd);
}
