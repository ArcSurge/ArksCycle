package top.eliauk.arkscycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import top.eliauk.arkscycle.task.ExecuteTasks;

/**
 * @author ArcSurge
 * @since 2024/08/06
 * <p>This java file was created by ArcSurge in 2024/08/06.
 * The following is the description information about this file:</p>
 * <p>description: Java-based scheduled task program.</p>
 */
@SpringBootApplication
public class ArksCycleApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ArksCycleApplication.class, args);
        applicationContext.getBean(ExecuteTasks.class).timer();
    }
}