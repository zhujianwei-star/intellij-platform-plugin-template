package com.github.zhujianweistar.change.spring.profile.beans;

import com.github.zhujianweistar.change.spring.profile.view.icon.Icons;
import com.github.zhujianweistar.change.spring.profile.view.window.frame.RunConfigurationTree;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class YMLEnvBean {

    @Nullable
    private String ymlEnvName;
    @NotNull
    private Icon icon = Icons.getCheckBoxIcon();
    @NotNull
    private RunConfigurationTree.RunConfigurationBeanNode parent;

    public YMLEnvBean(@Nullable String ymlEnvName, @NotNull Module module) {
        this.ymlEnvName = ymlEnvName;
        List<String> ymlNames = new ArrayList<>();
        ymlNames.add(ymlEnvName);
        RunConfigurationBean runConfigurationBean = new RunConfigurationBean(RunConfigurationType.SPRINGBOOT, "null", null, module, ymlNames);
        this.parent = new RunConfigurationTree.RunConfigurationBeanNode(runConfigurationBean);
    }

    public YMLEnvBean(@Nullable String ymlEnvName, @NotNull RunConfigurationTree.RunConfigurationBeanNode runConfigurationBeanNode) {
        this.ymlEnvName = ymlEnvName;
        this.parent = runConfigurationBeanNode;
    }

    public @Nullable String getYmlEnvName() {
        return ymlEnvName;
    }

    public void setYmlEnvName(@Nullable String ymlEnvName) {
        this.ymlEnvName = ymlEnvName;
    }

    public @NotNull RunConfigurationTree.RunConfigurationBeanNode getParent() {
        return parent;
    }

    @NotNull
    public Icon getIcon() {
        return icon;
    }

    public Icon getSelectIcon() {
        return Icons.getCheckBoxIcon(true);
    }

    @NotNull
    public String getIdentity(String... itemIds) {
        StringBuilder items = new StringBuilder();
        if (itemIds != null) {
            items.append("-[");
            for (int i = 0; i < itemIds.length; i++) {
                if (i > 0) {
                    items.append(", ");
                }
                items.append(itemIds[i]);
            }
            items.append("]");
        }

        return String.format("{}[%s]%s(%s)%s", IconNameType.CHECKBOX.name(), icon.getClass(), items.toString());
    }

    @Override
    public String toString() {
        return this.getIdentity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YMLEnvBean ymlEnvBean = (YMLEnvBean) o;
        if (!Objects.equals(ymlEnvName, ymlEnvBean.ymlEnvName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = ymlEnvName != null ? ymlEnvName.hashCode() : 0;
        return result;
    }
}
