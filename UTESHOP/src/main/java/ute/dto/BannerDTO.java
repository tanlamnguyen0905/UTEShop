package ute.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ute.entities.Banner;
import ute.entities.Image;
import ute.entities.Product;
import ute.service.impl.ProductServiceImpl;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    Long bannerID;
    String bannerName;
    String bannerImage;
    String description;

    List<ProductDTO> products;

    public static BannerDTO fromEntity(Banner b) {
        if (b == null)
            return null;

        BannerDTO dto = new BannerDTO();
        dto.setBannerID(b.getBannerID());
        dto.setBannerName(b.getBannerName());
        dto.setBannerImage(b.getBannerImage());
        dto.setDescription(b.getDescription());

        // Map product nếu đã load
        List<ProductDTO> productDTOs = new ArrayList<>();
        try {
            if (b.getProducts() != null) {
                productDTOs = b.getProducts().stream()
                        .map(p -> {
                            ProductDTO pd = new ProductDTO();
                            pd.setProductID(p.getProductID());
                            pd.setProductName(p.getProductName());
                            List<String> imageNames = new ArrayList<>();
                            if (p.getImages() != null) {
                                imageNames = p.getImages().stream()
                                        .map(Image::getDirImage) // Lấy tên file ảnh
                                        .collect(Collectors.toList());
                            }
                            pd.setImages(imageNames);
                            return pd;
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Tránh crash nếu lazy-loading lỗi
            System.err.println("⚠️ Không thể load sản phẩm của banner: " + e.getMessage());
        }
        dto.setProducts(productDTOs);

        return dto;
    }

}
