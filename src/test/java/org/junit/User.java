package org.junit;

import java.util.UUID;

final class User {
	String name, surname;
	String email, password;
	
	User() {
		this.name = "Ernestas";
		this.surname = "Lisicynas";
		this.email = "ernestaslisicynas" + UUID.randomUUID() + "@gmail.com";
		this.password = "ernest";
	}
}
