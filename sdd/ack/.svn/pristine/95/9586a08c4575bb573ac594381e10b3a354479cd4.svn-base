<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh_cn">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>登录</title>
	<link rel="shortcut icon" href="#" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/font-awesome.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/header.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/footer.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login/login.css">

	<script src="${pageContext.request.contextPath}/js/common/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/common/bootstrap.min.js"></script>

	<script src="${pageContext.request.contextPath}/js/common/globa.js"></script>

	<script src="${pageContext.request.contextPath}/js/nice-validator/jquery.validator.js?local=zh-CN"></script>
</head>

<body>
	<div class="head">
		<div class="head-top">
			<div class="logo">
                <a href="">
                    <span>积分系统</span>
                </a>
			</div>
		</div>
		<div class="login-content content">
			<div class="content-box">
				<ul>
					<li class="active"><a herf="#">欢迎登录</a></li>
				</ul>
				<form id="login-form" class="form" action ="/login">
                        <div class="form-group">

                            <div class="form-content">
                            <label>账号：</label>
                                <input type="text" class="form-control" name = "username" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-content">
                                <label>密码：</label>
                                <input type="password" class="form-control" name = "password" value="">
                            </div>

                        </div>
                        <div class="sumbit-box text-center">
                            <button type="submit" class="btn btn-primary min-btn mr-2">登录</button>
                        </div>
				</form>
			</div>
		</div>
	</div>
	<footer class="text-center">
		<p>Copyright © 2018 - 2030 UD. All Rights Reserved. <br>优地网络，版权所有</p>
	</footer>
</body>
<script type="text/javascript">
    $(function () {


        $('#login-form').validator({
            fields: {
                'username': 'required;username',
                'password': 'required;password'
            },
            valid: function(form)
            {
                 $.ajax({
                          type: "POST",
                          url: "${pageContext.request.contextPath}/login",
                          data: $(form).serialize(),
                          dataType: "json",
                          success: function(data)
                          {
                            if(data.success)
                            {
                                window.location.href = "${pageContext.request.contextPath}/activity";
                            }else
                            {
                                $("input[name=password]").trigger("showmsg", ["error", data.msg]);
                            }
                           }
                      });
            }
        });

    })
</script>
</html>