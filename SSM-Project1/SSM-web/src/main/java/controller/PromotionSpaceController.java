package controller;

import domain.PromotionSpace;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.PromotionSpaceService;

import java.util.List;

@RestController
@RequestMapping("/PromotionSpace")
public class PromotionSpaceController {
    @Autowired
    private PromotionSpaceService promotionSpaceService;

    @RequestMapping("/findAllPromotionSpace")
    public ResponseResult findAllPromotionSpace(){
        List<PromotionSpace> allPromotionSpace = promotionSpaceService.findAllPromotionSpace();
        return new ResponseResult(true,200,"查询所以广告位成功",allPromotionSpace);
    }

    @RequestMapping("/saveOrUpdatePromotionSpace")
    public ResponseResult saveOrUpdatePromotionSpace(@RequestBody PromotionSpace promotionSpace){
        if(promotionSpace.getId()==null){
            promotionSpaceService.savePromotionSpace(promotionSpace);
            return new ResponseResult(true,200,"新增广告位成功",null);
        }else{
            promotionSpaceService.updatePromotionSpace(promotionSpace);
            return new ResponseResult(true,200,"更新广告位成功",null);
        }
    }

    @RequestMapping("/findPromotionSpaceById")
    public ResponseResult findPromotionSpaceById(Integer id){
        PromotionSpace promotionSpaceById = promotionSpaceService.findPromotionSpaceById(id);
        return new ResponseResult(true,200,"查询具体广告位成功",promotionSpaceById);
    }
}
