package com.example.catalogservice.service;

import com.example.catalogservice.dto.CatalogResponse;
import org.springframework.data.domain.Page;

public interface CatalogService {

    Page<CatalogResponse.List> getAllCatalogList(int page, int size);
}
