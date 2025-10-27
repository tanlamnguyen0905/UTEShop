package ute.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ute.dto.AddressBasicDTO;
import ute.dto.DeliveryDTO;
import ute.dto.OrderBasicDTO;
import ute.dto.OrderDetailBasicDTO;
import ute.entities.Addresses;
import ute.entities.Delivery;
import ute.entities.OrderDetail;
import ute.entities.Orders;

public class DeliveryMapper {
    
    /**
     * Chuyển đổi Entity Delivery sang DTO
     */
    public static DeliveryDTO toDTO(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        
        DeliveryDTO dto = DeliveryDTO.builder()
                .deliveryID(delivery.getDeliveryID())
                .orderID(delivery.getOrder() != null ? delivery.getOrder().getOrderID() : null)
                .shipperID(delivery.getShipper() != null ? delivery.getShipper().getUserID() : null)
                .shipperName(delivery.getShipper() != null ? delivery.getShipper().getFullname() : null)
                .shipperPhone(delivery.getShipper() != null ? delivery.getShipper().getPhone() : null)
                .assignedDate(delivery.getAssignedDate())
                .completedDate(delivery.getCompletedDate())
                .deliveryStatus(delivery.getDeliveryStatus())
                .notes(delivery.getNotes())
                .failureReason(delivery.getFailureReason())
                .build();
        
        // Chuyển đổi thông tin order
        if (delivery.getOrder() != null) {
            dto.setOrder(toOrderBasicDTO(delivery.getOrder()));
        }
        
        return dto;
    }
    
    /**
     * Chuyển đổi danh sách Entity sang DTO
     */
    public static List<DeliveryDTO> toDTOList(List<Delivery> deliveries) {
        if (deliveries == null) {
            return new ArrayList<>();
        }
        return deliveries.stream()
                .map(DeliveryMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Chuyển đổi Entity Order sang OrderBasicDTO
     */
    private static OrderBasicDTO toOrderBasicDTO(Orders order) {
        if (order == null) {
            return null;
        }
        
        OrderBasicDTO dto = OrderBasicDTO.builder()
                .orderID(order.getOrderID())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .amount(order.getAmount())
                .shippingFee(order.getShippingFee())
                .totalDiscount(order.getTotalDiscount())
                .notes(order.getNotes())
                .recipientName(order.getRecipientName())
                .phoneNumber(order.getPhoneNumber())
                .paymentMethodName(order.getPaymentMethod() != null ? order.getPaymentMethod().getName() : null)
                .build();
        
        // Chuyển đổi địa chỉ
        if (order.getAddress() != null) {
            dto.setAddress(toAddressBasicDTO(order.getAddress()));
        }
        
        // Chuyển đổi chi tiết đơn hàng
        if (order.getOrderDetails() != null) {
            dto.setOrderDetails(toOrderDetailBasicDTOList(order.getOrderDetails()));
        }
        
        return dto;
    }
    
    /**
     * Chuyển đổi Entity Address sang AddressBasicDTO
     */
    private static AddressBasicDTO toAddressBasicDTO(Addresses address) {
        if (address == null) {
            return null;
        }
        
        return AddressBasicDTO.builder()
                .addressID(address.getAddressID())
                .name(address.getName())
                .phone(address.getPhone())
                .addressDetail(address.getAddressDetail())
                .ward(address.getWard())
                .district(address.getDistrict())
                .city(address.getProvince())
                .build();
    }
    
    /**
     * Chuyển đổi danh sách OrderDetail sang DTO
     */
    private static List<OrderDetailBasicDTO> toOrderDetailBasicDTOList(List<OrderDetail> orderDetails) {
        if (orderDetails == null) {
            return new ArrayList<>();
        }
        
        return orderDetails.stream()
                .map(DeliveryMapper::toOrderDetailBasicDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Chuyển đổi Entity OrderDetail sang DTO
     */
    private static OrderDetailBasicDTO toOrderDetailBasicDTO(OrderDetail detail) {
        if (detail == null) {
            return null;
        }
        
        String imageUrl = null;
        if (detail.getProduct() != null && 
            detail.getProduct().getImages() != null && 
            !detail.getProduct().getImages().isEmpty()) {
            imageUrl = detail.getProduct().getImages().get(0).getDirImage();
        }
        
        return OrderDetailBasicDTO.builder()
                .orderDetailID(detail.getOrderDetailID())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .productID(detail.getProduct() != null ? detail.getProduct().getProductID() : null)
                .productName(detail.getProduct() != null ? detail.getProduct().getProductName() : null)
                .categoryName(detail.getProduct() != null && detail.getProduct().getCategory() != null ? 
                             detail.getProduct().getCategory().getCategoryName() : null)
                .imageUrl(imageUrl)
                .build();
    }
}

