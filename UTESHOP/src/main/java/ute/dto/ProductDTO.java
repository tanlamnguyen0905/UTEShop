package ute.dto;

import java.util.Optional;

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
    private String image;
    public int favoriteCount;

    public static ProductDTO fromEntity(Product p) {
        if (p == null)
            return null;
        ProductDTO dto = new ProductDTO();
        dto.setProductID(p.getProductID());
        dto.setProductName(p.getProductName());
        dto.setUnitPrice(p.getUnitPrice());
        // discount price: không còn discount => mặc định bằng unitPrice

        // choose first image as thumbnail if exists
        String img = null;
        if (p.getImages() != null && !p.getImages().isEmpty()) {
            Optional<Image> first = p.getImages().stream().filter(i -> i != null && i.getDirImage() != null)
                    .findFirst();
            if (first.isPresent())
                img = first.get().getDirImage();
        }
        dto.setImage(img);

        // favorite count
        try {
            dto.favoriteCount = p.getFavoriteCount();
        } catch (Exception e) {
            dto.favoriteCount = p.getFavorites() != null ? p.getFavorites().size() : 0;
        }

        return dto;
    }
}
