package com.cos.photogramstart.service;
import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.domain.user.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional
	public Users 회원수정(int id, Users user) {
		// 1. 영속화
		Users userEntity = userRepository.findById(id).get(); // 1. 무조건 찾았다. 걱정마 get() 2. 못찾았어 익섹션 발동시킬께 orElseThrow()

		// 2. 영속화된 오브젝트를 수정 - 더티체킹 (어데이트 완료)
		userEntity.setName(user.getPassword());
		
		String rawPassword = user.getPassword();
		String encPassword =  bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return null;
	} // 더티체킹이 일어나서 업데이트가 완료됨.
}
