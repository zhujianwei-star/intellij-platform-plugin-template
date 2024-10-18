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
import com.github.zhujianweistar.change.spring.profile.beans.YMLEnvBean;
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
        } else if (value instanceof RunConfigurationTree.CheckBoxTreeNode<?>) {
            RunConfigurationTree.CheckBoxTreeNode node = (RunConfigurationTree.CheckBoxTreeNode) value;
            Object data = node.getData();
            if (data instanceof YMLEnvBean ymlEnvBean) {
                setMethodTypeAndPath(node,ymlEnvBean, selected);
            }
        } else if (value instanceof RunConfigurationTree.ControllerNode) {
            RunConfigurationTree.ControllerNode node = (RunConfigurationTree.ControllerNode) value;
            ClassTree data = node.getData();
            setIcon(data.getIcon());
            append(data.getName());
            append(" - " + data.getQualifiedName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
        }else if (value instanceof RunConfigurationTree.RunConfigurationBeanNode) {
            RunConfigurationTree.RunConfigurationBeanNode node = (RunConfigurationTree.RunConfigurationBeanNode) value;
            @Nullable RunConfigurationBean data = node.getData();
            setIcon(data.getIcon());
            append(data.getConfigurationName());
            append(" - " + data.getConfigurationName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
        } else if (value instanceof RunConfigurationTree.TreeNode<?>) {
            RunConfigurationTree.TreeNode<?> node = (RunConfigurationTree.TreeNode<?>) value;
            append(node.toString());
        }
    }

    private void setMethodTypeAndPath(RunConfigurationTree.CheckBoxTreeNode checkBoxTreeNode, @Nullable YMLEnvBean node, boolean selected) {
        if (node == null) {
            return;
        }
        if (checkBoxTreeNode.isChecked()) {
            setIcon(node.getSelectIcon());
        } else {
            setIcon(node.getIcon());
        }
        String path = node.getYmlEnvName();
        if (path != null) {
            append(path);
        }
    }
}
