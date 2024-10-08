/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: RequestMethod
  Author:   ZhuJW
  Date:     2020/5/2 00:54
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.beans;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author ZhuJW
 * @version 1.0
 */
public enum RunConfigurationType {

    /**
     * SpringBoot
     */
    SPRINGBOOT,

    ;

    @NotNull
    public static RunConfigurationType[] getValues() {
        return Arrays.stream(RunConfigurationType.values())
                .filter(type -> !type.equals(RunConfigurationType.SPRINGBOOT))
                .toArray(RunConfigurationType[]::new);
    }

    @NotNull
    public static RunConfigurationType parse(@Nullable Object type) {
        try {
            assert type != null;
            if (type instanceof RunConfigurationType) {
                return (RunConfigurationType) type;
            }
            return RunConfigurationType.valueOf(type.toString());
        } catch (Exception ignore) {
            return SPRINGBOOT;
        }
    }
}
