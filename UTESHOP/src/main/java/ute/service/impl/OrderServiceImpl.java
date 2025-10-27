package ute.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import ute.dao.impl.OrderDaoImpl;
import ute.dao.inter.OrderDao;
import ute.service.inter.OrderService;

public class OrderServiceImpl implements OrderService {
    
    private final OrderDao orderDao;
    
    public OrderServiceImpl() {
        this.orderDao = new OrderDaoImpl();
    }

    @Override
    public Double getTotalRevenue() {
        return orderDao.getTotalRevenue();
    }

    @Override
    public Double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return orderDao.getTotalRevenueByDateRange(startDateTime, endDateTime);
    }

    @Override
    public Long getTotalOrderCount() {
        return orderDao.getTotalOrderCount();
    }

    @Override
    public Long getOrderCountByStatus(String status) {
        return orderDao.getOrderCountByStatus(status);
    }

    @Override
    public List<Map<String, Object>> getOrderStatusDistribution() {
        return orderDao.getOrderStatusDistribution();
    }

    @Override
    public List<Map<String, Object>> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return orderDao.getDailyRevenue(startDateTime, endDateTime);
    }
}