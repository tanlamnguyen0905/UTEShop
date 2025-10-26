# Hướng dẫn Hệ thống Shipper - UTEShop

## Tổng quan
Hệ thống shipper cho phép người giao hàng:
- Xem danh sách đơn hàng đã được xác nhận và chọn đơn để giao
- Quản lý các đơn giao hàng của mình
- Cập nhật trạng thái giao hàng
- Xem lịch sử và thống kê giao hàng

## Cấu trúc hệ thống

### 1. Database Layer (DAO)
**DeliveryDao & DeliveryDaoImpl**
- `insert(Delivery)` - Tạo delivery mới
- `update(Delivery)` - Cập nhật delivery
- `findById(Long)` - Tìm delivery theo ID
- `findByShipperId(Long)` - Lấy tất cả delivery của shipper
- `findByShipperIdAndStatus(Long, String)` - Lọc delivery theo trạng thái
- `findByOrderId(Long)` - Tìm delivery theo orderId
- `countByShipperId(Long)` - Đếm số delivery của shipper
- `countByShipperIdAndStatus(Long, String)` - Đếm theo trạng thái

**OrderDao updates**
- `findConfirmedOrders()` - Lấy các đơn hàng "Đã xác nhận" chưa có shipper
- `countConfirmedOrders()` - Đếm số đơn hàng đã xác nhận

### 2. Business Logic Layer (Service)
**DeliveryService & DeliveryServiceImpl**
- `acceptOrder(Long orderId, Long shipperId)` - Shipper nhận đơn hàng
- `updateDeliveryStatus(Long deliveryId, String status, String notes)` - Cập nhật trạng thái
- `markAsCompleted(Long deliveryId)` - Đánh dấu đã giao thành công
- `markAsFailed(Long deliveryId, String reason)` - Đánh dấu thất bại
- `getShipperStats(Long shipperId)` - Lấy thống kê shipper

### 3. Controller
**ShipperController** - Single controller xử lý tất cả chức năng shipper

URL Patterns:
- `/api/shipper/feed` (GET) - Hiển thị danh sách đơn hàng "Đã xác nhận" có thể nhận
- `/api/shipper/my-deliveries` (GET) - Hiển thị các đơn giao hàng của shipper
  - Hỗ trợ lọc theo trạng thái: `?status=Đang giao hàng`
  - Hiển thị thống kê tổng quan
- `/api/shipper/delivery-detail?id=xxx` (GET) - Hiển thị chi tiết đơn giao hàng
  - Timeline lịch sử giao hàng
  - Thông tin khách hàng và sản phẩm chi tiết
- `/api/shipper/delivery-action` (POST) - Xử lý các actions:
  - `action=accept&orderId=xxx` - Nhận đơn hàng
  - `action=complete&deliveryId=xxx` - Hoàn thành giao hàng
  - `action=fail&deliveryId=xxx&failureReason=xxx` - Báo thất bại

**Methods:**
- `doGet()` - Route requests đến các handler tương ứng
- `doPost()` - Xử lý delivery actions
- `handleFeed()` - Xử lý trang feed đơn hàng
- `handleMyDeliveries()` - Xử lý trang đơn giao hàng
- `handleDeliveryDetail()` - Xử lý chi tiết delivery
- `handleDeliveryAction()` - Xử lý POST actions

### 4. Views (JSP)
**feed.jsp** - Trang feed đơn hàng
- Hiển thị grid các đơn hàng có thể nhận
- Thông tin khách hàng, địa chỉ, sản phẩm
- Nút "Nhận đơn hàng"

**my-deliveries.jsp** - Trang quản lý đơn giao hàng
- Thống kê: Tổng đơn, Đang giao, Đã hoàn thành, Thất bại
- Filter tabs theo trạng thái
- Cards hiển thị từng đơn giao hàng
- Actions: Xem chi tiết, Hoàn thành, Báo thất bại

**delivery-detail.jsp** - Chi tiết đơn giao hàng
- Thông tin khách hàng đầy đủ (có link gọi điện)
- Bảng sản phẩm chi tiết với hình ảnh
- Tổng kết đơn hàng
- Timeline lịch sử
- Actions cập nhật trạng thái

**shipper.jsp** - Decorator layout
- Sidebar navigation
- Header với thông báo và user menu
- Responsive design

## Luồng hoạt động

### 1. Shipper nhận đơn hàng
```
1. Shipper truy cập /api/shipper/feed
2. Xem danh sách đơn hàng "Đã xác nhận"
3. Click "Nhận đơn hàng"
4. System tạo Delivery mới với status "Đang giao hàng"
5. Cập nhật Order status thành "Đang giao hàng"
6. Redirect đến /api/shipper/my-deliveries
```

### 2. Cập nhật trạng thái giao hàng
```
1. Shipper vào /api/shipper/my-deliveries
2. Click "Hoàn thành" hoặc "Báo thất bại"
3. Modal xác nhận hiển thị
4. Submit form đến /api/shipper/delivery-action
5. System cập nhật:
   - Delivery status
   - Order status
   - CompletedDate (nếu thành công)
   - FailureReason (nếu thất bại)
   - PaymentStatus = "Đã thanh toán" (nếu COD thành công)
```

### 3. Xem chi tiết và lịch sử
```
1. Click "Xem chi tiết" trên delivery card
2. Hiển thị đầy đủ thông tin:
   - Thông tin khách hàng (có thể gọi điện)
   - Địa chỉ giao hàng chi tiết
   - Danh sách sản phẩm với hình ảnh
   - Tổng kết đơn hàng
   - Timeline lịch sử từ tạo đơn đến hiện tại
```

## Các trạng thái

### Order Status
- `Đang chờ` - Mới tạo, chưa xác nhận
- `Đã xác nhận` - Shop xác nhận, sẵn sàng cho shipper nhận
- `Đang giao hàng` - Shipper đã nhận và đang giao
- `Đã giao hàng` - Giao thành công
- `Giao hàng thất bại` - Giao thất bại
- `Đã hủy` - Đơn bị hủy

### Delivery Status
- `Đang giao hàng` - Default khi shipper nhận đơn
- `Đã giao hàng` - Giao thành công
- `Giao hàng thất bại` - Giao thất bại với lý do

## Phân quyền
- Role `shipper` required cho tất cả endpoints
- Shipper chỉ có thể xem và thao tác với deliveries của mình
- Kiểm tra ownership trong DeliveryDetailController

## UI/UX Features
- **Responsive Design** - Hoạt động tốt trên mobile và desktop
- **Gradient Colors** - Modern gradient purple theme
- **Hover Effects** - Cards có animation khi hover
- **Modal Confirmations** - Xác nhận trước khi thực hiện actions quan trọng
- **Real-time Stats** - Thống kê cập nhật theo deliveries
- **Status Badges** - Color-coded badges cho các trạng thái
- **Timeline View** - Visual timeline cho lịch sử giao hàng

## Navigation
```
Sidebar Menu:
├── Đơn hàng có sẵn (/api/shipper/feed)
├── Đơn của tôi (/api/shipper/my-deliveries)
├── Đang giao (/api/shipper/my-deliveries?status=Đang giao hàng)
├── Đã hoàn thành (/api/shipper/my-deliveries?status=Đã giao hàng)
├── Thất bại (/api/shipper/my-deliveries?status=Giao hàng thất bại)
├── Hồ sơ (/user/profile)
└── Trang chủ (/home)
```

## Cách sử dụng

### Đăng nhập với role Shipper
Đảm bảo user có `role = "shipper"` trong database.

### Truy cập hệ thống
URL chính: `http://localhost:8080/UTESHOP/api/shipper/feed`

### Nhận và giao đơn hàng
1. Vào "Đơn hàng có sẵn"
2. Chọn đơn phù hợp dựa trên địa chỉ
3. Click "Nhận đơn hàng"
4. Đơn hàng sẽ xuất hiện trong "Đơn của tôi"
5. Sau khi giao xong, cập nhật trạng thái

### Theo dõi thống kê
Dashboard "Đơn của tôi" hiển thị:
- Tổng số đơn đã nhận
- Số đơn đang giao
- Số đơn hoàn thành
- Số đơn thất bại

## Technical Notes

### SiteMesh Configuration
Decorator cho shipper được cấu hình trong `sitemesh3.xml`:
```xml
<mapping path="/api/shipper/*" decorator="/WEB-INF/decorators/shipper.jsp" />
```

### Session Management
CurrentUser được lưu trong session và kiểm tra ở mỗi controller.

### Error Handling
- Try-catch blocks trong services
- User-friendly error messages
- Redirect về page phù hợp khi có lỗi
- Session attributes cho success/error messages

## Future Enhancements
- [ ] Real-time notifications khi có đơn mới
- [ ] Map integration cho navigation
- [ ] QR code scanning để confirm giao hàng
- [ ] Rating system cho shipper
- [ ] Earnings calculator
- [ ] Export delivery reports

