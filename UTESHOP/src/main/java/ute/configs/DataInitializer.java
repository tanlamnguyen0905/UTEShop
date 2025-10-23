package ute.configs;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.utils.Constant;

import org.mindrot.jbcrypt.BCrypt;


import java.time.LocalDateTime;

/**
 * Listener để khởi tạo dữ liệu ban đầu khi webapp start
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("🚀 Khởi động ứng dụng UTESHOP...");
        System.out.println("========================================");
        
        createDefaultAccounts();
        
        System.out.println("========================================");
        System.out.println("✓ Khởi động hoàn tất!");
        System.out.println("========================================");
    }

    /**
     * Tạo tất cả các tài khoản mặc định
     */
    private void createDefaultAccounts() {
        createDefaultAdminAccount();
    }

    /**
     * Tạo tài khoản admin mặc định nếu chưa tồn tại
     */
    private void createDefaultAdminAccount() {
        UserDaoImpl userDao = new UserDaoImpl();
        
        try {
            // Kiểm tra xem admin đã tồn tại chưa
            if (!userDao.existsByUsername("admin")) {
                System.out.println("📝 Đang tạo tài khoản ADMIN mặc định...");
                
                String hashedPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
                
                Users admin = Users.builder()
                        .username("admin")
                        .password(hashedPassword)
                        .fullname("Quản Trị Viên Hệ Thống")
                        .email("admin@uteshop.com")
                        .phone("0123456789")
                        .avatar("default-avatar.png")
                        .role(Constant.ROLE_ADMIN)
                        .status(Constant.STATUS_ACTIVE)
                        .createAt(LocalDateTime.now())
                        .lastLoginAt(null)
                        .build();
                
                userDao.insert(admin);
                
                System.out.println("✓ Tạo tài khoản admin thành công!");
                System.out.println("  📌 Username: admin");
                System.out.println("  🔑 Password: admin");
                System.out.println("  📧 Email: admin@uteshop.com");
                System.out.println("  📞 Phone: 0123456789");
                System.out.println("  👤 Role: ADMIN");
            } else {
                System.out.println("✓ Tài khoản admin đã tồn tại, bỏ qua khởi tạo.");
            }
        } catch (Exception e) {
            System.err.println("✗ Lỗi khi tạo tài khoản admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🛑 Webapp UTESHOP đang dừng...");
    }
}

