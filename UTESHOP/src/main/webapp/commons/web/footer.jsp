<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- ====== FOOTER ====== -->
<footer class="footer-section">
    <!-- Main Footer -->
    <div class="footer-main">
        <div class="container">
            <div class="row g-4">
                
                <!-- About Us Column -->
                <div class="col-lg-4 col-md-6">
                    <div class="footer-widget">
                        <div class="footer-logo mb-4">
                            <h3 class="text-white fw-bold">
                                <i class="fas fa-store me-2"></i>UTESHOP
                            </h3>
                        </div>
                        <p class="footer-text mb-3">
                            Shop bán hàng đầu tại Võ Văn Ngân, cung cấp các sản phẩm chất lượng cao 
                            với giá cả hợp lý. Uy tín - Chất lượng - Tận tâm.
                        </p>
                        
                        <!-- Contact Info -->
                        <ul class="footer-contact list-unstyled">
                            <li class="mb-2">
                                <i class="fas fa-map-marker-alt me-2"></i>
                                <span>Võ Văn Ngân, TP.Thủ Đức, TP.HCM</span>
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-phone-alt me-2"></i>
                                <a href="tel:+84123456789">+84 915 677 079</a>
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-envelope me-2"></i>
                                <a href="mailto:uteshop@gmail.com">uteshop99@gmail.com</a>
                            </li>
                            <li>
                                <i class="fas fa-clock me-2"></i>
                                <span>T2 - CN: 8:00 - 22:00</span>
                            </li>
                        </ul>
                    </div>
                </div>
                
                <!-- Quick Links Column -->
                <div class="col-lg-2 col-md-6">
                    <div class="footer-widget">
                        <h5 class="footer-title">Liên Kết</h5>
                        <ul class="footer-links list-unstyled">
                            <li><a href="${pageContext.request.contextPath}/home"><i class="fas fa-angle-right me-2"></i>Trang chủ</a></li>
                            <li><a href="${pageContext.request.contextPath}/shop"><i class="fas fa-angle-right me-2"></i>Sản phẩm</a></li>
                            <li><a href="${pageContext.request.contextPath}/about"><i class="fas fa-angle-right me-2"></i>Về chúng tôi</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact"><i class="fas fa-angle-right me-2"></i>Liên hệ</a></li>
                            <li><a href="${pageContext.request.contextPath}/blog"><i class="fas fa-angle-right me-2"></i>Tin tức</a></li>
                        </ul>
                    </div>
                </div>
                
                <!-- Policies Column -->
                <div class="col-lg-3 col-md-6">
                    <div class="footer-widget">
                        <h5 class="footer-title">Chính Sách</h5>
                        <ul class="footer-links list-unstyled">
                            <li><a href="#"><i class="fas fa-angle-right me-2"></i>Chính sách bảo mật</a></li>
                            <li><a href="#"><i class="fas fa-angle-right me-2"></i>Chính sách đổi trả</a></li>
                            <li><a href="#"><i class="fas fa-angle-right me-2"></i>Chính sách vận chuyển</a></li>
                            <li><a href="#"><i class="fas fa-angle-right me-2"></i>Điều khoản sử dụng</a></li>
                            <li><a href="#"><i class="fas fa-angle-right me-2"></i>Hướng dẫn thanh toán</a></li>
                        </ul>
                    </div>
                </div>
                
                <!-- Newsletter Column -->
                <div class="col-lg-3 col-md-6">
                    <div class="footer-widget">
                        <h5 class="footer-title">Đăng Ký Nhận Tin</h5>
                        <p class="footer-text mb-3">
                            Đăng ký để nhận thông tin khuyến mãi và sản phẩm mới nhất
                        </p>
                        <form class="newsletter-form mb-4">
                            <div class="input-group">
                                <input type="email" class="form-control" placeholder="Email của bạn..." required>
                                <button class="btn btn-primary" type="submit">
                                    <i class="fas fa-paper-plane"></i>
                                </button>
                            </div>
                        </form>
                        
                        <!-- Social Media -->
                        <h6 class="text-white mb-3">Kết Nối Với Chúng Tôi</h6>
                        <div class="social-links">
                            <a href="#" class="social-link facebook" title="Facebook">
                                <i class="fab fa-facebook-f"></i>
                            </a>
                            <a href="#" class="social-link instagram" title="Instagram">
                                <i class="fab fa-instagram"></i>
                            </a>
                            <a href="#" class="social-link youtube" title="YouTube">
                                <i class="fab fa-youtube"></i>
                            </a>
                            <a href="#" class="social-link tiktok" title="TikTok">
                                <i class="fab fa-tiktok"></i>
                            </a>
                            <a href="#" class="social-link twitter" title="Twitter">
                                <i class="fab fa-twitter"></i>
                            </a>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
    
    <!-- Payment Methods -->
    <div class="footer-payment">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                    <h6 class="text-white mb-2">Phương Thức Thanh Toán</h6>
                    <div class="payment-methods">
                        <img src="https://img.icons8.com/color/48/cash-in-hand.png" alt="COD" title="Tiền mặt">
                    </div>
                </div>
                <div class="col-md-6 text-center text-md-end">
                    <h6 class="text-white mb-2">Vận Chuyển</h6>
                    <div class="payment-methods">
                        <img src="https://img.icons8.com/color/48/ghn.png" alt="GHN" title="Giao Hàng Nhanh">
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bottom Footer -->
    <div class="footer-bottom">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6 text-center text-md-start mb-2 mb-md-0">
                    <p class="mb-0">
                        &copy; 2025 <strong>UTESHOP</strong>. All rights reserved. | 
                        <span class="text-primary">Nhóm 15 - CNTT</span>
                    </p>
                </div>
                <div class="col-md-6 text-center text-md-end">
                    <p class="mb-0">
                        Phát triển bởi: 
                        <span class="team-member">Vũ Quốc Trung</span> • 
                        <span class="team-member">Nguyễn Lâm Tấn</span> • 
                        <span class="team-member">Nguyễn Kim Điền</span> • 
                        <span class="team-member">Trần Bá Thành</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Back to Top Button -->
    <button id="backToTop" class="back-to-top" title="Lên đầu trang">
        <i class="fas fa-chevron-up"></i>
    </button>
</footer>

<style>
    /* Footer Main Styles */
    .footer-section {
        background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
        color: #fff;
        position: relative;
        overflow: hidden;
    }
    
    .footer-section::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 1px;
        background: linear-gradient(90deg, transparent, #667eea, transparent);
    }
    
    .footer-main {
        padding: 60px 0 40px;
    }
    
    .footer-logo h3 {
        font-size: 28px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }
    
    .footer-text {
        color: rgba(255, 255, 255, 0.7);
        font-size: 14px;
        line-height: 1.8;
    }
    
    .footer-title {
        color: #fff;
        font-weight: 600;
        font-size: 18px;
        margin-bottom: 20px;
        position: relative;
        padding-bottom: 10px;
    }
    
    .footer-title::after {
        content: '';
        position: absolute;
        left: 0;
        bottom: 0;
        width: 50px;
        height: 2px;
        background: linear-gradient(90deg, #667eea, #764ba2);
    }
    
    /* Contact Info */
    .footer-contact li {
        color: rgba(255, 255, 255, 0.8);
        font-size: 14px;
        display: flex;
        align-items: start;
    }
    
    .footer-contact li i {
        color: #667eea;
        margin-top: 3px;
        flex-shrink: 0;
    }
    
    .footer-contact a {
        color: rgba(255, 255, 255, 0.8);
        text-decoration: none;
        transition: color 0.3s;
    }
    
    .footer-contact a:hover {
        color: #667eea;
    }
    
    /* Footer Links */
    .footer-links li {
        margin-bottom: 12px;
    }
    
    .footer-links a {
        color: rgba(255, 255, 255, 0.7);
        text-decoration: none;
        font-size: 14px;
        transition: all 0.3s;
        display: inline-block;
    }
    
    .footer-links a:hover {
        color: #667eea;
        padding-left: 5px;
    }
    
    .footer-links i {
        font-size: 10px;
    }
    
    /* Newsletter */
    .newsletter-form .form-control {
        background: rgba(255, 255, 255, 0.1);
        border: 1px solid rgba(255, 255, 255, 0.2);
        color: #fff;
        padding: 12px 15px;
        border-radius: 8px 0 0 8px;
    }
    
    .newsletter-form .form-control:focus {
        background: rgba(255, 255, 255, 0.15);
        border-color: #667eea;
        color: #fff;
        box-shadow: none;
    }
    
    .newsletter-form .form-control::placeholder {
        color: rgba(255, 255, 255, 0.5);
    }
    
    .newsletter-form .btn {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        padding: 12px 20px;
        border-radius: 0 8px 8px 0;
        transition: all 0.3s;
    }
    
    .newsletter-form .btn:hover {
        transform: scale(1.05);
        box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
    }
    
    /* Social Links */
    .social-links {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
    }
    
    .social-link {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.1);
        color: #fff;
        text-decoration: none;
        transition: all 0.3s;
        font-size: 16px;
    }
    
    .social-link:hover {
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    }
    
    .social-link.facebook:hover { background: #1877f2; color: #fff; }
    .social-link.instagram:hover { 
        background: linear-gradient(45deg, #f09433 0%, #e6683c 25%, #dc2743 50%, #cc2366 75%, #bc1888 100%);
        color: #fff;
    }
    .social-link.youtube:hover { background: #ff0000; color: #fff; }
    .social-link.tiktok:hover { background: #000; color: #fff; }
    .social-link.twitter:hover { background: #1da1f2; color: #fff; }
    
    /* Payment Methods */
    .footer-payment {
        padding: 30px 0;
        background: rgba(0, 0, 0, 0.2);
        border-top: 1px solid rgba(255, 255, 255, 0.1);
    }
    
    .payment-methods {
        display: flex;
        gap: 15px;
        flex-wrap: wrap;
        justify-content: center;
    }
    
    .payment-methods img {
        width: 40px;
        height: 40px;
        object-fit: contain;
        background: #fff;
        padding: 5px;
        border-radius: 8px;
        transition: transform 0.3s;
    }
    
    .payment-methods img:hover {
        transform: scale(1.1);
    }
    
    @media (min-width: 768px) {
        .payment-methods {
            justify-content: flex-start;
        }
        
        .text-md-end .payment-methods {
            justify-content: flex-end;
        }
    }
    
    /* Bottom Footer */
    .footer-bottom {
        padding: 20px 0;
        background: rgba(0, 0, 0, 0.3);
        border-top: 1px solid rgba(255, 255, 255, 0.1);
    }
    
    .footer-bottom p {
        color: rgba(255, 255, 255, 0.6);
        font-size: 13px;
    }
    
    .team-member {
        color: rgba(255, 255, 255, 0.8);
        font-weight: 500;
    }
    
    /* Back to Top Button */
    .back-to-top {
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 50px;
        height: 50px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        border: none;
        border-radius: 50%;
        font-size: 20px;
        cursor: pointer;
        display: none;
        align-items: center;
        justify-content: center;
        transition: all 0.3s;
        z-index: 999;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    }
    
    .back-to-top:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
    }
    
    .back-to-top.show {
        display: flex;
    }
    
    /* Responsive */
    @media (max-width: 768px) {
        .footer-main {
            padding: 40px 0 30px;
        }
        
        .footer-title {
            font-size: 16px;
        }
        
        .footer-logo h3 {
            font-size: 24px;
        }
        
        .payment-methods img {
            width: 35px;
            height: 35px;
        }
        
        .back-to-top {
            width: 45px;
            height: 45px;
            bottom: 20px;
            right: 20px;
        }
    }
</style>

<script>
    // Back to Top Button
    document.addEventListener('DOMContentLoaded', function() {
        const backToTopBtn = document.getElementById('backToTop');
        
        window.addEventListener('scroll', function() {
            if (window.pageYOffset > 300) {
                backToTopBtn.classList.add('show');
            } else {
                backToTopBtn.classList.remove('show');
            }
        });
        
        backToTopBtn.addEventListener('click', function() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    });
    
    // Newsletter Form
    document.querySelector('.newsletter-form').addEventListener('submit', function(e) {
        e.preventDefault();
        const email = this.querySelector('input[type="email"]').value;
        
        // TODO: Implement newsletter subscription
        alert('Cảm ơn bạn đã đăng ký nhận tin! Email: ' + email);
        this.reset();
    });
</script>
