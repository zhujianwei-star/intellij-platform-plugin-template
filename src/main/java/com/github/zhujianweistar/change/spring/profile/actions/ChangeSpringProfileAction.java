package com.github.zhujianweistar.change.spring.profile.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ChangeSpringProfileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog("嘿嘿嘿，终于可以了!", "Demo Plugin", Messages.getInformationIcon());
    }

}
