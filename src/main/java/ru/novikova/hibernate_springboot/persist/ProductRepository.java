package ru.novikova.hibernate_springboot.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // Version 1
//    List<Product> findAllByNameLike(Optional<String> pattern);
//    List<Product> findAllByPriceGreaterThanEqual(Optional<BigDecimal> minPrice);
//    List<Product> findAllByPriceLessThanEqual(Optional<BigDecimal> maxPrice);
//    List<Product> findAllByPriceIsGreaterThanEqualAndPriceIsLessThanEqual(Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice);

    // Version 2
//    @Query("select p " +
//            " from Product p " +
//            " where (p.name like :pattern or :pattern is null) and " +
//            " (p.price >= :minPrice or :minPrice is null) and " +
//            " (p.price <= :maxPrice or :maxPrice is null) ")
//    List<Product> findByFilter(@Param("pattern") Optional<String> pattern,
//                               @Param("minPrice") Optional<BigDecimal> minPrice,
//                               @Param("maxPrice") Optional<BigDecimal> maxPrice);


}
