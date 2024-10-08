package com.github.zhujianweistar.change.spring.profile.service.topic;

import com.github.zhujianweistar.change.spring.profile.beans.RunConfigurationBean;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface ServiceTreeTopic extends RunConfigurationTopic<Map<String, List<RunConfigurationBean>>> {

    Topic<ServiceTreeTopic> TOPIC = Topic.create("RestTopic.ServiceTreeTopic", ServiceTreeTopic.class);

    /**
     * action
     *
     * @param data data
     */
    @Override
    void action(@NotNull Map<String, List<RunConfigurationBean>> data);
}
