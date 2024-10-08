package com.github.zhujianweistar.change.spring.profile.actions.copy;

import com.github.zhujianweistar.change.spring.profile.actions.EditorOption;
import com.github.zhujianweistar.change.spring.profile.service.Notify;
import com.github.zhujianweistar.change.spring.profile.utils.Bundle;
import com.github.zhujianweistar.change.spring.profile.utils.SystemUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuJW
 * @version 1.0
 */
public interface CopyOption extends EditorOption {

    PsiMethod[] PSI_METHODS = new PsiMethod[1];

    /**
     * 检测当前 PsiClass 是否含有`RestController` | `Controller` | `Path`
     *
     * @param psiMethod psiMethod
     * @return bool
     */
    default boolean hasRestful(@Nullable PsiMethod psiMethod) {
        if (psiMethod == null) {
            return false;
        }
        return false;
    }

    /**
     * 获取当前选择的PsiMethod
     *
     * @param e AnActionEvent
     * @return PsiMethod
     */
    @Nullable
    default PsiMethod getPsiMethod(@NotNull AnActionEvent e) {
        PsiElement currentEditorElement = EditorOption.getCurrentEditorElement(e);
        if (currentEditorElement == null) {
            return null;
        }
        // 如果右键处为当前方法其中的 注解末尾 或 方法体中
        PsiElement editorElementContext = currentEditorElement.getContext();
        if (editorElementContext != null) {
            if (editorElementContext instanceof PsiClass) {
                return null;
            }
            if (editorElementContext instanceof PsiMethod) {
                return ((PsiMethod) editorElementContext);
            }
        }
        return null;
    }


    /**
     * 是否包含PsiMethod
     *
     * @param e AnActionEvent
     * @return bool
     */
    default boolean withPsiMethod(@NotNull AnActionEvent e) {
        if (e.getProject() == null) {
            return false;
        }
        PSI_METHODS[0] = getPsiMethod(e);
        return hasRestful(PSI_METHODS[0]);
    }

    /**
     * 复制
     *
     * @param e    AnActionEvent
     * @param full 全量
     */
    default void copyPath(@NotNull AnActionEvent e, boolean full) {
        Project project = e.getData(LangDataKeys.PROJECT);
        PsiMethod psiMethod = PSI_METHODS[0];
        if (project == null || psiMethod == null) {
            return;
        }
        List<String> requests = new ArrayList<>();
        if (requests.isEmpty()) {
            return;
        }
        if (requests.size() == 1) {
            copyPath(psiMethod, requests.get(0), full);
            return;
        }
        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(requests)
                .setItemChosenCallback(selected -> copyPath(psiMethod, selected, full))
                .setTitle(Bundle.getString("other.prompt.multiItem.selectOne"))
                .setAdText(JBPopupFactory.ActionSelectionAid.SPEEDSEARCH.name())
                .setNamerForFiltering(String::toString)
                .createPopup()
                .showInBestPositionFor(e.getDataContext());
    }

    /**
     * copy
     *
     * @param psiMethod psiMethod
     * @param path      path
     * @param fullPath  full path?
     */
    default void copyPath(@NotNull PsiMethod psiMethod, @NotNull final String path, boolean fullPath) {
        Project project = psiMethod.getProject();
        String contextPath = "auto";
        if (!fullPath) {
            SystemUtil.Clipboard.copy((contextPath == null || "null".equals(contextPath) ? "" : contextPath) + path);
            Notify.getInstance(project).info(Bundle.getString("action.CopyApi.success"));
        } else {
            GlobalSearchScope scope = psiMethod.getResolveScope();
            String protocol = "auto";
            int port = 0;
            SystemUtil.Clipboard.copy(SystemUtil.buildUrl(
                    protocol,
                    port,
                    contextPath,
                    path
            ));
            Notify.getInstance(project).info(Bundle.getString("action.CopyFullPath.success"));
        }
    }
}
