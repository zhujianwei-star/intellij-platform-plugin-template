/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: Icons
  Author:   ZhuJW
  Date:     2020/5/6 10:39
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.view.icon;

import com.github.zhujianweistar.change.spring.profile.beans.IconNameType;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationType;
import com.github.zhujianweistar.change.spring.profile.beans.settings.Settings;
import com.intellij.icons.AllIcons;
import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class Icons {

    public static Icon Plugin = load("/META-INF/pluginIcon.svg");

    @NotNull
    public static Icon load(@NotNull String path) {
        return IconManager.getInstance().getIcon(path, Icons.class);
    }

    /**
     * 获取方法对应的图标
     *
     * @param type 请求类型
     * @return icon
     */
    @NotNull
    public static Icon getTypeIcon(@Nullable IconNameType type) {
        return getTypeIcon(type, false);
    }

    public static Icon getTypeIcon(@Nullable IconNameType type, boolean selected) {
        IconType iconType = Settings.IconTypeOptionForm.ICON_TYPE_SCHEME.getData();
        type = type == null ? IconNameType.SPRINGBOOT : type;
        if (selected) {
            return iconType.getSelectIcon(type);
        }
        return iconType.getDefaultIcon(type);
    }

    /**
     * 获取方法对应的图标
     *
     * @return icon
     */
    @NotNull
    public static Icon getCheckBoxIcon() {
        return AllIcons.Diff.GutterCheckBox;
    }

    public static Icon getCheckBoxIcon(boolean selected) {
        IconType iconType = new CuteIconType();
        if (selected) {
            return AllIcons.Diff.GutterCheckBoxSelected;
        }
        return AllIcons.Diff.GutterCheckBox;
    }
}
