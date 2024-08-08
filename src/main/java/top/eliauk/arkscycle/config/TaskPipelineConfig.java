package top.eliauk.arkscycle.config;

import cn.hutool.core.map.MapUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.eliauk.arkscycle.common.pipline.ProcessBusiness;
import top.eliauk.arkscycle.common.pipline.ProcessController;
import top.eliauk.arkscycle.common.pipline.ProcessTemplate;
import top.eliauk.arkscycle.common.utils.SpringContextUtil;
import top.eliauk.arkscycle.task.policy.AbstractBusinessPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Responsibility chain configuration</p>
 */
@Configuration
public class TaskPipelineConfig {
    private final Map<String, List<ProcessBusiness>> policyHolder = MapUtil.newHashMap(5);

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        Map<String, AbstractBusinessPolicy> beans = SpringContextUtil.getBeansByClass(AbstractBusinessPolicy.class);
        beans.values().forEach(policy -> putPolicy(policyHolder, policy.businessCode, policy.index, policy));
    }

    /**
     * 代理装配
     *
     * @param policyHolder {@link Map }<{@link String }, {@link List }<{@link ProcessBusiness }>>
     * @param key          {@link String }
     * @param index        {@link Integer }
     * @param policy       {@link ProcessBusiness }
     */
    private void putPolicy(Map<String, List<ProcessBusiness>> policyHolder, String key, Integer index, ProcessBusiness policy) {
        List<ProcessBusiness> list = policyHolder.getOrDefault(key, new ArrayList<>());
        if (index == null) {
            list.add(policy);
        } else {
            list.add(index, policy);
        }
        policyHolder.put(key, list);
    }

    /**
     * 获取模板Map
     *
     * @return {@link Map }<{@link String }, {@link ProcessTemplate }>
     */
    private Map<String, ProcessTemplate> getTemplateMap() {
        Map<String, ProcessTemplate> templateMap = new HashMap<>(policyHolder.size());
        policyHolder.forEach((key, value) -> {
            ProcessTemplate template = new ProcessTemplate();
            template.setProcessList(value);
            templateMap.put(key, template);
        });
        return templateMap;
    }

    /**
     * @return {@link ProcessController }
     **/
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        processController.setTemplateMap(getTemplateMap());
        return processController;
    }
}
