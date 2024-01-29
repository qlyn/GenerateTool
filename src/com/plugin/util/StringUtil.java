package com.plugin.util;

import java.io.UnsupportedEncodingException;

/**
 * 处理插件中String显示的问题
 *
 * @author 沈钦林
 * @date 2023/9/28 22:20
 */
public class StringUtil {
    /**
     * 同时处理String中的中文乱码问题和String的/r替换为Tab，在生成代码插入时无法识别\r会报错
     * @param str
     * @return
     */
    public static String handleEncodeAndTab(String str) {
        str=handleEncode(str);
        return handleTab(str);
    }
    /**
     * 处理String中的中文乱码问题
     *
     * @param str
     * @return
     */
    public static String handleEncode(String str) {
        try {
            return new String(str.getBytes("gbk"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理String的/r替换为Tab，在生成代码插入时无法识别\r会报错
     *
     * @param str
     * @return
     */
    public static String handleTab(String str) {
        return new String(str.replace("\r", "       \n"));
    }
}
