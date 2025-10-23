package ute.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.entities.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPage {
    private List<Product> products;
    private int total; // total items
    private int totalPages;
    private int currentPage;
    private int pageSize;
}