package com.github.zhujianweistar.change.spring.profile.utils.scanner;

import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationType;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.spring.boot.run.SpringBootApplicationRunConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpringHelper {

    @NotNull
    public static List<RunConfigurationBean> getSpringRunConfigurationBeansByModule(@NotNull Project project, @NotNull com.intellij.openapi.module.Module module) {
        List<RunConfigurationBean> moduleList = new ArrayList<>(0);

        moduleList.addAll(getSpringRunConfigurationBeans(project, module));

        return moduleList;
    }

    private static List<RunConfigurationBean> getSpringRunConfigurationBeans(@NotNull Project project, @NotNull com.intellij.openapi.module.Module module) {

        // 获取当前项目的 RunManager
        RunManager runManager = RunManager.getInstance(project);

        // 获取所有运行配置
        List<RunConfiguration> configurations = runManager.getAllConfigurationsList();

        List<RunConfigurationBean> runConfigurationBeans = new ArrayList<>(0);
        // 遍历所有配置并打印信息
        for (RunConfiguration configuration : configurations) {
            if (configuration instanceof SpringBootApplicationRunConfiguration springBootApplicationRunConfiguration) {
                if (module.getName().equals(springBootApplicationRunConfiguration.getModule().getName())) {
                    RunConfigurationBean runConfigurationBean = new RunConfigurationBean(
                            RunConfigurationType.SPRINGBOOT,
                            springBootApplicationRunConfiguration.getName(),
                            null,
                            module);
                    runConfigurationBeans.add(runConfigurationBean);
                }
            }
            System.out.println("Configuration Name: " + configuration.getName());
            System.out.println("Configuration Type: " + configuration.getType());
            System.out.println("Configuration Factory: " + configuration.getFactory());
            System.out.println("Configuration Module: " + configuration.getConfigurationEditor());
        }
        return runConfigurationBeans;
    }

}
