package com.github.zhujianweistar.change.spring.profile.view.window.frame;


import com.github.zhujianweistar.change.spring.profile.beans.ClassTree;
import com.github.zhujianweistar.change.spring.profile.beans.ModuleTree;
import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.github.zhujianweistar.change.spring.profile.beans.settings.Settings;
import com.github.zhujianweistar.change.spring.profile.service.Notify;
import com.github.zhujianweistar.change.spring.profile.utils.Bundle;
import com.github.zhujianweistar.change.spring.profile.utils.SystemUtil;
import com.github.zhujianweistar.change.spring.profile.view.window.RunConfigurationBeanTreeCellRenderer;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ZhuJW
 * @version 1.0
 */
public class RunConfigurationTree extends JBScrollPane {

    private final Project project;

    /**
     * 树 - service列表
     */
    private final Tree tree;

    private final Map<String, RunConfigurationBeanNode> runConfigurationNodeMap;

    @Nullable
    private ChooseRequestCallback chooseRequestCallback;

    public RunConfigurationTree(@NotNull Project project) {
        this.project = project;
        this.runConfigurationNodeMap = new HashMap<>();
        tree = new SimpleTree();

        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setBorder(new CustomLineBorder(JBUI.insetsTop(1)));

        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.setRoot(new DefaultMutableTreeNode());
        tree.setCellRenderer(new RunConfigurationBeanTreeCellRenderer());
        tree.setRootVisible(true);
        tree.setShowsRootHandles(false);
        this.setViewportView(tree);

        initEvent();
    }

    /**
     * 渲染Restful请求列表
     */
    public void renderRequestTree(@NotNull Map<String, List<RunConfigurationBean>> allRequests) {
        AtomicInteger apiCount = new AtomicInteger();
        TreeNode<String> root = new TreeNode<>(Bundle.getString("service.tree.NotFoundAny"));

        runConfigurationNodeMap.clear();
        allRequests.forEach((itemName, requests) -> {
            if (requests == null || requests.isEmpty()) {
                return;
            }
            Map<String, List<RunConfigurationBean>> collect = new HashMap<>(1);
            if (Settings.SystemOptionForm.SHOW_CLASS_SERVICE_TREE.getData()) {
                collect = requests.stream().collect(Collectors.toMap(
                        RunConfigurationBean::getConfigurationName,
                        request -> new ArrayList<>(Collections.singletonList(request)),
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                ));
            } else {
                collect.put(null, requests);
            }

            ModuleNode moduleNode = new ModuleNode(new ModuleTree(itemName, requests.size()));
            collect.forEach((runConfigurationBeanName, items) -> {
                if (runConfigurationBeanName != null) {
                    TreeNode<String> node = new TreeNode<>(runConfigurationBeanName);
                    items.forEach(request -> {
                        RunConfigurationBeanNode runConfigurationBeanNode = new RunConfigurationBeanNode(request);
                        if (request.getPsiElement() instanceof PsiMethod) {
                            runConfigurationNodeMap.put(runConfigurationBeanName, runConfigurationBeanNode);
                        }
                        node.add(runConfigurationBeanNode);
                        apiCount.incrementAndGet();
                    });
                    moduleNode.add(node);
                } else {
                    items.forEach(request -> {
                        RunConfigurationBeanNode runConfigurationBeanNode = new RunConfigurationBeanNode(request);
                        if (request.getPsiElement() instanceof PsiMethod) {
                            runConfigurationNodeMap.put(null, runConfigurationBeanNode);
                        }
                        moduleNode.add(runConfigurationBeanNode);
                        apiCount.incrementAndGet();
                    });
                }
            });
            root.add(moduleNode);
        });

        ((DefaultTreeModel) tree.getModel()).setRoot(root);

        if (Settings.SystemOptionForm.EXPAND_OF_SERVICE_TREE.getData()) {
            expandAll(new TreePath(tree.getModel().getRoot()), true);
        }

        // api数量小于1才显示根节点
        tree.firePropertyChange(JTree.ROOT_VISIBLE_PROPERTY, tree.isRootVisible(), apiCount.get() < 1);
        // api数量小于1则不可点击
        tree.setEnabled(apiCount.get() > 0);
    }

    /**
     * 渲染Restful请求列表
     */
    public void renderServiceTree(@NotNull Map<String, List<RunConfigurationBean>> allRunConfiguration) {
        AtomicInteger apiCount = new AtomicInteger();
        TreeNode<String> root = new TreeNode<>(Bundle.getString("service.tree.NotFoundAny"));

        runConfigurationNodeMap.clear();
        allRunConfiguration.forEach((itemName, runConfigurationBeans) -> {
            if (runConfigurationBeans == null || runConfigurationBeans.isEmpty()) {
                return;
            }
            Map<String, List<RunConfigurationBean>> collect = new HashMap<>(1);
            if (Settings.SystemOptionForm.SHOW_CLASS_SERVICE_TREE.getData()) {
                collect = runConfigurationBeans.stream().collect(Collectors.toMap(
                        RunConfigurationBean::getConfigurationName,
                        runConfigurationBean -> new ArrayList<>(Collections.singletonList(runConfigurationBean)),
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                ));
            } else {
                collect.put(null, runConfigurationBeans);
            }

            ModuleNode moduleNode = new ModuleNode(new ModuleTree(itemName, runConfigurationBeans.size()));
            collect.forEach((runConfigurationBeanName, items) -> {
                if (runConfigurationBeanName != null) {
                    TreeNode<String> node = new TreeNode<>(runConfigurationBeanName);
                    items.forEach(request -> {
                        RunConfigurationBeanNode runConfigurationBeanNode = new RunConfigurationBeanNode(request);
                        if (request.getPsiElement() instanceof PsiMethod) {
                            runConfigurationNodeMap.put(runConfigurationBeanName, runConfigurationBeanNode);
                        }
                        node.add(runConfigurationBeanNode);
                        apiCount.incrementAndGet();
                    });
                    moduleNode.add(node);
                } else {
                    items.forEach(request -> {
                        RunConfigurationBeanNode runConfigurationBeanNode = new RunConfigurationBeanNode(request);
                        if (request.getPsiElement() instanceof PsiMethod) {
                            runConfigurationNodeMap.put(null, runConfigurationBeanNode);
                        }
                        moduleNode.add(runConfigurationBeanNode);
                        apiCount.incrementAndGet();
                    });
                }
            });
            root.add(moduleNode);
        });

        ((DefaultTreeModel) tree.getModel()).setRoot(root);

        if (Settings.SystemOptionForm.EXPAND_OF_SERVICE_TREE.getData()) {
            expandAll(new TreePath(tree.getModel().getRoot()), true);
        }

        // api数量小于1才显示根节点
        tree.firePropertyChange(JTree.ROOT_VISIBLE_PROPERTY, tree.isRootVisible(), apiCount.get() < 1);
        // api数量小于1则不可点击
        tree.setEnabled(apiCount.get() > 0);
    }

    public void setChooseRequestCallback(@Nullable ChooseRequestCallback chooseRequestCallback) {
        this.chooseRequestCallback = chooseRequestCallback;
    }

    private void initEvent() {
        // RequestTree子项点击监听
        tree.addTreeSelectionListener(e -> {
            RunConfigurationBean nodeRunConfigurationBean = getTreeNodeRunConfigurationBean(tree);
            if (chooseRequestCallback == null) {
                return;
            }
            if (nodeRunConfigurationBean == null) {
                chooseRequestCallback.choose(null);
                return;
            }
            chooseRequestCallback.choose(nodeRunConfigurationBean);
        });

        // RequestTree子项双击监听
        tree.addMouseListener(new MouseAdapter() {

            private JPopupMenu modulePopupMenu;
            private JPopupMenu classPopupMenu;
            private JPopupMenu requestItemPopupMenu;

            @Override
            public void mouseClicked(MouseEvent e) {
                final int doubleClick = 2;
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 0 && e.getClickCount() % doubleClick == 0) {
                    RunConfigurationBean node = getTreeNodeRunConfigurationBean(tree);
                    if (node != null && e.getClickCount() == doubleClick) {
                        node.navigate(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    RunConfigurationBean request = getTreeNodeRunConfigurationBean(tree);
                    if (request != null) {
                        showPopupMenu(e, getRequestItemPopupMenu());
                        return;
                    }

                    ClassTree classTree = getTreeNodeClassTree(tree);
                    if (classTree != null) {
                        showPopupMenu(e, getClassPopupMenu());
                    }

                    ModuleTree moduleTree = getTreeNodeModuleTree(tree);
                    if (moduleTree != null) {
                        showPopupMenu(e, getModulePopupMenu());
                    }
                }
            }

            private JPopupMenu getClassPopupMenu() {
                if (classPopupMenu != null) {
                    return classPopupMenu;
                }
                // navigation
                JMenuItem navigation = new JBMenuItem(Bundle.getString("action.NavigateToClass.text"), AllIcons.Nodes.Class);
                navigation.addActionListener(actionEvent -> {
                    ClassTree classTree = getTreeNodeClassTree(tree);
                    if (classTree == null) {
                        return;
                    }
                    classTree.getPsiClass().navigate(true);
                });
                return (classPopupMenu = generatePopupMenu(navigation));
            }

            private JPopupMenu getModulePopupMenu() {
                ModuleTree moduleTree = getTreeNodeModuleTree(tree);
                if (moduleTree == null) {
                    return null;
                }
                if (modulePopupMenu != null) {
                    return modulePopupMenu;
                }
                JBMenuItem moduleSetting = new JBMenuItem(Bundle.getString("action.OpenModuleSetting.text"), AllIcons.General.Settings);
                moduleSetting.addActionListener(action -> {
                    Module module = ModuleManager.getInstance(project).findModuleByName(moduleTree.getModuleName());
                    if (module == null) {
                        return;
                    }
                    // 打开当前项目模块设置
                    ProjectSettingsService.getInstance(project).openModuleSettings(module);
                });
                return (modulePopupMenu = generatePopupMenu(moduleSetting));
            }

            private JPopupMenu getRequestItemPopupMenu() {
                if (requestItemPopupMenu != null) {
                    return requestItemPopupMenu;
                }

                // navigation
                JMenuItem navigation = new JBMenuItem(Bundle.getString("action.NavigateToMethod.text"), AllIcons.Nodes.Method);
                navigation.addActionListener(actionEvent -> {
                    RunConfigurationBean nodeRunConfigurationBean = getTreeNodeRunConfigurationBean(tree);
                    if (nodeRunConfigurationBean == null) {
                        return;
                    }
                    nodeRunConfigurationBean.navigate(true);
                });

                // Copy full url
                JMenuItem copyFullUrl = new JBMenuItem(Bundle.getString("action.CopyFullPath.text"), AllIcons.Actions.Copy);
                copyFullUrl.addActionListener(actionEvent -> {
                    RunConfigurationBean treeNodeRunConfigurationBean = getTreeNodeRunConfigurationBean(tree);
                    if (treeNodeRunConfigurationBean == null) {
                        return;
                    }
                    GlobalSearchScope scope = treeNodeRunConfigurationBean.getPsiElement().getResolveScope();
                    String contextPath = "";
                    SystemUtil.Clipboard.copy(SystemUtil.buildUrl(
                            "",
                            0,
                            contextPath,
                            treeNodeRunConfigurationBean.getConfigurationName()));
                    Notify.getInstance(project).info(Bundle.getString("action.CopyFullPath.success"));
                });

                // Copy api path
                JMenuItem copyApiPath = new JBMenuItem(Bundle.getString("action.CopyApi.text"), AllIcons.Actions.Copy);
                copyApiPath.addActionListener(actionEvent -> {
                    RunConfigurationBean runConfigurationBean = getTreeNodeRunConfigurationBean(tree);
                    if (runConfigurationBean == null) {
                        return;
                    }
                    GlobalSearchScope scope = runConfigurationBean.getPsiElement().getResolveScope();
                    String contextPath = "";
                    SystemUtil.Clipboard.copy(
                            (contextPath == null || "null".equals(contextPath) ? "" : contextPath) +
                                    runConfigurationBean.getConfigurationName()
                    );
                    Notify.getInstance(project).info(Bundle.getString("action.CopyApi.success"));
                });
                return (requestItemPopupMenu = generatePopupMenu(
                        navigation,
                        null,
                        copyFullUrl, copyApiPath
                ));
            }
        });

        // 按回车键跳转到对应方法
        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    RunConfigurationBean runConfigurationBean = getTreeNodeRunConfigurationBean(tree);
                    if (runConfigurationBean != null) {
                        runConfigurationBean.navigate(true);
                    }
                }
            }
        });
    }

    @Nullable
    private RunConfigurationBean getTreeNodeRunConfigurationBean(@NotNull JTree tree) {
        DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (mutableTreeNode == null) {
            return null;
        }
        Object userObject = mutableTreeNode.getUserObject();
        if (userObject instanceof RunConfigurationBean) {
            return (RunConfigurationBean) userObject;
        }
        return null;
    }

    @Nullable
    private ModuleTree getTreeNodeModuleTree(@NotNull JTree tree) {
        DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (mutableTreeNode == null) {
            return null;
        }
        Object userObject = mutableTreeNode.getUserObject();
        if (!(userObject instanceof ModuleTree)) {
            return null;
        }
        return (ModuleTree) userObject;
    }

    @Nullable
    private ClassTree getTreeNodeClassTree(@NotNull JTree tree) {
        DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (mutableTreeNode == null) {
            return null;
        }
        Object userObject = mutableTreeNode.getUserObject();
        if (!(userObject instanceof ClassTree)) {
            return null;
        }
        return (ClassTree) userObject;
    }

    /**
     * 展开tree视图
     *
     * @param parent treePath
     * @param expand 是否展开
     */
    private void expandAll(@NotNull TreePath parent, boolean expand) {
        javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                javax.swing.tree.TreeNode n = (javax.swing.tree.TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }

        // 展开或收起必须自下而上进行
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    @NotNull
    private JPopupMenu generatePopupMenu(JComponent... items) {
        JBPopupMenu menu = new JBPopupMenu();
        if (items == null) {
            return menu;
        }
        for (int i = 0; i < items.length; i++) {
            JComponent item = items[i];
            if (item != null) {
                if (item instanceof JMenuItem) {
                    ((JMenuItem) item).setMnemonic(i);
                }
                menu.add(item);
            } else {
                menu.addSeparator();
            }
        }
        return menu;
    }

    /**
     * 显示右键菜单
     */
    private void showPopupMenu(@NotNull MouseEvent event, @Nullable JPopupMenu menu) {
        if (menu == null) {
            return;
        }
        TreePath path = tree.getPathForLocation(event.getX(), event.getY());
        tree.setSelectionPath(path);
        Rectangle rectangle = tree.getUI().getPathBounds(tree, path);
        if (rectangle != null && rectangle.contains(event.getX(), event.getY())) {
            menu.show(tree, event.getX(), rectangle.y + rectangle.height);
        }
    }

    /**
     * 转到tree
     */
    public void navigationToTree(@NotNull PsiMethod psiMethod) {
        RunConfigurationBeanNode runConfigurationBeanNode = runConfigurationNodeMap.get(psiMethod);
        if (runConfigurationBeanNode == null) {
            return;
        }
        //有节点到根路径数组
        javax.swing.tree.TreeNode[] nodes = ((DefaultTreeModel) tree.getModel()).getPathToRoot(runConfigurationBeanNode);
        TreePath path = new TreePath(nodes);
        tree.setSelectionPath(path);
    }

    interface ChooseRequestCallback {

        /**
         * 选择的Request项
         *
         * @param runConfigurationBean runConfigurationBean
         */
        void choose(@Nullable RunConfigurationBean runConfigurationBean);
    }

    public static class TreeNode<T> extends DefaultMutableTreeNode {

        private final T data;

        public TreeNode(@NotNull T data) {
            super(data);
            this.data = data;
        }

        @NotNull
        public T getData() {
            return data;
        }
    }

    public static class ModuleNode extends TreeNode<ModuleTree> {

        public ModuleNode(@NotNull ModuleTree data) {
            super(data);
        }
    }

    public static class ControllerNode extends TreeNode<ClassTree> {

        public ControllerNode(@NotNull ClassTree data) {
            super(data);
        }
    }

    public static class RunConfigurationBeanNode extends TreeNode<RunConfigurationBean> {

        public RunConfigurationBeanNode(@NotNull RunConfigurationBean data) {
            super(data);
        }
    }
}