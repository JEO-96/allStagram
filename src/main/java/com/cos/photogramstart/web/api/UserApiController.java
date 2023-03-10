package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.Users;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.auth.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.subscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@RestController // file 반환이 아닌 data 를 보내줘야 하기에 RestController 방식
public class UserApiController {

    private final UserService usersService;
    private final SubscribeService subscribeService;

    @GetMapping("/api/user/{pageUserId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){
    	
    	List<subscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);
    	
    	return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 불러오기 성공", subscribeDto), HttpStatus.OK);
    }
    
    @PutMapping(value = "api/user/{id}")
    public CMRespDto<?> update(
            @PathVariable int id,
            @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult, // @Valid 가 적혀 있는 다음 파라미터에 적어야 적용 됨!!!!
            @AuthenticationPrincipal PrincipalDetails principalDetails){
        if (bindingResult.hasErrors()) {
            Map<String, String> errMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationApiException("유효성 검사 실패 함", errMap); // 해당 오류가 발생하면 handler
        } else {
            Users userEntity = usersService.회원수정(id, userUpdateDto.toEntity());
            principalDetails.setUser(userEntity); // 세션 정보 변경
            return new CMRespDto<>(1,"회원수정 완료", userEntity); // 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
        }
    }

}