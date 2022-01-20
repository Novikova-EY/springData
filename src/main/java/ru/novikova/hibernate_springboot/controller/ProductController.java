package ru.novikova.hibernate_springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.novikova.hibernate_springboot.persist.Product;
import ru.novikova.hibernate_springboot.persist.ProductRepository;
import ru.novikova.hibernate_springboot.persist.ProductSpecification;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listPage(Model model,
                           @RequestParam("nameFilter") Optional<String> nameFilter,
                           @RequestParam("minPrice") Optional<BigDecimal> minPrice,
                           @RequestParam("maxPrice") Optional<BigDecimal> maxPrice) {
        logger.info("Product filter with name pattern {}", nameFilter.orElse(null));
        logger.info("Product filter with minPrice {}", minPrice.orElse(null));
        logger.info("Product filter with maxPrice {}", maxPrice.orElse(null));

        // Version 1
//        List<Product> products;
//        if (nameFilter.isPresent() && !nameFilter.get().isBlank()) {
//            products = productRepository.findAllByNameLike(nameFilter);
//        } else if (minPrice.isPresent() && maxPrice.isEmpty()) {
//            products = productRepository.findAllByPriceGreaterThanEqual(minPrice);
//        } else if (maxPrice.isPresent() && minPrice.isEmpty()) {
//            products = productRepository.findAllByPriceLessThanEqual(maxPrice);
//        } else if (maxPrice.isPresent() && minPrice.isPresent()) {
//            products = productRepository.findAllByPriceIsGreaterThanEqualAndPriceIsLessThanEqual(minPrice, maxPrice);
//        } else {
//            products = productRepository.findAll();
//        }
//        model.addAttribute("products", products);

        // Version 2
//        if (nameFilter.isPresent() || minPrice.isPresent() || maxPrice.isPresent()) {
//            model.addAttribute("products",
//                    productRepository.findByFilter(nameFilter, minPrice, maxPrice));
//        } else {
//            model.addAttribute("products", productRepository.findAll());
//        }

        // Version 3
        Specification<Product> spec = Specification.where(null);
        if (nameFilter.isPresent() && !nameFilter.get().isBlank()) {
            spec.and(ProductSpecification.nameLike(nameFilter.get()));
        } else if (!(minPrice == null) && maxPrice.isEmpty()) {
            spec.and(ProductSpecification.minPriceFilter(minPrice.get()));
        } else if (maxPrice.isPresent() && minPrice.isEmpty()) {
            spec.and(ProductSpecification.maxPriceFilter(maxPrice.get()));
        } else if (maxPrice.isPresent() && minPrice.isPresent()) {
            spec.and(ProductSpecification.minPriceFilter(minPrice.get()));
            spec.and(ProductSpecification.maxPriceFilter(maxPrice.get()));
        }
        model.addAttribute("products", productRepository.findAll(spec));
        return "product";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found")));
        return "product_form";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping
    public String save(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "product_form";
        }
        productRepository.save(product);
        return "redirect:/product";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/product";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(NotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "not_found";
    }
}

