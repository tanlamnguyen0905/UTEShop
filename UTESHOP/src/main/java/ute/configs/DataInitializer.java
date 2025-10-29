package ute.configs;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.utils.Constant;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Listener để khởi tạo dữ liệu ban đầu khi webapp start
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Khởi động ứng dụng UTESHOP ===");
        
        createDefaultAdminAccount();
        createUploadDirs(sce.getServletContext());
        
        System.out.println("[INFO] Khởi động hoàn tất!");
    }

    /**
     * Tạo tài khoản admin mặc định nếu chưa tồn tại
     */
    private void createDefaultAdminAccount() {
        UserDaoImpl userDao = new UserDaoImpl();
        
        try {
            // Kiểm tra xem admin đã tồn tại chưa
            if (!userDao.existsByUsername("admin")) {
                System.out.println("[INIT] Đang tạo tài khoản ADMIN mặc định...");
                
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
                
                System.out.println("[INFO] Tạo tài khoản admin thành công!");
                System.out.println("  -Username: admin");
                System.out.println("  -Password: admin");
                System.out.println("  -Email: admin@uteshop.com");
                System.out.println("  -Phone: 0123456789");
                System.out.println("  -Role: ADMIN");
            } else {
                System.out.println("[INFO] Tài khoản admin đã tồn tại, bỏ qua khởi tạo.");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Lỗi khi tạo tài khoản admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createUploadDirs(ServletContext context) {
        System.out.println("[INIT] Khởi tạo thư mục file upload tại:\n" + context.getRealPath("/"));
        for (String dir : Constant.ALL_UPLOAD_DIRS) {
            try {
                String realPath = context.getRealPath("/" + dir);
                if (realPath == null) {
                    System.err.println("[WARN] Không thể xác định đường dẫn thực cho: " + dir);
                    continue;
                }

                Path uploadPath = Paths.get(realPath);
                File folder = uploadPath.toFile();

                if (!folder.exists()) {
                    Files.createDirectories(uploadPath);
                    System.out.println("[INIT] Đã tạo thư mục: " + dir);
                } else {
                    System.out.println("[INIT] Thư mục đã tồn tại: " + dir);
                }

                // Kiểm tra quyền truy cập
                if (!folder.canRead() && !folder.canWrite()) {
                    System.err.println("[WARN] Không có quyền đọc/ghi thư mục: " + dir);
                } else if (!folder.canWrite()) {
                    System.err.println("[WARN] Không có quyền ghi vào thư mục: " + dir);
                } else if (!folder.canRead()) {
                    System.err.println("[WARN] Không có quyền đọc thư mục: " + dir);
                }

            } catch (Exception e) {
                System.err.println("[ERROR] Không thể tạo thư mục: " + dir);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[INFO] Webapp UTESHOP đang dừng...");
        JPAConfig.closeEntityManagerFactory();
    }
}

