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

import cn.hutool.http.*;
import com.github.zhujianweistar.change.spring.profile.beans.YMLEnvBean;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.JBUI;
import org.intellij.lang.annotations.Language;
import org.jdesktop.swingx.JXButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class ChangeDetail extends JPanel {

    private static final String IDENTITY_HEAD = "HEAD";
    private static final String IDENTITY_BODY = "BODY";
    private final Project project;
    private final ThreadPoolExecutor poolExecutor;
    /**
     * 输入框 - url地址
     */
    private JTextField requestUrl;
    /**
     * 按钮 - 发送请求
     */
    private JButton sendRequest;
    /**
     * 选项卡面板 - 请求信息
     */
    private JBTabs tabs;
    /**
     * 文本域 - 请求体
     */
    private TabInfo bodyTab;
    private ComboBox<FileType> requestBodyFileType;
    /**
     * 标签 - 显示返回结果
     */
    private TabInfo responseTab;

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
        initView();

        // initEvent();
    }

    private void initView() {
        setLayout(new BorderLayout(0, 0));

        JPanel panelInput = new JPanel();
        add(panelInput, BorderLayout.NORTH);
        panelInput.setLayout(new BorderLayout(0, 0));


        requestUrl = new JBTextField();
        panelInput.add(requestUrl);
        requestUrl.setColumns(45);

        panelInput.add(sendRequest, BorderLayout.EAST);

        tabs = new JBTabsImpl(project);


        tabs.addTab(bodyTab);
        tabs.addTab(responseTab);

        add(tabs.getComponent(), BorderLayout.CENTER);

        JPanel bodyFileTypePanel = new JPanel(new BorderLayout());
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
    }

    public void chooseRequest(@Nullable YMLEnvBean ymlEnvBean) {
        this.ymlEnvBean = ymlEnvBean;
        String reqUrl = null;
        String reqHead = null;
        String reqBody = null;

        try {
            if (ymlEnvBean != null) {
                String ymlEnvName = ymlEnvBean.getYmlEnvName();
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

        requestUrl.setText(reqUrl);
    }

    public void setCallback(DetailHandle callback) {
        this.callback = callback;
    }

    public void reset() {
        this.chooseRequest(null);
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
