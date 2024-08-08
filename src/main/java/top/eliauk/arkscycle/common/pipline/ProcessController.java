package top.eliauk.arkscycle.common.pipline;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: procedure controller</p>
 */
@Data
public class ProcessController {
    /**
     * 模板映射
     */
    private Map<String, ProcessTemplate> templateMap;

    /**
     * 执行责任链
     *
     * @param context 上下文
     * @return {@link ProcessContext}<{@link ?}>
     */
    public ProcessContext<ProcessModel> process(ProcessContext<ProcessModel> context) {
        if (!preCheck(context)) {
            return context;
        }
        // 遍历流程节点
        List<? extends ProcessBusiness> processList = templateMap.get(context.getCode()).getProcessList();
        for (ProcessBusiness business : processList) {
            business.process(context);
            if (Boolean.TRUE.equals(context.getNeedBreak())) {
                break;
            }
        }
        return context;
    }

    private boolean preCheck(ProcessContext<?> context) {
        // 上下文判断
        if (context == null) {
            return false;
        }

        // 业务代码判断
        String businessCode = context.getCode();
        if (StrUtil.isEmptyIfStr(businessCode)) {
            return false;
        }

        // 执行模板判断
        ProcessTemplate processTemplate = templateMap.get(businessCode);
        if (processTemplate == null) {
            return false;
        }

        // 执行模板列表判断
        List<ProcessBusiness> processList = processTemplate.getProcessList();
        return !CollUtil.isEmpty(processList);
    }
}
