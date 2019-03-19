package com.taotao.search.exception;

import com.taotao.search.util.MailUtils;
import com.taotao.search.util.StackTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局异常处理
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

    //获取logger
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, final Exception e) {

//        //1.写入日志，这里打印
////        System.out.println(e.getMessage());
////        e.printStackTrace();
////        //2.及时通知开发管理员，发邮件 （通过第三方接口）
////        System.out.println("发短信");

        logger.info("进入全局异常处理器。。。");
        logger.debug("测试handler的类型："+handler.getClass());
        //控制台打印异常
        e.printStackTrace();
        //向日志文件中写入异常
        logger.error("系统发生异常", e);
        //发邮件（采用jmail客户端进行发送）
        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    MailUtils.sendEmail("搜索系统出现异常", StackTrace.getStackTrace(e));
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();

        //3.给用户一个友好提示：你的网络有问题，请重试。
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/exception");
        mav.addObject("message","你的网络有异常，请重试" );


        return mav;
    }
}
