package ute;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;
public class Main {
	public static void main(String[] args) {
		EntityManager em = Persistence.createEntityManagerFactory("jpa-demo").createEntityManager();
		
	}
}
