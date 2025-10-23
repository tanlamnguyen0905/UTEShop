package ute.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ute.entities.Image;
import ute.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productID;
    private String productName;
    private Double unitPrice;
    private Integer stockQuantity;
    public int favoriteCount;
    private String brand;
    private String category;
    private String description;
    private float rating;
    public List<String> images;

    public static ProductDTO fromEntity(Product p) {
        if (p == null)
            return null;
        ProductDTO dto = new ProductDTO();
        dto.setProductID(p.getProductID());
        dto.setProductName(p.getProductName());
        dto.setUnitPrice(p.getUnitPrice());
        dto.setStockQuantity(p.getStockQuantity());
        dto.setDescription(p.getDescribe());
        dto.setCategory(p.getCategory() != null ? p.getCategory().getCategoryName() : "N/A");
        dto.setBrand(p.getBrand() != null ? p.getBrand().getBrandName() : "N/A");
        dto.setRating(p.getRating());
        // discount price: không còn discount => mặc định bằng unitPrice

        // Convert all product images to URLs
        List<String> imageUrls = new ArrayList<>();
        if (p.getImages() != null) {
            imageUrls = p.getImages().stream()
                    .filter(i -> i != null && i.getDirImage() != null)
                    .map(Image::getDirImage)
                    .collect(Collectors.toList());
        }
        dto.setImages(imageUrls);

        // favorite count
        try {
            dto.favoriteCount = p.getFavoriteCount();
        } catch (Exception e) {
            dto.favoriteCount = p.getFavorites() != null ? p.getFavorites().size() : 0;
        }

        return dto;
    }
}
