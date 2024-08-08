package top.eliauk.arkscycle.common.pipline;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Business execution template, series chain of responsibility logic</p>
 */
@Getter
@Setter
public class ProcessTemplate {
    /**
     * 业务链
     */
    private List<ProcessBusiness> processList;
}
