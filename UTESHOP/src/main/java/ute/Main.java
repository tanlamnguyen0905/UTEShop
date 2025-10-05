package ute;
import jakarta.persistence.*;
import lombok.*;
public class Main {
	public static void main(String[] args) {
		EntityManager em = Persistence.createEntityManagerFactory("UTESHOP").createEntityManager();
		
	}
}
