package com.cos.photogramstart.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.Users;

import lombok.Data;


@Data
public class ImageUploadDto {
	private MultipartFile file;
	private String caption;
	
	public Image toEntity(String postImageUrl, Users user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}
}
