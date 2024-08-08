package top.eliauk.arkscycle.common.utils;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * 获取applicationContext对象
     */
    @Getter
    private static ApplicationContext applicationContext;

    /**
     * 设置应用程序上下文
     *
     * @param applicationContext 应用程序上下文
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        setContext(applicationContext);
    }

    /**
     * 设置上下文
     *
     * @param applicationContext 应用程序上下文
     */
    private static void setContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据bean的name来查找对象
     *
     * @param name String
     * @return Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param c Class<T>
     * @return T
     */
    public static <T> T getBean(Class<T> c) {
        return getApplicationContext().getBean(c);
    }

    /**
     * 根据bean的name和class来查找对象
     *
     * @param name String
     * @param c    Class<T>
     * @return T
     */
    public static <T> T getBean(String name, Class<T> c) {
        return getApplicationContext().getBean(name, c);
    }

    /**
     * 根据bean的class来查找所有的对象(包括子类)
     *
     * @param c Class<T>
     * @return Map<String, T>
     */
    public static <T> Map<String, T> getBeansByClass(Class<T> c) {
        return applicationContext.getBeansOfType(c);
    }
}
