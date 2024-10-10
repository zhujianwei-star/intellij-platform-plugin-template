
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
public enum IconNameType {

    /**
     * SpringBoot
     */
    SPRINGBOOT,

    /**
     * checkbox
     */
    CHECKBOX,

    /**
     * AAA
     */
    aaa,

    ;

    @NotNull
    public static IconNameType[] getValues() {
        return Arrays.stream(IconNameType.values())
                .filter(type -> !type.equals(IconNameType.SPRINGBOOT))
                .toArray(IconNameType[]::new);
    }

    @NotNull
    public static IconNameType parse(@Nullable Object type) {
        try {
            assert type != null;
            if (type instanceof IconNameType) {
                return (IconNameType) type;
            }
            return IconNameType.valueOf(type.toString());
        } catch (Exception ignore) {
            return SPRINGBOOT;
        }
    }
}
