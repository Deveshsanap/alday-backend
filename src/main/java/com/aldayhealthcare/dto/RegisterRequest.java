package com.aldayhealthcare.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

	@JsonAlias({ "fullName", "fullname", "name", "username" })
	private String name;
	private String email;
	private String password;
	private String role;
}