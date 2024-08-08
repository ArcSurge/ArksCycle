package top.eliauk.arkscycle.script.cloudmusic.context;

import top.eliauk.arkscycle.script.cloudmusic.pojo.CloudMusicUser;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public class UserContext {
    private static final ThreadLocal<CloudMusicUser> LOCAL = new InheritableThreadLocal<>();

    private UserContext() {
        throw new UnsupportedOperationException("This is a static class and cannot be instantiated.");
    }

    public static void setUserContext(CloudMusicUser user) {
        LOCAL.set(user);
    }

    public static CloudMusicUser currentUser() {
        return LOCAL.get();
    }

    public static void close() {
        LOCAL.remove();
    }
}
