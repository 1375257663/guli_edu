package com.qx.guli.service.base.exception;

import com.qx.guli.common.base.result.ResultCodeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname GuliException
 * @Description TODO
 * @Date 2020/6/7 21:51
 * @Created by 卿星
 */
@Data
@Slf4j
public class GuliException extends RuntimeException {

    // 状态码
    private Integer code;

    /**
     * 接收状态码和消息
     * @param message
     * @param code
     */
    public GuliException(String message,Integer code){
        super(message);
        this.code = code;
    }

    /**
     *  接收枚举类型
     * @param codeEnum
     */
    public GuliException(ResultCodeEnum codeEnum){
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ",message="+this.getMessage()+
                '}';
    }
}
