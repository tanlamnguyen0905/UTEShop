package ute.service.inter;

import java.util.List;
import java.util.Map;

public interface ProductService {
    
    // Product counts
    Long getTotalProductCount();
    Long getActiveProductCount();
    
    // Top selling products by quantity or revenue
    List<Map<String, Object>> getTopProductsByQuantity(int limit);
    List<Map<String, Object>> getTopProductsByRevenue(int limit);
    
    // Sales by category
    List<Map<String, Object>> getSalesByCategory();
}
