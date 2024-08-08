package top.eliauk.arkscycle.script.cloudmusic;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import top.eliauk.arkscycle.common.enums.ChannelType;
import top.eliauk.arkscycle.common.pipline.ProcessContext;
import top.eliauk.arkscycle.common.pipline.ProcessController;
import top.eliauk.arkscycle.common.pipline.ProcessModel;
import top.eliauk.arkscycle.script.cloudmusic.context.UserContext;
import top.eliauk.arkscycle.script.cloudmusic.pojo.CloudMusicConfig;
import top.eliauk.arkscycle.task.Task;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "cloud-music", name = "enabled", havingValue = "true")
public class CloudMusicTask implements Task {

    @Autowired
    private ProcessController processController;

    @Autowired
    private CloudMusicConfig config;

    private static boolean status = false;

    /**
     * 执行
     */
    @Override
    public void execute() {
        if (status) {
            log.info("云音乐正在运行...");
            return;
        }
        log.info("云音乐任务执行开始.");
        try {
            status = true;
            ProcessContext<ProcessModel> process = processController.process(ProcessContext.builder()
                    .code(ChannelType.CLOUD_MUSIC.getCode())
                    .build());
            log.info(JSONUtil.toJsonStr(process));
        } finally {
            UserContext.close();
            status = false;
        }
        log.info("云音乐任务执行结束.");
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getCron() {
        if (config == null) {
            return StrUtil.EMPTY;
        }
        return config.getCron();
    }
}
