package top.eliauk.arkscycle.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Channel type</p>
 */
@Getter
@AllArgsConstructor
public enum ChannelType {
    CLOUD_MUSIC("CloudMusic", "网易云音乐");
    /**
     * 编码值
     */
    private final String code;

    /**
     * 描述
     */
    private final String description;
}
