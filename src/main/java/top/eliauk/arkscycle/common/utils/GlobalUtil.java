package top.eliauk.arkscycle.common.utils;

import org.springframework.scheduling.support.CronExpression;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Global tool class</p>
 */
public class GlobalUtil {
    private GlobalUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidCron(String cron) {
        return CronExpression.isValidExpression(cron);
    }
}
