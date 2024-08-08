package top.eliauk.arkscycle.common.pipline;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public interface ProcessBusiness {
    /**
     * 处理逻辑
     *
     * @param context 上下文
     */
    void process(ProcessContext<? extends ProcessModel> context);
}
