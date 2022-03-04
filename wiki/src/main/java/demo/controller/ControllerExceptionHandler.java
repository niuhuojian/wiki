package demo.controller;

import demo.exception.BusinessException;
import demo.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



//对controller做统一的异常处理，或者是数据的预处理
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger Log= LoggerFactory.getLogger(ControllerExceptionHandler.class);

    //针对BindException进行处理，如果是全部异常填Exception
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BindException e){
        CommonResp commonResp=new CommonResp();
        Log.warn("参数校验失败：{}",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResp;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BusinessException e){
        CommonResp commonResp=new CommonResp();
        Log.warn("业务异常：{}",e.getCode().getDesc());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getCode().getDesc());
        return commonResp;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp validExceptionHandler(Exception e){
        CommonResp commonResp=new CommonResp();
        Log.error("系统异常：",e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统出现异常");
        return commonResp;
    }
}
