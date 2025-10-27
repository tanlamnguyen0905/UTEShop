package ute.dto;

public class ProductWithAvgRatingDTO {
    private Long productID;
    private String productName;
    private Double avgRating;

    public ProductWithAvgRatingDTO(Long productID, String productName, Double avgRating) {
        this.productID = productID;
        this.productName = productName;
        this.avgRating = avgRating;
    }

    // Getters and setters
    public Long getProductID() { return productID; }
    public void setProductID(Long productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Double getAvgRating() { return avgRating; }
    public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }
}