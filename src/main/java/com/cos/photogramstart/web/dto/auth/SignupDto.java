package com.cos.photogramstart.web.dto.auth;

import com.cos.photogramstart.domain.user.Users;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;

@Data // Getter, Setter
public class SignupDto {
	
	@Size(min = 2, max = 20)
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	
	public Users toEntity() {
		return Users.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}
