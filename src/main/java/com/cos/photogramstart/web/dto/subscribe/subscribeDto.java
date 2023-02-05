package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class subscribeDto {
	private int id;
	private String username;
	private String profile_image_url;
	private boolean subscribeState; // Integer -> boolean
	private boolean equalUserState; // Integer -> boolean
}
