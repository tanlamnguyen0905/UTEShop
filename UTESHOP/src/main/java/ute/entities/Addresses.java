package ute.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String province; // Tỉnh / Thành phố
    @Column(columnDefinition = "NVARCHAR(255)")
    private String district; // Quận / Huyện
    @Column(columnDefinition = "NVARCHAR(255)")
    private String ward; // Phường / Xã

    @Column(columnDefinition = "NVARCHAR(255)")
    private String addressDetail;

    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;
}
