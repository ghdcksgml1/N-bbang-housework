package com.heachi.housework.api.service.housework.category;

import com.heachi.housework.api.service.housework.category.response.HouseworkCategoryResponse;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseworkCategoryService {

    private final HouseworkCategoryRepository houseworkCategoryRepository;

    public List<HouseworkCategoryResponse> selectCategory() {

        return houseworkCategoryRepository.findAll().stream()
                .map(HouseworkCategoryResponse::of)
                .collect(Collectors.toList());
    }
}
