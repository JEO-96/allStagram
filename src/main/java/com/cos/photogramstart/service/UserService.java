package com.cos.photogramstart.service;
import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.domain.user.Users;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		// SELECT * FROM image WHERE user_id= :user_id
		Users userEntity = userRepository.findById(pageUserId).orElseThrow(()-> {
			 throw new CustomException("해당 프로핑 페이지는 없는 페이지입니다");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);	// 1은 페이지 주인, -1은 주인이 아님
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		 
		return dto;
	}
	
	@Transactional
	public Users 회원수정(int id, Users user) {
		// 1. 영속화
		Users userEntity = userRepository.findById(id).orElseThrow(()->{return new CustomValidationApiException("찾을 수 없는 id입니다.");}); // 1. 무조건 찾았다. 걱정마 get() 2. 못찾았어 익섹션 발동시킬께 orElseThrow()

		// 2. 영속화된 오브젝트를 수정 - 더티체킹 (어데이트 완료)
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encPassword =  bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;
	} // 더티체킹이 일어나서 업데이트가 완료됨.
}
