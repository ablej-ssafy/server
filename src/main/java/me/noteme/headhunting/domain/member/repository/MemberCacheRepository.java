package me.noteme.headhunting.domain.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.cache.CacheKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberCacheRepository {
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @Value("${jwt.refresh-expire}")
    private Long refreshExpire;

    public void saveConfirmKey(String confirmKey, String email) {
        String key = CacheKey.confirmKey(confirmKey);
        valueOps.set(key, email, Duration.ofMinutes(5));
    }

    public String findEmailByConfirmKey(String confirmKey) {
        return valueOps.get(CacheKey.confirmKey(confirmKey));
    }

    public void saveAuthenticationKey(Long memberId, String refreshToken) {
        String key = CacheKey.authenticationKey(memberId.toString());
        valueOps.set(key, refreshToken, Duration.ofMillis(refreshExpire));
    }

    public Optional<String> findAuthenticationKey(Long memberId) {
        return Optional.ofNullable(
                valueOps.get(CacheKey.authenticationKey(memberId.toString()))
        );
    }

    public void deleteAuthenticationKey(Long memberId) {
        valueOps.getOperations().delete(
                CacheKey.authenticationKey(memberId.toString())
        );
    }
}
