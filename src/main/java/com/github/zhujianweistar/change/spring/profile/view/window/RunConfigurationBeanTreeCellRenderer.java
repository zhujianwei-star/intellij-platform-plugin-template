/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: RestfulTreeCellRenderer
  Author:   ZhuJW
  Date:     2020/5/6 15:41
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.view.window;

import com.github.zhujianweistar.change.spring.profile.beans.ClassTree;
import com.github.zhujianweistar.change.spring.profile.beans.ModuleTree;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.view.window.frame.RunConfigurationTree;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class RunConfigurationBeanTreeCellRenderer extends ColoredTreeCellRenderer {

    @Override
    public void customizeCellRenderer(
            @NotNull JTree tree, Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row, boolean hasFocus) {
        if (value instanceof RunConfigurationTree.ModuleNode) {
            RunConfigurationTree.ModuleNode node = (RunConfigurationTree.ModuleNode) value;
            ModuleTree data = node.getData();
            setIcon(data.getIcon());
            append(data.toString());
        } else if (value instanceof RunConfigurationTree.RunConfigurationBeanNode) {
            RunConfigurationTree.RunConfigurationBeanNode node = (RunConfigurationTree.RunConfigurationBeanNode) value;
            RunConfigurationBean data = node.getData();
            setMethodTypeAndPath(data, selected);
        } else if (value instanceof RunConfigurationTree.RunConfigurationBeanNode) {
            RunConfigurationTree.ControllerNode node = (RunConfigurationTree.ControllerNode) value;
            ClassTree data = node.getData();
            setIcon(data.getIcon());
            append(data.getName());
            append(" - " + data.getQualifiedName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
        } else if (value instanceof RunConfigurationTree.TreeNode<?>) {
            RunConfigurationTree.TreeNode<?> node = (RunConfigurationTree.TreeNode<?>) value;
            append(node.toString());
        }
    }

    private void setMethodTypeAndPath(@Nullable RunConfigurationBean node, boolean selected) {
        if (node == null) {
            return;
        }
        if (selected) {
            setIcon(node.getSelectIcon());
        } else {
            setIcon(node.getIcon());
        }
        String path = node.getConfigurationName();
        if (path != null) {
            append(path);
        }
    }
}
