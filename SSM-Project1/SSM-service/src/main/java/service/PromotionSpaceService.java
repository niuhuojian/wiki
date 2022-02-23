package service;

import domain.PromotionSpace;

import java.util.List;

public interface PromotionSpaceService {
    public List<PromotionSpace> findAllPromotionSpace();

    public void savePromotionSpace(PromotionSpace promotionSpace);

    public PromotionSpace findPromotionSpaceById(Integer id);

    public void updatePromotionSpace(PromotionSpace promotionSpace);
}
