package top.eliauk.arkscycle.config;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import top.eliauk.arkscycle.task.Task;

import java.util.List;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Slf4j
@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {

    private final List<Task> tasks;

    /**
     * 任务配置
     *
     * @param tasks 任务
     */
    public TaskConfig(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * 配置任务
     *
     * @param taskRegistrar 任务注册器
     */
    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        tasks.stream().filter(task -> null != task.getCron()).forEach(task -> {
            String cron = task.getCron();
            if (cron != null && !StrUtil.isBlankIfStr(cron)) {
                log.info("任务: {} cron表达式: {}", task.getName(), cron);
                taskRegistrar.addTriggerTask(() -> ThreadPoolConfig.buildExecutor(task.getName()).execute(task::execute), triggerContext -> {
                    try {
                        return new CronTrigger(cron).nextExecution(triggerContext);
                    } catch (Exception e) {
                        log.error("计算任务触发时间出错.", e);
                        // 在无法获取有效触发时间时，返回一个远期时间防止死循环
                        return DateTime.now().offset(DateField.SECOND, 1).toInstant();
                    }
                });
            }
        });
    }
}
