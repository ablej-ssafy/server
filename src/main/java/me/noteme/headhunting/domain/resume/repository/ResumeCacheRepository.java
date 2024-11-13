package me.noteme.headhunting.domain.resume.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.cache.CacheKey;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ResumeCacheRepository {
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    public void save(Long memberId, String data) {
        String key = CacheKey.resumeKey(memberId);
        valueOps.set(key, data, Duration.ofMinutes(5));
    }

    // 데이터를 가져오는 메서드
    public String getData(Long memberId) {
        String key = CacheKey.resumeKey(memberId);
        if(exists(key)){
            return valueOps.get(key);
        }
        throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 데이터입니다.");
    }

    private boolean exists(String key) {
        return valueOps.getOperations().hasKey(key);
    }
}
