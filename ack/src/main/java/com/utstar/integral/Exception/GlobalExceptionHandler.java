package com.utstar.integral.Exception;

import com.utstar.integral.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by UTSC1244 at 2019/5/10
 *
 * controller 层统一异常处理
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public Object ExceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response)
    {
        log.error("error.", ex);
        if(StringUtils.isNotBlank(request.getHeader("X-Requested-With")))//ajax 请求
        {
            Result result = new Result(false);
            result.setMessage("server is error");
            return new ResponseEntity<Result>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else
        {
            //TODO 500 页面
        }
        return "";
    }

    @ExceptionHandler(CoreException.class)
    @ResponseBody
    public Object CoreExceptionHandler(CoreException ex)
    {
        Result result = new Result(false);
        result.setMessage(ex.getMessage());
        return result;
    }

    @ExceptionHandler(LoginException.class)
    public Object loginExceptionHandler(LoginException ex, HttpServletRequest request)
    {
        if(StringUtils.isNotBlank(request.getHeader("X-Requested-With")))
        {
            Result result = new Result(false);
            result.setMessage("need to login");
            return new ResponseEntity<Result>(result, HttpStatus.UNAUTHORIZED);
        }else
        {
            return "redirect:/login";
        }
    }
}
