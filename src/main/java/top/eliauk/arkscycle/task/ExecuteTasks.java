package top.eliauk.arkscycle.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Slf4j
@Component
public class ExecuteTasks {

    private final List<Task> tasks;

    public ExecuteTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void start() {
        log.info("任务执行开始.");
        tasks.forEach(Task::execute);
        log.info("任务执行结束.");
    }

    public void timer() {
        log.info("触发定时器.");
        start();
        log.info("等待下次触发定时器.");
    }
}
