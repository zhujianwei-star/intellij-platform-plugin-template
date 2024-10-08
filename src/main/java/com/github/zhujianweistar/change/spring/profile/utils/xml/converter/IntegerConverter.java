/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: BooleanConverter
  Author:   ZhuJW
  Date:     2020/9/2 01:28
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.utils.xml.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class IntegerConverter extends BaseConverter<Integer> {

    @Nullable
    @Override
    public Integer fromString(@NotNull String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignore) {
        }
        return null;
    }
}
