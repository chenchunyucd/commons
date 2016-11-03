package ccy.work.commons.exception;

/**
 * desc: excel模板错误时发生的异常
 * project: activity
 * Created by yfliqiang@jd.com
 * 2015-05-14 15:59
 */
public class WrongTemplateException extends Exception {
    public WrongTemplateException() {
        super();
    }

    public WrongTemplateException(String message) {
        super(message);
    }

    public WrongTemplateException(String message, Throwable e) {
        super(message, e);
    }
}
