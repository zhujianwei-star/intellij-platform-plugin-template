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

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ZhuJW
 * @version 1.0
 */
public abstract class BaseConverter<T> extends Converter<T> {

    /**
     * abstract
     *
     * @param value value
     * @return T
     */
    @Nullable
    @Override
    public abstract T fromString(@NotNull String value);

    @Nullable
    @Override
    public String toString(@NotNull T value) {
        return value.toString();
    }
}
