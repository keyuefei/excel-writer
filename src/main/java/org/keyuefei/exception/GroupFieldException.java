package org.keyuefei.exception;

/**
 * @Description 属性异常
 * @Author 003654
 * @Date 2020/6/13
 * @Time 19:23
 */
public class GroupFieldException extends RuntimeException{

    public GroupFieldException(String message) {
        super(message);
    }

    public GroupFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}