package top.eliauk.arkscycle.script.cloudmusic.tasks;

import cn.hutool.core.bean.BeanUtil;
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
import top.eliauk.arkscycle.script.cloudmusic.pojo.CloudMusicConfig;
import top.eliauk.arkscycle.script.cloudmusic.pojo.CloudMusicUser;
import top.eliauk.arkscycle.task.policy.AbstractBusinessPolicy;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: Verify login</p>
 */
@Slf4j
@Service
@ConditionalOnBean(CloudMusicTask.class)
public class CloudMusicVerifyTask extends AbstractBusinessPolicy {

    @Autowired
    private CloudMusicConfig config;

    @Autowired
    private CloudMusicApi mapi;

    /**
     * 云音乐验证任务
     */
    public CloudMusicVerifyTask() {
        index = 0;
        businessCode = ChannelType.CLOUD_MUSIC.getCode();
    }

    /**
     * 过程
     *
     * @param context 上下文
     */
    @Override
    public void process(ProcessContext<? extends ProcessModel> context) {
        log.info("云音乐授权验证开始.");
        log.info("用户数据: {}", JSONUtil.toJsonStr(config));
        context.setNeedBreak(true);
        if (!config.isEnabled()) {
            log.info("云音乐任务未开启.");
            return;
        }
        CloudMusicConfig.UserAccount account = config.getAccount();
        String resultStr = mapi.userLevel(account.getCookie());
        log.debug("获取用户等级数据: {}", resultStr);
        if (StrUtil.isBlankIfStr(resultStr)) {
            log.info("获取用户[{}]等级数据错误请检查cookie.", account.getName());
            return;
        }
        JSONObject resultObj = JSONUtil.parseObj(resultStr);
        if (!Integer.valueOf(200).equals(resultObj.getInt("code"))) {
            log.info("获取用户[{}]等级数据错误请检查cookie.", account.getName());
            return;
            // TODO 通知
        }
        JSONObject data = resultObj.getJSONObject("data");
        if (data == null) {
            log.error("获取用户等级失败！data数据为空.");
            return;
        }
        CloudMusicUser musicUser = BeanUtil.copyProperties(account, CloudMusicUser.class);
        musicUser.setLevel(data.getInt("level"));
        musicUser.setNowPlayCount(data.getInt("nowPlayCount"));
        musicUser.setIsAuthenticated(1);
        UserContext.setUserContext(musicUser);
        log.info("云音乐授权验证结束.");
        context.setNeedBreak(false);
    }
}
