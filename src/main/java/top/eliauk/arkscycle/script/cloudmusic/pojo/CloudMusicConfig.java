package top.eliauk.arkscycle.script.cloudmusic.pojo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.eliauk.arkscycle.common.utils.GlobalUtil;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: NetEase cloud related configuration.</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud-music")
public class CloudMusicConfig {
    private boolean enabled = false;
    private UserAccount account;
    private Scheduled scheduled;

    @Value("${task.scheduled.cron}")
    private String taskCron;

    @Data
    public static class UserAccount {
        private String name;
        private String cookie;
    }

    @Data
    private static class Scheduled {
        private String cron;
    }

    public String getCron() {
        if (this.getScheduled() == null) {
            return StrUtil.EMPTY;
        }
        String cron = this.getScheduled().getCron();
        if (StrUtil.isBlankIfStr(cron) || !GlobalUtil.isValidCron(cron)) {
            if (!StrUtil.isBlankIfStr(taskCron) && GlobalUtil.isValidCron(taskCron)) {
                return taskCron;
            }
            return StrUtil.EMPTY;
        }
        return cron;
    }
}
