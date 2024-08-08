package top.eliauk.arkscycle.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.eliauk.arkscycle.common.constant.ThreadPoolConstant;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public class ThreadPoolConfig {
    private static final String PRE_FIX = "ArcSurge.";

    private ThreadPoolConfig() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 构建执行器
     *
     * @param group 分组
     * @return {@link Executor }
     */
    public static Executor buildExecutor(String group) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(ThreadPoolConstant.SMALL_CORE_POOL_SIZE);
        // 配置最大线程数
        executor.setMaxPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE);
        // 配置队列大小
        executor.setQueueCapacity(ThreadPoolConstant.COMMON_QUEUE_SIZE);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(PRE_FIX + group);
        /*
         * 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 执行初始化
        executor.initialize();
        return executor;
    }
}
