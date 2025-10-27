package ute.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ute.dao.impl.ProductDaoImpl;
import ute.dto.ProductDTO;
import ute.entities.Product;
import ute.utils.ProductFilter;
import ute.utils.ProductPage;
import ute.dao.impl.OrderDetailDaoImpl;
import ute.dao.inter.OrderDetailDao;
import ute.dao.inter.ProductDao;
import ute.service.inter.ProductService;

public class ProductServiceImpl implements ProductService {
    
    private final ProductDao productDao;
    private final OrderDetailDao orderDetailDao;
    
    public ProductServiceImpl() {
        this.productDao = new ProductDaoImpl();
        this.orderDetailDao = new OrderDetailDaoImpl();
    }

	@Override
	public void insert(Product product) {
		productDao.insert(product);
	}

	@Override
	public void update(Product product) {
		productDao.update(product);
	}

	@Override
	public void delete(Long id) {
		productDao.delete(id.intValue());
	}

	@Override
	public Product findById(Long id) {
		return productDao.findById(id.intValue());
	}

	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public List<Product> findByName(String name) {
		return productDao.findByName(name);
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		return productDao.findByCategoryId(categoryId);
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		return productDao.findBestSeller(limit);
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		return productDao.findByPriceRange(minPrice, maxPrice);
	}

	@Override
	public long count() {
		return productDao.count();
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		return productDao.findPage(page, pageSize);
	}

	@Override
	public List<Product> findNewProduct(int limit) {
		return productDao.findNewProduct(limit);
	}

	@Override
	public List<Product> findTopReview(int limit) {
		return productDao.findTopReview(limit);
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		return productDao.findTopFavorite(limit);
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		return productDao.findByCategoryIdinPage(categoryId, page, pageSize);
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		return productDao.findProductsByFilter(filter, page, pageSize);
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		return productDao.findTopFavoriteinPage(page, pageSize);
	}

	@Override
	public int countProductsByFilter(ProductFilter filter) {
		return productDao.countProductsByFilter(filter);
	}

	@Override
	public ProductPage getProductsPageByFilter(ProductFilter filter) {
		int page = filter.getCurrentPage() != null ? filter.getCurrentPage() : 1;
		int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 20;
		List<Product> products = productDao.findProductsByFilter(filter, page, pageSize);
		int total = productDao.countProductsByFilter(filter);
		int totalPages = (int) Math.ceil((double) total / pageSize);
		return ProductPage.builder()
				.products(products)
				.total(total)
				.totalPages(totalPages)
				.currentPage(page)
				.pageSize(pageSize)
				.build();
	}

	@Override
	public List<ProductDTO> MapToProductDTO(List<Product> products) {
		if (products == null) {
			return null;
		}
		return products.stream()
				.filter(Objects::nonNull)
				.map(ProductDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
    public void importStock(Long productId, int quantity) {
        Product product = productDao.findById(productId.intValue());
        if (product != null) {
            product.setStockQuantity(product.getStockQuantity() + quantity);
            productDao.update(product);
        }
    }

    @Override
    public void exportStock(Long productId, int quantity) {
        Product product = productDao.findById(productId.intValue());
        if (product != null && product.getStockQuantity() >= quantity) {
            product.setStockQuantity(product.getStockQuantity() - quantity);
            product.setSoldCount(product.getSoldCount() + quantity);
            productDao.update(product);
        } else {
            throw new RuntimeException("Tồn kho không đủ hoặc sản phẩm không tồn tại");
        }
    }

    @Override
    public List<Product> getInventoryStats(ProductFilter filter, int page, int pageSize) {
        // Không override keyword cứng nữa, để filter từ Controller quyết định
        // Nếu muốn default low stock: if (filter.getKeyword() == null || filter.getKeyword().isEmpty()) filter.setKeyword("stockQuantity < 10");
        return productDao.findProductsByFilter(filter, page, pageSize);
    }

    @Override
    public void exportInventoryReport(HttpServletResponse response, ProductFilter filter) {
        List<Product> products = productDao.findProductsByFilter(filter, 1, Integer.MAX_VALUE);
        // Use Apache POI to generate Excel (similar to admin export)
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tồn kho");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Tên");
            header.createCell(2).setCellValue("Tồn kho");
            header.createCell(3).setCellValue("Đã bán");
            header.createCell(4).setCellValue("Giá");
            header.createCell(5).setCellValue("Category");

            int rowNum = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getProductID());
                row.createCell(1).setCellValue(p.getProductName());
                row.createCell(2).setCellValue(p.getStockQuantity());
                row.createCell(3).setCellValue(p.getSoldCount());
                row.createCell(4).setCellValue(p.getUnitPrice());
                row.createCell(5).setCellValue(p.getCategory().getCategoryName());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=inventory_report.xlsx");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xuất báo cáo: " + e.getMessage());
        }
    }

    @Override
    public Long getTotalProductCount() {
        return productDao.count();
    }

    @Override
    public Long getActiveProductCount() {
        return productDao.getActiveProductCount();
    }

    @Override
    public List<Map<String, Object>> getTopProductsByQuantity(int limit) {
        return orderDetailDao.getTopProductsByQuantity(limit);
    }

    @Override
    public List<Map<String, Object>> getTopProductsByRevenue(int limit) {
        return orderDetailDao.getTopProductsByRevenue(limit);
    }

    @Override
    public List<Map<String, Object>> getSalesByCategory() {
        return orderDetailDao.getSalesByCategory();
    }
}