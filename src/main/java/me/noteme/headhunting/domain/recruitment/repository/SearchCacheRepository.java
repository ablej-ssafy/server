package me.noteme.headhunting.domain.recruitment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SearchCacheRepository {
    public static final String SEARCH_KEYWORD = "searchKeywords";
    public static final String SEARCH_USER_KEYWORD = "searchKeywords:user:";

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    public void addKeyword(Long userId, String keyword) {
        if (userId != null) {
            zSetOperations.incrementScore(SEARCH_USER_KEYWORD + userId, keyword, 1);
        }
        zSetOperations.incrementScore(SEARCH_KEYWORD, keyword, 1);

    }

    public Set<String> getTopKeywords() {
        return zSetOperations.reverseRange("search-keyword", 0, 9);
    }

    public Set<String> getKeywords(Long userId) {
        return zSetOperations.reverseRange(SEARCH_USER_KEYWORD + userId, 0, 4);
    }
}
