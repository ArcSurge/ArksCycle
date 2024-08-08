package top.eliauk.arkscycle.common.constant;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public class ThreadPoolConstant {
    /**
     * small
     */
    public static final Integer SMALL_CORE_POOL_SIZE = 1;
    public static final Integer SMALL_MAX_POOL_SIZE = 1;
    public static final Integer SMALL_KEEP_LIVE_TIME = 10;
    public static final Integer SMALL_QUEUE_SIZE = 64;
    /**
     * medium
     */
    public static final Integer COMMON_CORE_POOL_SIZE = 2;
    public static final Integer COMMON_MAX_POOL_SIZE = 2;
    public static final Integer COMMON_KEEP_LIVE_TIME = 60;
    public static final Integer COMMON_QUEUE_SIZE = 128;
    /**
     * big
     */
    public static final Integer BIG_QUEUE_SIZE = 1024;

    private ThreadPoolConstant() {
        throw new IllegalStateException("Utility class");
    }
}
