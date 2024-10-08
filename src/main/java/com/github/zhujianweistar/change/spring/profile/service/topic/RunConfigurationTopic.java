package com.github.zhujianweistar.change.spring.profile.service.topic;

/**
 * @author ZhuJW
 * @version 1.0
 */
public interface RunConfigurationTopic<T> {

    /**
     * action
     *
     * @param data data
     */
    default void action(T data) {
        // default
    }
}
