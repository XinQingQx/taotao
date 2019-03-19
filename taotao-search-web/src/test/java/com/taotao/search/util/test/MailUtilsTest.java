package com.taotao.search.util.test;

import com.taotao.search.util.MailUtils;
import org.junit.Test;

import javax.mail.MessagingException;

public class MailUtilsTest {

    @Test
    public void sendEmail() throws MessagingException {
        MailUtils mailUtils = new MailUtils();
        mailUtils.sendEmail("搜索异常" , "查明原因，尽快修复");
    }
}