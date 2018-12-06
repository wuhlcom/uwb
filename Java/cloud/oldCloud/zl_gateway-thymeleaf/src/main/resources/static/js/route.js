
function getRoutes(){
    page
    if (username.length > 0 && password.length > 0) {
        $.ajax({
            type: "POST",
            url: '/login/auth',
            data: {username: username, password: password},
            success: function (data) {///去更新cookies
                if (data.resultCode == "success") {
                    window.location.href = "/gateway/routes/list";

                }else{
                    layer.alert('账号或者密码错误', {
                        icon: 2,
                    });
                    return;
                }
            }
        });
    } else {
        layer.alert('账号或者密码不能为空', {
            icon: 2,
        })
    }
}