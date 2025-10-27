package ute.utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;

public class InsertTestData {
	public static void main(String[] args) {
		UserDaoImpl userDao = new UserDaoImpl();

		List<Users> testUsers = Arrays.asList(Users.builder().username("admin01").fullname("Quản trị viên 1")
				.email("admin1@uteshop.com").phone("0909000001").password(BCrypt.hashpw("admin123", BCrypt.gensalt()))
				.role("ADMIN").status("ACTIVE").lastLoginAt(LocalDateTime.now()).build(),

				Users.builder().username("employee01").fullname("Nhân viên bán hàng 1").email("employee1@uteshop.com")
						.phone("0909000002").password(BCrypt.hashpw("emp123", BCrypt.gensalt())).role("EMPLOYEE")
						.status("ACTIVE").lastLoginAt(LocalDateTime.now()).build(),

				Users.builder().username("user01").fullname("Nguyễn Văn A").email("user1@gmail.com").phone("0911000001")
						.password(BCrypt.hashpw("user123", BCrypt.gensalt())).role("USER").status("ACTIVE")
						.lastLoginAt(LocalDateTime.now()).build(),

				Users.builder().username("user02").fullname("Trần Thị B").email("user2@gmail.com").phone("0911000002")
						.password(BCrypt.hashpw("user123", BCrypt.gensalt())).role("USER").status("INACTIVE")
						.lastLoginAt(LocalDateTime.now().minusDays(10)).build(),

				Users.builder().username("employee02").fullname("Nhân viên giao hàng 1").email("employee2@uteshop.com")
						.phone("0909000003").password(BCrypt.hashpw("emp123", BCrypt.gensalt())).role("EMPLOYEE")
						.status("ACTIVE").lastLoginAt(LocalDateTime.now().minusDays(2)).build());

		System.out.println("🔧 Inserting test data...");
		for (Users user : testUsers) {
			try {
				if (!userDao.existsByUsername(user.getUsername())) {
					userDao.insert(user);
					System.out.println("✅ Inserted user: " + user.getUsername());
				} else {
					System.out.println("⚠️ Skipped (already exists): " + user.getUsername());
				}
			} catch (Exception e) {
				System.err.println("❌ Failed to insert user: " + user.getUsername());
				e.printStackTrace();
			}
		}

		System.out.println("🎉 Test data insertion complete.");
	}
}
