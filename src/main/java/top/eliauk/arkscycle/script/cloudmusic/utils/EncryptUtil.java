package top.eliauk.arkscycle.script.cloudmusic.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ArcSurge
 * @since 2024/08/08
 * <p>This java file was created by ArcSurge in 2024/08/08.
 * The following is the description information about this file:</p>
 * <p>description: </p>
 */
public class EncryptUtil {
    private static final String MODULUS = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" + "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" + "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" + "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" + "3ece0462db0a22b8e7";

    private static final String NONCE = "0CoJUm6Qyw8W8jud";
    private static final String PUB_KEY = "010001";

    private static final String PARAMS = "params";
    private static final String ENC_SEC_KEY = "encSecKey";

    private EncryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> encrypt(String text) {
        String secKey = RandomStringUtils.random(16, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        String encText = aesEncrypt(aesEncrypt(text, NONCE), secKey);
        String encSecKey = rsaEncrypt(StrUtil.reverse(secKey));
        Map<String, Object> map = new HashMap<>();
        map.put(PARAMS, encText);
        map.put(ENC_SEC_KEY, encSecKey);
        return map;
    }

    private static String aesEncrypt(String text, String key) {
        return Base64.encode(new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(StandardCharsets.UTF_8), "0102030405060708".getBytes(StandardCharsets.UTF_8)).encrypt(text));
    }

    private static String rsaEncrypt(String text) {
        BigInteger rs = new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16).modPow(new BigInteger(EncryptUtil.PUB_KEY, 16), new BigInteger(EncryptUtil.MODULUS, 16));
        return StrUtil.fillBefore(rs.toString(16), '0', 256);
    }
}
