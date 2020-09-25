package com.qx.guli.common.base.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname JwtInfo
 * @Description TODO
 * @Date 2020/6/24 15:12
 * @Created by 卿星
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtInfo {

    private String id;
    private String nickname;
    private String avatar;
    //权限、角色等
    //不要存敏感信息

}
