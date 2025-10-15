package ute.entities;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    @Column( columnDefinition = "nvarchar(255)" )
    private String content;
    private String image;
    private Double rating;
    private LocalDateTime createAt;
    
    private int status;

    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;

    @OneToOne
    @JoinColumn(name = "orderDetailID")
    private OrderDetail orderDetail;
}
