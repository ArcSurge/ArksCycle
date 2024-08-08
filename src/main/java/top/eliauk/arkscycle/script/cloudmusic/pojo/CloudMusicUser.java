package top.eliauk.arkscycle.script.cloudmusic.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudMusicUser extends CloudMusicConfig.UserAccount {
    private String id;
    private Integer level;
    private Integer nowPlayCount;
    private Integer isAuthenticated = 0;
}
