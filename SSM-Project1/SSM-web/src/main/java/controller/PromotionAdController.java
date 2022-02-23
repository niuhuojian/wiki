package controller;

import com.github.pagehelper.PageInfo;
import domain.PromotionAd;
import domain.PromotionAdVO;
import domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.PromotionAdService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/PromotionAd")
public class PromotionAdController {
    @Autowired
    private PromotionAdService promotionAdService;
    @RequestMapping("/findAllPromotionAdByPage")
    public ResponseResult findAllAdByPage(PromotionAdVO promotionAdVO){
        //因为接口文档中是直接传入参数，而不需要再封装进Vo对象中，所以可以把@RequestBody去除
        PageInfo<PromotionAd> pageInfo = promotionAdService.findAllPromotionByPage(promotionAdVO);
        return new ResponseResult(true,200,"分页查询成功",pageInfo);
    }

    @RequestMapping("/PromotionAdUpload")
    public ResponseResult fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        //1、判断接收到的文件是否为空
        if(file.isEmpty()){
            throw new RuntimeException();
        }
        //2、获取项目部署路径
        //获取到的是在tomcat中部署的绝对路径
        //D:\imoocJavaTest\SSM-Project1\SSM-web\target\SSM-web-1.0-SNAPSHOT\
        String realPath = request.getServletContext().getRealPath("/");
        //将绝对路径截取，只取项目名称之前的部分
        //D:\imoocJavaTest\SSM-Project1\
        //indexOf:指定字符串第一次出现的索引
        String substring = realPath.substring(0, realPath.indexOf("SSM-web"));

        //3、获取文件名
        String originalFilename = file.getOriginalFilename();

        //4、生成新文件名(随意，不重复即可)
        //System.currentTimeMillis().jsp
        //lastIndexOf：指定字符串最后一次出现的索引
        String newFileName=System.currentTimeMillis()+originalFilename.substring(originalFilename.lastIndexOf("."));

        //5、文件上传
        //上传路径
        String uploadPath=substring+"upload\\";
        //实际文件存在路径(上传路径+文件名称)
        //实际上是上传到服务器的文件本身
        File filePath = new File(uploadPath, newFileName);
        //判断目录是否存在，不存在就创建
        if(!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdir();
            System.out.println("创建目录" + filePath);
        }

        //图片进行了上传
        //transferTo：将接收到的文件传输到给定的目标文件。
        file.transferTo(filePath);

        //6、将文件名和文件路径返回，进行响应
        Map<String,String> map=new HashMap<>();
        map.put("fileName",newFileName);
        map.put("filePath","http://localhost:8080/upload/"+newFileName);

        ResponseResult responseResult = new ResponseResult(true, 200, "图片上传成功", map);
        return responseResult;
    }

    @RequestMapping("/updatePromotionAdStatus")
    public ResponseResult updatePromotionAdStatus(Integer id,Integer status){
        promotionAdService.updatePromotionAdStatus(id,status);
        return new ResponseResult(true,200,"修改广告成功",null);
    }
}
