package com.sicmatr1x.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量
 * @author sicmatr1x
 */
@Component
public class Constant {
    @Value("${constant.version}")
    public String version;

    public String getVersion() {
        return version;
    }


}
