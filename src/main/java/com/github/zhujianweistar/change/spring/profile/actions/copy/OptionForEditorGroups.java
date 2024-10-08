/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: Test
  Author:   ZhuJW
  Date:     2020/7/7 00:12
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.actions.copy;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class OptionForEditorGroups extends DefaultActionGroup implements CopyOption {

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(withPsiMethod(e));
    }
}
