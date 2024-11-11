package me.noteme.headhunting.domain.recruitment.repository;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.cache.CacheKey;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SearchCacheRepository {
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    public void addKeyword(Long userId, String keyword) {
        if (userId != null) {
            Long size = listOperations.size(CacheKey.searchUserKey(userId));
            if (Objects.nonNull(size) && size >= 5) {
                listOperations.rightPop(CacheKey.searchUserKey(userId));
            }
            listOperations.leftPush(CacheKey.searchUserKey(userId), keyword);
        }
        zSetOperations.incrementScore(CacheKey.searchKey(), keyword, 1);

    }

    public Set<String> getTopKeywords() {
        return zSetOperations.reverseRange(CacheKey.searchKey(), 0, 9);
    }

    public Set<String> getKeywords(Long userId) {
        return zSetOperations.reverseRange(CacheKey.searchUserKey(userId), 0, 4);
    }

    public void removeKeyword(Long userId, String keyword) {
        if (userId != null) {
            listOperations.remove(CacheKey.searchUserKey(userId), 0, keyword);
        }
        zSetOperations.remove(CacheKey.searchKey(), keyword);
    }
}
