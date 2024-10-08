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
public class BooleanConverter extends BaseConverter<Boolean> {

    @Nullable
    @Override
    public Boolean fromString(@NotNull String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception ignore) {
        }
        return null;
    }
}
