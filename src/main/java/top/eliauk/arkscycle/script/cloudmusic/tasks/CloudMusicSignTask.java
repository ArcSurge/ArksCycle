package top.eliauk.arkscycle.script.cloudmusic.tasks;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import top.eliauk.arkscycle.common.enums.ChannelType;
import top.eliauk.arkscycle.common.pipline.ProcessContext;
import top.eliauk.arkscycle.common.pipline.ProcessModel;
import top.eliauk.arkscycle.script.cloudmusic.CloudMusicTask;
import top.eliauk.arkscycle.script.cloudmusic.api.CloudMusicApi;
import top.eliauk.arkscycle.script.cloudmusic.context.UserContext;
import top.eliauk.arkscycle.script.cloudmusic.pojo.CloudMusicUser;
import top.eliauk.arkscycle.task.policy.AbstractBusinessPolicy;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Slf4j
@Service
@ConditionalOnBean(CloudMusicTask.class)
public class CloudMusicSignTask extends AbstractBusinessPolicy {

    @Autowired
    private CloudMusicApi mapi;

    /**
     * 云音乐签到任务
     */
    public CloudMusicSignTask() {
        businessCode = ChannelType.CLOUD_MUSIC.getCode();
    }

    /**
     * 签到过程
     *
     * @param context 上下文
     */
    @Override
    public void process(ProcessContext<? extends ProcessModel> context) {
        context.setNeedBreak(true);
        log.info("云音乐签到任务执行开始.");
        CloudMusicUser user = UserContext.currentUser();
        String resultStr = mapi.sign(user.getCookie());
        log.debug("云音乐签到结果: {}", resultStr);
        if (StrUtil.isBlankIfStr(resultStr)) {
            log.error("云音乐签到任务执行失败.");
            return;
        }

        JSONObject resultObj = JSONUtil.parseObj(resultStr);
        if (!Integer.valueOf(200).equals(resultObj.getInt("code"))) {
            log.error("云音乐签到任务执行失败！code: {}, message: {}", resultObj.getStr("code"), resultObj.getStr("message"));
            return;
        }

        JSONObject data = resultObj.getJSONObject("data");
        if (data == null) {
            log.info("云音乐签到失败，data数据为空.");
            return;
        }
        if (!data.getBool("sign", false)) {
            log.info("云音乐签到失败，签到状态为false.");
        }
        log.info("云音乐签到任务执行完毕.");
        context.setNeedBreak(false);
    }
}
