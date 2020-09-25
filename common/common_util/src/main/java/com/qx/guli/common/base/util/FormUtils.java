package com.qx.guli.common.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname FormUtils
 * @Description 字符串操作工具类
 * @Date 2020/6/23 15:01
 * @Created by 卿星
 */
public class FormUtils {

    public static boolean isMobile(String phoneNum){
        // 验证手机号
        Pattern p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$");
        Matcher matcher = p.matcher(phoneNum);
        return matcher.matches();
    }

}
