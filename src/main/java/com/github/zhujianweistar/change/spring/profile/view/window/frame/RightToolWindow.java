package com.github.zhujianweistar.change.spring.profile.view.window.frame;

import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationType;
import com.github.zhujianweistar.change.spring.profile.service.topic.ServiceTreeTopic;
import com.github.zhujianweistar.change.spring.profile.utils.PomUtil;
import com.github.zhujianweistar.change.spring.profile.utils.RunConfigurationUtil;
import com.github.zhujianweistar.change.spring.profile.view.window.ChangeSpringProfileWindowFactory;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhuJW
 */
public class RightToolWindow extends JPanel {

    public static final Map<RunConfigurationType, Boolean> METHOD_CHOOSE_MAP;
    private static final float DEFAULT_PROPORTION = 0.5F;

    static {
        RunConfigurationType[] values = RunConfigurationType.values();
        METHOD_CHOOSE_MAP = new ConcurrentHashMap<>(values.length);
        for (RunConfigurationType value : values) {
            METHOD_CHOOSE_MAP.put(value, true);
        }
    }

    /**
     * 项目对象
     */
    private final Project project;
    private final RunConfigurationTree runConfigurationTree;

    /**
     * Create the panel.
     */
    public RightToolWindow(@NotNull Project project) {
        super(new BorderLayout());
        this.project = project;
        this.runConfigurationTree = new RunConfigurationTree(project);


        AnAction action = ActionManager.getInstance().getAction(ChangeSpringProfileWindowFactory.TOOL_WINDOW_ID + ".Toolbar");
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(
                ActionPlaces.TOOLBAR,
                action instanceof ActionGroup ? ((ActionGroup) action) : new DefaultActionGroup(),
                true
        );
        actionToolbar.setTargetComponent(this);
        this.add(actionToolbar.getComponent(), BorderLayout.NORTH);

        JBSplitter splitter = new JBSplitter(true, RightToolWindow.class.getName(), DEFAULT_PROPORTION);
        splitter.setFirstComponent(this.runConfigurationTree);
        this.add(splitter, BorderLayout.CENTER);

        initEvent();

        DumbService.getInstance(project).smartInvokeLater(this::firstLoad);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        project.getMessageBus().connect().subscribe(ServiceTreeTopic.TOPIC, runConfigurationTree::renderServiceTree);
        // project.getMessageBus().connect().subscribe(RefreshServiceTreeTopic.TOPIC, this::refreshRequestTree);
    }

    private void firstLoad() {
        refreshRequestTree();
    }

    @NotNull
    private Map<String, List<RunConfigurationBean>> getRunConfigurationBeans() {
        Map<String, List<RunConfigurationBean>> allRunConfigurations = RunConfigurationUtil.getAllRunConfigurations(project);

        allRunConfigurations.forEach((moduleName, requests) -> requests.removeIf(next -> !METHOD_CHOOSE_MAP.get(next.getType())));

        return allRunConfigurations;
    }

    @NotNull
    public Project getProject() {
        return this.project;
    }

    public void refreshRequestTree() {

        ServiceTreeTopic serviceTreeTopic = project.getMessageBus().syncPublisher(ServiceTreeTopic.TOPIC);
        DumbService.getInstance(project).runWhenSmart(() -> serviceTreeTopic.action(getRunConfigurationBeans()));

        // 清除RestDetail中的Cache缓存
        /* RestDetailTopic changeDetailTopic = project.getMessageBus().syncPublisher(RestDetailTopic.TOPIC);
        changeDetailTopic.clearCache(null); */

        // 清除扫描的pom文件缓存
        PomUtil.clearCaches();
    }

    public void navigationToView(@NotNull PsiMethod psiMethod) {
        ChangeSpringProfileWindowFactory.showWindow(project, () -> runConfigurationTree.navigationToTree(psiMethod));
    }
}
