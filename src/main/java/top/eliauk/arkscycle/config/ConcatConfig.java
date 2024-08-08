package top.eliauk.arkscycle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Concat config</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "concat")
public class ConcatConfig {
    /**
     * 作者
     */
    private String author;
    /**
     * 版本
     */
    private String version;
    /**
     * 创建时间
     */
    private String created;
    /**
     * GitHub主页
     */
    private String github;
    /**
     * 存储库
     */
    private String repository;
}
