package com.github.zhujianweistar.change.spring.profile.service.impl;

import com.github.zhujianweistar.change.spring.profile.service.ToolWindowService;
import com.github.zhujianweistar.change.spring.profile.view.window.frame.RightToolWindow;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class ToolWindowServiceImpl implements ToolWindowService {

    private final Project project;

    public ToolWindowServiceImpl(Project project) {
        this.project = project;
    }

    @Override
    public JComponent getContent() {
        return new RightToolWindow(project);
    }
}
