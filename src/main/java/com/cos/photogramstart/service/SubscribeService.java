package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.subscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<subscribeDto> 구독리스트(int principalId, int pageUserId){
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profile_image_url, ");
		sb.append("CASE WHEN (SELECT true FROM subscribe WHERE from_user_id = ? AND to_user_id = u.id) THEN true ELSE false END subscribe_state, ");
		sb.append("CASE WHEN (?=u.id) THEN true ELSE false END equalUserState ");
		sb.append("FROM users u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.to_user_id ");
		sb.append("WHERE s.from_user_id = ?"); // 세미콜론 첨부하면 안됨
		
		// 1. 물음표 principalId
		// 2. 물음표 principalId
		// 3. 마지막 물음표 page_user_id
		
		// 쿼리 완성
		Query quere = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		// 쿼리 실행 (qlrm 라이브러리 필요 = Dto에 DB결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<subscribeDto>subscribeDtos = result.list(quere, subscribeDto.class);
		
		return subscribeDtos;
	}
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
