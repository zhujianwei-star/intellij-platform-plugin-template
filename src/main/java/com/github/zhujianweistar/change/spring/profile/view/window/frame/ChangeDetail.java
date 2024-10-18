/*
  Copyright (C), 2018-2020, ZhuJW
  FileName: ChangeDetail
  Author:   ZhuJW
  Date:     2020/5/21 23:54
  Description: 
  History:
  <author>          <time>          <version>          <desc>
  作者姓名            修改时间           版本号              描述
 */
package com.github.zhujianweistar.change.spring.profile.view.window.frame;

import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.beans.YMLEnvBean;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.spring.boot.run.SpringBootApplicationRunConfiguration;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLFile;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class ChangeDetail extends JPanel {

    private final Project project;
    private final ThreadPoolExecutor poolExecutor;

    private DetailHandle callback;
    /**
     * 选中的Request
     */
    private YMLEnvBean ymlEnvBean;

    public ChangeDetail(@NotNull Project project) {
        this.project = project;
        this.poolExecutor = new ThreadPoolExecutor(
                1,
                1,
                1000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(8),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        // initView();

        // initEvent();
    }

    private void initView() {
        setLayout(new BorderLayout(0, 0));

        JPanel panelInput = new JPanel();
        add(panelInput, BorderLayout.NORTH);
        panelInput.setLayout(new BorderLayout(0, 0));
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
    }

    public void chooseYmlEnv(@Nullable YMLEnvBean ymlEnvBean) {
        this.ymlEnvBean = ymlEnvBean;
        String reqUrl = null;
        String reqHead = null;
        String reqBody = null;

        try {
            if (ymlEnvBean != null) {
                String ymlEnvName = ymlEnvBean.getYmlEnvName();
                // 获取当前启动的SpringBoot实例，然后将profile改变，然后将图标改变

                // 获取当前项目的 RunManager
                RunManager runManager = RunManager.getInstance(project);
                // 获取所有运行配置
                java.util.List<RunConfiguration> configurations = runManager.getAllConfigurationsList();

                java.util.List<RunConfigurationBean> runConfigurationBeans = new ArrayList<>(0);
                // 遍历所有配置并修改运行文件
                for (RunConfiguration configuration : configurations) {
                    if (configuration instanceof SpringBootApplicationRunConfiguration springBootApplicationRunConfiguration) {
                        springBootApplicationRunConfiguration.setActiveProfiles(ymlEnvName);
                    }
                }

                // 修改图标
                // 获取父节点
                RunConfigurationTree.RunConfigurationBeanNode parent = ymlEnvBean.getParent();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    TreeNode childAt = parent.getChildAt(i);
                    if (childAt instanceof RunConfigurationTree.CheckBoxTreeNode<?> checkBoxTreeNode) {
                        Object data = checkBoxTreeNode.getData();
                        if (data instanceof YMLEnvBean nodeYmlEnvBean) {
                            assert ymlEnvName != null;
                            if (ymlEnvName.equals(ymlEnvBean.getYmlEnvName())) {
                                checkBoxTreeNode.setChecked(true);
                            } else {
                                checkBoxTreeNode.setChecked(false);
                            }
                        }
                    }
                }
            }
        } catch (PsiInvalidElementAccessException e) {
            /*
            @Throws Code: request.getPsiMethod().getResolveScope()
            @Throws Message: 无效访问，通常代表指向方法已被删除
             */
            if (callback != null) {
                callback.handle();
            }
        }
    }

    public void setCallback(DetailHandle callback) {
        this.callback = callback;
    }

    public void reset() {
        this.chooseYmlEnv(null);
    }


    @NotNull
    private Pattern compileRegExp(@NotNull final String reg) {
        return Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
    }

    public interface DetailHandle {

        /**
         * 处理逻辑
         */
        void handle();
    }
}
