package me.noteme.headhunting.domain.recruitment.repository;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.cache.CacheKey;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SearchCacheRepository {
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    public void addKeyword(Long userId, String keyword) {
        if (userId != null) {
            zSetOperations.incrementScore(CacheKey.searchUserKey(userId), keyword, 1);
        }
        zSetOperations.incrementScore(CacheKey.searchKey(), keyword, 1);

    }

    public Set<String> getTopKeywords() {
        return zSetOperations.reverseRange("search-keyword", 0, 9);
    }

    public Set<String> getKeywords(Long userId) {
        return zSetOperations.reverseRange(CacheKey.searchUserKey(userId), 0, 4);
    }

    public void removeKeyword(Long userId, String keyword) {
        if (userId != null) {
            zSetOperations.remove(CacheKey.searchUserKey(userId), keyword);
        }
        zSetOperations.remove(CacheKey.searchKey(), keyword);
    }
}
