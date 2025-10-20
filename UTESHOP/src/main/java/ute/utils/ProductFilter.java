package ute.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {

    private Long categoryId = null;
    private Long brandId = null;
    private Long bannerId = null;

    private Double minPrice = null;
    private Double maxPrice = null;
    private String keyword = null;
    private String sortBy = null; // "0": bán chạy, "1": mới nhất, "2": nhiều đánh giá, "3": yêu thích, "4": giá
                                  // tăng, "5": giá giảm
    private Integer currentPage = 1;
    private Integer pageSize = 20;

    // getters + setters

}
