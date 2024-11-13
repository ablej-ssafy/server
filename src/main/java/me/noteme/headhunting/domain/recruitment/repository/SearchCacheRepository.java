package me.noteme.headhunting.domain.recruitment.repository;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.cache.CacheKey;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SearchCacheRepository {
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

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

    public List<String> getKeywords(Long userId) {
        return listOperations.range(CacheKey.searchUserKey(userId), 0, 4);
    }

    public void removeKeyword(Long userId, String keyword) {
        if (userId != null) {
            listOperations.remove(CacheKey.searchUserKey(userId), 0, keyword);
        }
        zSetOperations.remove(CacheKey.searchKey(), keyword);
    }

    public void initSuggestions(List<String> keywords) {
        setOperations.add(CacheKey.autoCompleteKey(), keywords.toArray(new String[0]));
    }

    public List<String> getSuggestions(String keyword) {
        List<String> keywords = Objects.requireNonNull(setOperations.members(
                CacheKey.autoCompleteKey()
        )).stream().toList();
        return FuzzySearch.extractTop(
                        keyword,
                        keywords,
                        10,
                        60
                )
                .stream()
                .map(ExtractedResult::getString)
                .collect(Collectors.toList());
    }
}
