package me.noteme.headhunting.domain.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.cache.CacheKey;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberCacheRepository {
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    public void saveConfirmKey(String confirmKey, String email){
        String key = CacheKey.confirmKey(confirmKey);
        valueOps.set(key, email, Duration.ofMinutes(5));
    }
}
