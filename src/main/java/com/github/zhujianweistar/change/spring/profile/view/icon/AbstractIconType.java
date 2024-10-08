package com.github.zhujianweistar.change.spring.profile.view.icon;

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

    private final Map<RunConfigurationType, Icon> defaultIcons;
    private final Map<RunConfigurationType, Icon> selectIcons;

    protected AbstractIconType(@NotNull String name, @NotNull String suffix) {
        this(name, "_select", suffix);
    }

    protected AbstractIconType(@NotNull String name, @NotNull String selectSuffix, @NotNull String suffix) {
        this.name = name;
        this.defaultIcons = new HashMap<>(RunConfigurationType.values().length);
        this.selectIcons = new HashMap<>(RunConfigurationType.values().length);

        for (RunConfigurationType runConfigurationType : RunConfigurationType.values()) {
            String iconPath = String.format("/icons/method/%s/%s.%s", name, runConfigurationType.name(), suffix);
            String iconSelectPath = String.format("/icons/method/%s/%s%s.%s", name, runConfigurationType.name(), selectSuffix, suffix);
            Icon defaultIcon = Icons.load(iconPath);
            defaultIcons.put(runConfigurationType, defaultIcon);
            Icon selectIcon;
            try {
                selectIcon = Icons.load(iconSelectPath);
            } catch (Exception e) {
                selectIcon = defaultIcon;
            }
            selectIcons.put(runConfigurationType, selectIcon);
        }
    }

    @Override
    public final @NotNull Icon getDefaultIcon(RunConfigurationType method) {
        return defaultIcons.get(method);
    }

    @Override
    public final @NotNull Icon getSelectIcon(RunConfigurationType method) {
        return selectIcons.get(method);
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
