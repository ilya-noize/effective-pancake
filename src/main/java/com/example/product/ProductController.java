package com.example.product;

import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LogManager.getLogger(ProductController.class);
    private final ProductService productService;
    private final ProductDtoConverter dtoConverter;

    public ProductController(ProductService productService, ProductDtoConverter dtoConverter) {
        this.productService = productService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public PagedModel<ProductResponse> getAll(Pageable pageable) {
        log.debug("Getting all");
        Page<ProductResponse> productResponses = productService
                .getAll(pageable)
                .map(dtoConverter::toDomain);

        return new PagedModel<>(productResponses);
    }

    @GetMapping("/{id}")
    public ProductResponse getOne(@PathVariable Long id) {
        log.debug("Fetching one with ID:{} ", id);

        return dtoConverter.toDomain(productService.getOne(id));
    }

    @GetMapping("/by-ids")
    public List<ProductResponse> getMany(@RequestParam List<Long> ids) {
        log.debug("Fetch many");

        return productService.getMany(ids)
                .stream()
                .map(dtoConverter::toDomain)
                .toList();
    }

    @PostMapping
    public ProductResponse create(@RequestBody ProductPostRequest requestCreate) {
        log.debug("Creating a {}", requestCreate);
        ProductDto dto = dtoConverter.toEntity(requestCreate);
        ProductDto created = productService.create(dto);

        return dtoConverter.toDomain(created);
    }

    @PatchMapping("/{id}")
    public ProductResponse patch(
            @PathVariable
            @Positive Long id,
            @RequestBody ProductPatchRequest patchNode
    ) {
        log.debug("Patch product by Id:{}, node={}", id, patchNode);
        ProductDto dto = dtoConverter.toEntity(patchNode);
        ProductDto patched = productService.patch(id, dto);

        return dtoConverter.toDomain(patched);
    }

    @PatchMapping
    public List<Long> patchMany(
            @RequestParam List<Long> ids,
            @RequestBody List<ProductPatchRequest> patchNode
    ) {
        log.debug("Patch products. Ids {}. Nodes := [\n{}\n]", ids, patchNode);
        List<ProductDto> patchNodeDtos = patchNode.stream().map(dtoConverter::toEntity).toList();

        return productService.patchMany(ids, patchNodeDtos);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.debug("Deleting... ID:{} ", id);
        productService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        log.debug("Deleting Many.... IDs:{} ", ids);
        productService.deleteMany(ids);
    }
}
