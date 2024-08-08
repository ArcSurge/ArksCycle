package top.eliauk.arkscycle.script.cloudmusic.tasks;

import cn.hutool.core.collection.ListUtil;
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
import top.eliauk.arkscycle.script.cloudmusic.pojo.SongsDetail;
import top.eliauk.arkscycle.task.policy.AbstractBusinessPolicy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
public class ListenSongsTask extends AbstractBusinessPolicy {

    @Autowired
    private CloudMusicApi mapi;

    private static final Integer MAX_LEVEL = 10;

    public ListenSongsTask() {
        businessCode = ChannelType.CLOUD_MUSIC.getCode();
    }

    /**
     * 听歌过程
     *
     * @param context 上下文
     */
    @Override
    public void process(ProcessContext<? extends ProcessModel> context) {
        log.info("听歌任务执行开始.");
        CloudMusicUser account = UserContext.currentUser();
        log.debug("用户数据: {}", JSONUtil.toJsonStr(account));
        context.setNeedBreak(true);
        if (!listenSongsList(account)) {
            return;
        }
        log.info("听歌任务执行结束.");
        context.setNeedBreak(false);
    }

    /**
     * 每天听歌
     *
     * @param songsMap 歌曲地图
     * @param account  账号
     */
    private void listenSongsDaily(Map<Long, List<SongsDetail>> songsMap, CloudMusicUser account) {
        int count = 0;
        int nextCount = 300;
        final int maxCount = 300;
        Integer nowPlayCount = account.getNowPlayCount();
        int upperLimit = nowPlayCount + nextCount;
        for (Map.Entry<Long, List<SongsDetail>> entry : songsMap.entrySet()) {
            Long sourceId = entry.getKey();
            List<SongsDetail> songsList = entry.getValue();
            for (SongsDetail songs : songsList) {
                try {
                    String listenStr = mapi.listenSong(account.getCookie(), songs.getId(), sourceId);
                    if (StrUtil.isBlankIfStr(listenStr)) {
                        continue;
                    }
                    JSONObject listenSongs = JSONUtil.parseObj(listenStr);
                    if (!Integer.valueOf(200).equals(listenSongs.getInt("code"))) {
                        continue;
                    }
                    count++;
                    log.info("{}: 第{}首: ====>> 歌单id:{} ===>> 歌曲: {} ===>> 目标: {}", account.getName(), count, sourceId, songs.getId(), upperLimit);
                    Thread.sleep(500);
                    if (count >= nextCount && count % 50 == 0) {
                        int t = JSONUtil.parseObj(mapi.userLevel(account.getCookie())).getJSONObject("data").getInt("nowPlayCount");
                        if (t >= upperLimit) {
                            log.info("已完成每日 {} 首听歌当前等级听歌量为：{}", maxCount, t);
                            return;
                        }
                        log.info("当前听歌量为: {} 首, 未达到目标: {} 首", t, upperLimit);
                        nextCount = count + upperLimit - t;
                    }
                } catch (Exception e) {
                    log.error("听歌过程中出现错误: {}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 听歌列表
     *
     * @param account 账号
     * @return boolean
     */
    private boolean listenSongsList(CloudMusicUser account) {
        if (!Integer.valueOf(1).equals(account.getIsAuthenticated())) {
            return false;
        }
        if (account.getLevel() == null || account.getLevel() >= MAX_LEVEL) {
            return true;
        }
        // 获取推荐歌单
        String resultStr = mapi.getRecommendSongs(account.getCookie());
        JSONObject resultObj = JSONUtil.parseObj(resultStr);
        if (!Integer.valueOf(200).equals(resultObj.getInt("code"))) {
            return false;
        }
        List<JSONObject> recommends = resultObj.getBeanList("recommend", JSONObject.class);
        Set<Long> recommendIds = recommends.stream().map(recommend -> recommend.getLong("id")).collect(Collectors.toSet());
        Map<Long, List<SongsDetail>> songsMap = parsePlayList(recommendIds, account);
        listenSongsDaily(songsMap, account);
        return true;
    }

    /**
     * 解析播放列表
     *
     * @param recommendIds 推荐 ID
     * @param account      账号
     * @return {@link Map }<{@link Long }, {@link List }<{@link SongsDetail }>>
     */
    private Map<Long, List<SongsDetail>> parsePlayList(Set<Long> recommendIds, CloudMusicUser account) {
        log.debug("推荐歌单数量: {}", recommendIds.size());
        return recommendIds.stream().collect(Collectors.toMap(id -> id, id -> {
            String resultStr = mapi.playListDetail(id, account.getCookie());
            if (StrUtil.isBlankIfStr(resultStr)) {
                return ListUtil.empty();
            }
            JSONObject resultObj = JSONUtil.parseObj(resultStr);
            JSONObject playlist = resultObj.getJSONObject("playlist");
            if (playlist == null) {
                return ListUtil.empty();
            }
            return playlist.getBeanList("trackIds", SongsDetail.class);
        }));
    }
}
