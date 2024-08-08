$(document).ready(function() {
    $('#loginBotton').on('click', function(event) {
        event.preventDefault();
		
        const username = $('#username').val();
        const password = $('#password').val();
		
        $.ajax({
            type: 'POST',
            url: '/demo/member/loginAJAX',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {	
                if (response === 'success') {
                    window.location.href = '/demo/member/memberInfo';
                }else if (response === 'fail'){
					$('#errorMsg').text('帳號密碼錯誤，請重新輸入');
				}else if (response === 'null'){
					$('#errorMsg').text('查無此會員帳號');
                }else {
					$('#errorMsg').text('請輸入會員帳號密碼');
				}
            },
            error: function(xhr){
                if(xhr.status === 401) {
                    var response = JSON.parse(xhr.responseText);
                    $('#errorMsg').text(response.error);
                } else {
                    $('#errorMsg').text("網頁發生錯誤，請稍後再試!!");
                }
			}
        });
    });
});