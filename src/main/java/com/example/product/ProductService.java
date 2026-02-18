package com.example.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductEntityConverter entityConverter;

    public ProductService(
            ProductRepository productRepository,
            ProductEntityConverter entityConverter
    ) {
        this.productRepository = productRepository;
        this.entityConverter = entityConverter;
    }

    public Page<ProductDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(entityConverter::toDomain);
    }

    public ProductDto getOne(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID `%s` not found".formatted(id)));

        return entityConverter.toDomain(product);
    }

    public List<ProductDto> getMany(List<Long> ids) {

        return productRepository.findAllById(ids)
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public ProductDto create(ProductDto dto) {
        Product request = entityConverter.toEntity(dto);
        Product save = productRepository.save(request);

        return entityConverter.toDomain(save);
    }

    public ProductDto patch(Long id, ProductDto dto) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product with ID `%s` not found".formatted(id)
            );
        }

        if (id.equals(dto.id())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product with ID `%s` not equal to path ID `%s`."
                            .formatted(dto.id(), id)
            );
        }
        Product product = entityConverter.toEntity(dto);

        return entityConverter.toDomain(productRepository.save(product));
    }

    public List<Long> patchMany(List<Long> ids, List<ProductDto> patchNode) {
        Map<Long, Product> products = new HashMap<>();
        patchNode.forEach(patch -> {
            long id = patch.id();
            if (ids.contains(id)) {
                Product entity = entityConverter.toEntity(patch);
                products.put(id, entity);
            }
        });
        var values = products.values();
        if (values.isEmpty()) {
            return List.of();
        }
        productRepository.saveAll(values);

        return products.keySet().stream().toList();
    }

    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product with ID `%s` not found".formatted(id)
            );
        }
        productRepository.deleteById(id);
    }

    public void deleteMany(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }
}
