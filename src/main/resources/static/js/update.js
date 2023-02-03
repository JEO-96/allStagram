// (1) 회원정보 수정
function update(userid, event) {
    event.preventDefault() // 폼태그 액션 진행 되지 않는다

    let data = $("#profileUpdate").serialize();




    $.ajax({
        type: "PUT",
        url: `/api/user/${userid}`,
        data: data,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        dataType: "json",


    }).done(res => { // HTTP Status 상태코드가 200번대 일 때, done 이 실행된다.
        console.log("성공", res);
        location.href = `/user/${userid}`;
    }).fail(error => { // HTTP Status 상태코드가 200번대가 아닐 때, fail 이 실행된다.
        console.log("실패", error.responseJSON.data);
    });
}