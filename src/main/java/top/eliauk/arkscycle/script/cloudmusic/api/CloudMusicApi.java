package top.eliauk.arkscycle.script.cloudmusic.api;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.eliauk.arkscycle.script.cloudmusic.utils.EncryptUtil;

import java.util.Map;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
@Slf4j
@Component
public class CloudMusicApi {

    private static final String ORIGIN = "https://music.163.com";
    private static final String REFERER = "https://music.163.com/";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36";

    /**
     * 获取用户等级信息
     *
     * @param cookie String
     * @return {@link String}
     */
    public String userLevel(String cookie) {
        String url = "https://music.163.com/weapi/user/level";
        JSONObject param = new JSONObject();
        return api(JSONUtil.toJsonStr(param), url, MapUtil.builder("crypto", "weapi").put("Cookie", cookie).build());
    }

    /**
     * 签到
     *
     * @param cookie String
     * @return {@link String }
     */
    public String sign(String cookie) {
        String url = "https://music.163.com/weapi/pointmall/user/sign";
        JSONObject param = new JSONObject();
        return api(JSONUtil.toJsonStr(param), url, MapUtil.builder("crypto", "weapi").put("Cookie", cookie).build());
    }

    /**
     * 获取每日推荐歌单
     *
     * @param cookie String
     * @return {@link JSONObject}
     */
    public String getRecommendSongs(String cookie) {
        String url = "https://music.163.com/api/v1/discovery/recommend/resource";
        JSONObject param = new JSONObject();
        return api(JSONUtil.toJsonStr(param), url, MapUtil.builder("crypto", "weapi").put("Cookie", cookie).build());
    }

    /**
     * 获取歌单详情
     *
     * @param id     long
     * @param cookie String
     * @return {@link String}
     */
    public String playListDetail(long id, String cookie) {
        String url = "https://music.163.com/weapi/v6/playlist/detail";
        JSONObject param = new JSONObject();
        param.set("id", id);
        param.set("n", 100000);
        param.set("s", 8);
        return api(JSONUtil.toJsonStr(param), url, MapUtil.builder("crypto", "weapi").put("Cookie", cookie).build());
    }

    /**
     * 听歌
     *
     * @param cookie     String
     * @param songId     Long
     * @param playListId Long
     * @return {@link String}
     */
    public String listenSong(String cookie, Long songId, Long playListId) {
        String url = "https://music.163.com/weapi/feedback/weblog";
        JSONObject param = new JSONObject();
        param.set("logs", "[{\"action\":\"play\",\"json\":{\"download\":0,\"end\":\"playend\",\"id\":" + songId + ",\"sourceId\":" + playListId + ",\"time\":" + "240" + ",\"type\":\"song\",\"wifi\":0}}]");
        return api(JSONUtil.toJsonStr(param), url, MapUtil.builder("crypto", "weapi").put("Cookie", cookie).build());
    }

    /**
     * 封装的网易云接口
     *
     * @param param  参数
     * @param url    请求地址
     * @param header 请求头
     * @return 服务器响应体
     */
    private String api(String param, String url, Map<String, String> header) {
        if (param == null || param.isEmpty() || url == null || url.isEmpty()) {
            log.error("参数或URL不能为空");
            return StrUtil.EMPTY;
        }

        try {
            // 使用 HTTPS 以保证通信安全
            String secureUrl = ensureHttps(url);

            // 构造请求
            try (HttpResponse response = HttpRequest.post(secureUrl)
                    .headerMap(header, false)
                    .header("Referer", REFERER)
                    .header("Origin", ORIGIN)
                    .header("User-Agent", USER_AGENT)
                    .header("Content-Type", CONTENT_TYPE)
                    .form(EncryptUtil.encrypt(param))
                    .execute()) {
                return response.body();
            }
        } catch (Exception e) {
            log.error("请求处理过程中发生异常: ", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * 确保URL使用HTTPS协议
     *
     * @param url 原始URL
     * @return 使用HTTPS的URL
     */
    private String ensureHttps(String url) {
        if (!url.startsWith("https://")) {
            // 简单的替换方案，实际应用中可能需要更复杂的逻辑
            url = url.replaceFirst("^http://", "https://");
        }
        return url;
    }
}
