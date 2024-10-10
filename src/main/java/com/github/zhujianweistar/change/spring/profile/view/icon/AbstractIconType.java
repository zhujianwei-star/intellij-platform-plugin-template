package com.github.zhujianweistar.change.spring.profile.view.icon;

import com.github.zhujianweistar.change.spring.profile.beans.IconNameType;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuJW
 * @version 1.0
 */
public abstract class AbstractIconType extends IconType {

    private final String name;

    private final Map<IconNameType, Icon> defaultIcons;
    private final Map<IconNameType, Icon> selectIcons;

    protected AbstractIconType(@NotNull String name, @NotNull String suffix) {
        this(name, "_select", suffix);
    }

    protected AbstractIconType(@NotNull String name, @NotNull String selectSuffix, @NotNull String suffix) {
        this.name = name;
        this.defaultIcons = new HashMap<>(IconNameType.values().length);
        this.selectIcons = new HashMap<>(IconNameType.values().length);

        for (IconNameType iconNameType : IconNameType.values()) {
            String iconPath = String.format("/icon/%s/%s.%s", name, iconNameType.name(), suffix);
            String iconSelectPath = String.format("/icon/%s/%s%s.%s", name, iconNameType.name(), selectSuffix, suffix);
            Icon defaultIcon = Icons.load(iconPath);
            defaultIcons.put(iconNameType, defaultIcon);
            Icon selectIcon;
            try {
                selectIcon = Icons.load(iconSelectPath);
            } catch (Exception e) {
                selectIcon = defaultIcon;
            }
            selectIcons.put(iconNameType, selectIcon);
        }
    }

    @Override
    public final @NotNull Icon getDefaultIcon(IconNameType nameType) {
        return defaultIcons.get(nameType);
    }

    @Override
    public final @NotNull Icon getSelectIcon(IconNameType nameType) {
        return selectIcons.get(nameType);
    }

    @Override
    public final @NotNull List<PreviewIcon> getDefaultIcons() {
        List<PreviewIcon> list = new ArrayList<>(defaultIcons.size());
        defaultIcons.forEach((method, icon) -> list.add(new PreviewIcon(method.name(), icon)));
        return list;
    }

    @Override
    public final @NotNull List<PreviewIcon> getSelectIcons() {
        List<PreviewIcon> list = new ArrayList<>(selectIcons.size());
        selectIcons.forEach((method, icon) -> list.add(new PreviewIcon(method.name(), icon)));
        return list;
    }

    @Override
    public final @NotNull String toString() {
        return this.name;
    }
}
