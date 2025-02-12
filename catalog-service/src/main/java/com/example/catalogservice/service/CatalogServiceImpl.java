package com.example.catalogservice.service;

import com.example.catalogservice.dto.CatalogResponse;
import com.example.catalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public Page<CatalogResponse.List> getAllCatalogList(int page, int size) {
        return catalogRepository.findAll(PageRequest.of(page - 1, size)).map(CatalogResponse.List::new);
    }
}
