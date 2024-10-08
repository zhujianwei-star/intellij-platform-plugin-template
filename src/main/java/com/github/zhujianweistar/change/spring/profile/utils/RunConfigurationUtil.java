package com.github.zhujianweistar.change.spring.profile.utils;

import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.utils.scanner.SpringHelper;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunConfigurationUtil {


    private static final Map<String, List<RunConfigurationBean>> RUN_CONFIGURATION_CACHE = Custom.RUN_CONFIGURATION_CACHE;

    /**
     * 获取所有的Request
     *
     * @param project project
     * @return map-{key: moduleName, value: itemRequestList}
     */
    @NotNull
    public static Map<String, List<RunConfigurationBean>> getAllRunConfigurations(@NotNull Project project) {
        return getAllRunConfigurations(project, false);
    }



    /**
     * 获取所有的Request
     *
     * @param hasEmpty 是否生成包含空Request的moduleName
     * @param project  project
     * @return map-{key: moduleName, value: itemRequestList}
     */
    @NotNull
    public static Map<String, List<RunConfigurationBean>> getAllRunConfigurations(@NotNull Project project, boolean hasEmpty) {
        Map<String, List<RunConfigurationBean>> map = new HashMap<>();
        RUN_CONFIGURATION_CACHE.clear();

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            List<RunConfigurationBean> runConfigurationBeans = getModuleRunConfigurationBeans(project, module);
            if (!hasEmpty && runConfigurationBeans.isEmpty()) {
                continue;
            }
            map.put(module.getName(), runConfigurationBeans);

            runConfigurationBeans.forEach(runConfigurationBean -> {
                List<RunConfigurationBean> configurationBeans;
                String runConfigurationKey = runConfigurationBean.getModule().getName() + ":" + runConfigurationBean.getConfigurationName();
                if (RUN_CONFIGURATION_CACHE.containsKey(runConfigurationKey)) {
                    configurationBeans = RUN_CONFIGURATION_CACHE.get(runConfigurationKey);
                } else {
                    configurationBeans = new ArrayList<>();
                    RUN_CONFIGURATION_CACHE.put(runConfigurationKey, configurationBeans);
                }
                configurationBeans.add(runConfigurationBean);
            });
        }
        return map;
    }


    /**
     * 获取选中module的所有Request
     *
     * @param project project
     * @param module  module
     * @return list
     */
    @NotNull
    public static List<RunConfigurationBean> getModuleRunConfigurationBeans(@NotNull Project project, @NotNull Module module) {
        List<RunConfigurationBean> runConfigurationBeans = new ArrayList<>();

        // Spring ApplicationName
        List<RunConfigurationBean> springRequestByModule = SpringHelper.getSpringRunConfigurationBeansByModule(project, module);
        if (!springRequestByModule.isEmpty()) {
            runConfigurationBeans.addAll(springRequestByModule);
        }
        return runConfigurationBeans;
    }


    private static class Custom {

        public static final Map<String, List<RunConfigurationBean>> RUN_CONFIGURATION_CACHE = new HashMap<>();
    }
}
