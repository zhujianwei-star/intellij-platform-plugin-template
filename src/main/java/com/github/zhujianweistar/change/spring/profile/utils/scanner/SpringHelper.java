package com.github.zhujianweistar.change.spring.profile.utils.scanner;

import cn.hutool.core.util.StrUtil;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationType;
import com.github.zhujianweistar.change.spring.profile.utils.ProjectConfigUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.spring.boot.run.SpringBootApplicationRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import y.L.k.P;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
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
                    List<YAMLFile> applicationEnvYmlFile = getApplicationEnvYmlFile(project, module);
                    // 获取 application-*.yml 文件
                    RunConfigurationBean runConfigurationBean = getRunConfigurationBean(module, springBootApplicationRunConfiguration, applicationEnvYmlFile);
                    if (StrUtil.isNotBlank(springBootApplicationRunConfiguration.getActiveProfiles())) {
                        runConfigurationBean.setUsedYmlName(springBootApplicationRunConfiguration.getActiveProfiles());
                    }
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

    private static @NotNull RunConfigurationBean getRunConfigurationBean(@NotNull Module module, SpringBootApplicationRunConfiguration springBootApplicationRunConfiguration, List<YAMLFile> applicationEnvYmlFile) {
        List<String> applicationEnvNameList = null;
        if (!applicationEnvYmlFile.isEmpty()) {
            applicationEnvNameList = new ArrayList<>(0);
            for (YAMLFile applicationEnvYml : applicationEnvYmlFile) {
                String applicationEnvName = applicationEnvYml.getName().replace("application-", "").replace(".yml", "");
                applicationEnvNameList.add(applicationEnvName);
            }
        }
        return new RunConfigurationBean(
                RunConfigurationType.SPRINGBOOT,
                springBootApplicationRunConfiguration.getName(),
                null,
                module,
                applicationEnvNameList);
    }

    /**
     * 获取所有的控制器类
     *
     * @param project project
     * @param module  module
     * @return Collection<PsiClass>
     */
    @NotNull
    private static List<YAMLFile> getAllYmlFile(@NotNull Project project, @NotNull Module module) {
        Collection<VirtualFile> ymlVirtualFiles = FilenameIndex.getAllFilesByExt(project, "yml", ProjectConfigUtil.getModuleScope(module));
        List<YAMLFile> ymlFiles = new ArrayList<>(0);
        for (VirtualFile ymlVirtualFile : ymlVirtualFiles) {
            PsiFile file = PsiManager.getInstance(project).findFile(ymlVirtualFile);
            if (file instanceof YAMLFile yamlFile) {
                ymlFiles.add(yamlFile);
            }
        }
        return ymlFiles;
    }

    /**
     * 获取所有的控制器类
     *
     * @param project project
     * @param module  module
     * @return Collection<PsiClass>
     */
    @NotNull
    private static List<YAMLFile> getApplicationEnvYmlFile(@NotNull Project project, @NotNull Module module) {
        List<YAMLFile> allYmlFile = getAllYmlFile(project, module);
        List<YAMLFile> applicationEnvYmlFile = new ArrayList<>(0);
        for (YAMLFile ymlFile : allYmlFile) {
            if (ymlFile.getName().contains("application-")) {
                applicationEnvYmlFile.add(ymlFile);
            }
        }
        return applicationEnvYmlFile;
    }

}
