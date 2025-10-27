package ute.utils;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStats {
    private LocalDate date;  // Cho daily
    private Double revenue;
    private Long orderCount;  // Cho total
    private Double avgRevenue;  // Cho total

    // Constructor cho daily (chỉ date + revenue)
    public RevenueStats(LocalDate date, Double revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    // Constructor cho total (tổng hợp)
    public RevenueStats(Double totalRevenue, Long orderCount, Double avgRevenue) {
        this.revenue = totalRevenue;
        this.orderCount = orderCount;
        this.avgRevenue = avgRevenue;
    }
}