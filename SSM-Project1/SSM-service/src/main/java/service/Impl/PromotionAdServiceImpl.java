package service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import dao.PromotionAdMapper;
import domain.PromotionAd;
import domain.PromotionAdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.PromotionAdService;

import java.util.Date;
import java.util.List;

@Service
public class PromotionAdServiceImpl implements PromotionAdService {
    @Autowired
    private PromotionAdMapper promotionAdMapper;

    @Override
    public PageInfo<PromotionAd> findAllPromotionByPage(PromotionAdVO promotionAdVO) {
        PageHelper.startPage(promotionAdVO.getCurrentPage(),promotionAdVO.getPageSize());
        //分页查询
        List<PromotionAd> allPromotionByPage = promotionAdMapper.findAllPromotionByPage();
        //返回具体的广告信息
        //因为之前已经采取了分页操作，此处已经自动完成分页查询的操作
        PageInfo<PromotionAd> pageInfo = new PageInfo<>(allPromotionByPage);
        //封装了一些分页的数据，总页数，每页条数。。。。
        //具体就是将分页后的广告信息，和一些附带的需要显示的分页信息都封装成一个新的pageInfo对象
        return pageInfo;
    }

    @Override
    public void updatePromotionAdStatus(Integer id, Integer status) {
        PromotionAd promotionAd = new PromotionAd();
        promotionAd.setId(id);
        promotionAd.setStatus(status);
        promotionAd.setUpdateTime(new Date());
        promotionAdMapper.updatePromotionAdStatus(promotionAd);
    }
}
