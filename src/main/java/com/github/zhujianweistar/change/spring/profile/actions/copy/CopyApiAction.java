package com.github.zhujianweistar.change.spring.profile.actions.copy;

import com.github.zhujianweistar.change.spring.profile.utils.Bundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class CopyApiAction extends AnAction implements CopyOption {

    public CopyApiAction() {
        getTemplatePresentation().setText(Bundle.getString("action.CopyApi.text"));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        copyPath(e, false);
    }
}
