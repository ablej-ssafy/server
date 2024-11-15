package me.noteme.headhunting.domain.recruitment.repository;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.cache.CacheKey;
import me.noteme.headhunting.common.utils.HangulUtils;
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
    private SetOperations<String, String> setOperations;

    public void addKeyword(Long userId, String keyword) {
        if (userId != null) {
            String userSearchKey = CacheKey.searchUserKey(userId);
            Long size = setOperations.size(userSearchKey);
            if (size != null && size >= 5) {
                String oldestKeyword = setOperations.randomMember(userSearchKey);
                setOperations.remove(userSearchKey, oldestKeyword);
            }
            setOperations.add(userSearchKey, keyword);
        }

        if (!hasKeyword(keyword)) {
            return;
        }
        zSetOperations.incrementScore(CacheKey.searchKey(), keyword, 1);

    }

    public Set<String> getTopKeywords() {
        return zSetOperations.reverseRange(CacheKey.searchKey(), 0, 9);
    }

    public Set<String> getKeywords(Long userId) {
        return setOperations.members(CacheKey.searchUserKey(userId));
    }

    public void removeKeyword(Long userId, String keyword) {
        if (userId != null) {
            setOperations.remove(CacheKey.searchUserKey(userId), keyword);
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

    private boolean hasKeyword(String keyword) {
        return Objects.requireNonNull(setOperations.members(
                CacheKey.autoCompleteKey()
        )).contains(keyword);
    }

    public void removeAllKeyword(Long userId) {
        setOperations.getOperations().delete(CacheKey.searchUserKey(userId));
    }
}
