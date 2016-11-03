package ccy.work.commons.utils;



import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 脱敏工具类，如果需支持vm则需要在spring文件配置
 * Created by chenchunyu on 2016/5/13.
 */
public class DesensitizationUtil {

    private static Log log = LogFactory.getLog(DesensitizationUtil.class);

    /**
     * 脱敏邮箱地址
     * 规则：Test@jd.com  > T***@jd.com ;chengd@jd.com > c*****@jd.com
     * @param email
     * @return
     */
    public static String email(String email){
        try {
            if(StringUtils.isBlank(email)){
                return "";
            }
            Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
            Matcher m = p.matcher(email);
            if(!m.matches()){//不是邮箱格式的直接返回
                return email;
            }
            int index = email.indexOf("@");
            if(index<=-1 || index==0 || index==(email.length()+1)){//没有@符号或者@在第一个字符或者在最后一个字符
                return email;
            }

            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }catch (Exception e){
            log.error("DesensitizationUtils-email",e);
            return email;
        }
    }

    /**
     * 脱敏手机号码
     * 规则：12345678910 > 123****8910
     * @param phoneNo
     * @return
     */
    public static String phoneNo(String phoneNo){
        try {
            if(StringUtils.isBlank(phoneNo)){
                return "";
            }
            return StringUtils.left(phoneNo, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(phoneNo, 4), StringUtils.length(phoneNo), "*"), "***"));
        }catch (Exception e){
            log.error("DesensitizationUtils-phoneNo",e);
            return phoneNo;
        }
    }

    /**
     * 脱敏中文名字
     * 规则：成吉思汗 > 成***；陈真 > 陈*
     * @param name
     * @return
     */
    public static String chineseName(String name){
        try {
            if(StringUtils.isBlank(name)){
                return "";
            }
            return StringUtils.rightPad(StringUtils.left(name, 1),StringUtils.length(name),"*");
        }catch (Exception e){
            log.error("DesensitizationUtils-chineseName",e);
            return name;
        }
    }

    /**
     * 银行账户
     * 规则：前6位后4位显示，其他*号替代
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        try {
            if (StringUtils.isBlank(cardNum)) {
                return "";
            }
            return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
        }catch (Exception e){
            log.error("DesensitizationUtils-bankCard",e);
            return cardNum;
        }

    }

    /**
     * 默认脱敏方式
     * 显示第一个字符，其他用*号替代
     * @param content
     * @return
     */
    public static String def(String content){
        try {
            if (StringUtils.isBlank(content)) {
                return "";
            }
            return StringUtils.rightPad(StringUtils.left(content, 1),StringUtils.length(content),"*");
        }catch (Exception e){
            log.error("DesensitizationUtils-def",e);
            return content;
        }
    }


    /**
     * 统一入口
     * @param type
     * @param content
     * @return
     */
    public static String desensitization(DesensitizationUtil.Type type,String content){
        if(null == type || StringUtils.isBlank(content)){
            return content;
        }
        switch (type){
            case CHINESE_NAME:
                return chineseName(content);
            case EMAIL:
                return email(content);
            case MOBILPHONE:
                return phoneNo(content);
            case BANK_CARD:
                return bankCard(content);
            case DEFAULT:
                return def(content);
            default:
                return def(content);
        }
    }

    public enum Type{
        DEFAULT,CHINESE_NAME,MOBILPHONE,EMAIL,BANK_CARD;
    }
}
