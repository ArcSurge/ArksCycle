package top.eliauk.arkscycle.task;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public interface Task {
    void execute();

    String getName();

    String getCron();
}
