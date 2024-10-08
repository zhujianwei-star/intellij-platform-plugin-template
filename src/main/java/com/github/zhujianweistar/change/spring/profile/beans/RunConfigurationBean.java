package com.github.zhujianweistar.change.spring.profile.beans;

import com.github.zhujianweistar.change.spring.profile.view.icon.Icons;
import com.intellij.openapi.module.Module;
import com.intellij.psi.NavigatablePsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class RunConfigurationBean {

    private final NavigatablePsiElement psiElement;
    private final com.intellij.openapi.module.Module module;
    @Nullable
    private String configurationName;
    @Nullable
    private RunConfigurationType type;
    @NotNull
    private Icon icon = Icons.getTypeIcon(null);


    public RunConfigurationBean(RunConfigurationType type, @Nullable String configurationName, @Nullable NavigatablePsiElement psiElement, @NotNull com.intellij.openapi.module.Module module) {
        this.setType(type);
        this.configurationName = configurationName;
        this.psiElement = psiElement;
        this.module = module;
    }

    public void navigate(boolean requestFocus) {
        if (psiElement != null) {
            psiElement.navigate(requestFocus);
        }
    }

    public NavigatablePsiElement getPsiElement() {
        return psiElement;
    }

    public @Nullable RunConfigurationType getType() {
        return type;
    }

    public void setType(@Nullable RunConfigurationType type) {
        this.type = type;
        this.icon = Icons.getTypeIcon(type);
    }

    public @Nullable String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(@Nullable String configurationName) {
        this.configurationName = configurationName;
    }

    @NotNull
    public Icon getIcon() {
        return icon;
    }

    public Icon getSelectIcon() {
        return Icons.getTypeIcon(this.type, true);
    }

    public Module getModule() {
        return module;
    }

    public void setParent(@NotNull RunConfigurationBean parent) {
        if ((this.type == null || this.type == RunConfigurationType.SPRINGBOOT) && parent.getType() != null) {
            this.setType(parent.getType());
        }
    }

    @NotNull
    public RunConfigurationBean copyWithParent(@Nullable RunConfigurationBean parent) {
        RunConfigurationBean request = new RunConfigurationBean(this.type, this.configurationName, this.psiElement, this.module);
        if (parent != null) {
            request.setParent(parent);
        }
        return request;
    }

    @NotNull
    public String getIdentity(String... itemIds) {
        RunConfigurationType method = this.type == null ? RunConfigurationType.SPRINGBOOT : this.type;

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

        return String.format("{}[%s]%s(%s)%s", method, icon.getClass(), items.toString());
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
        RunConfigurationBean request = (RunConfigurationBean) o;
        if (type != request.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        return result;
    }
}
