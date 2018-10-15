<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'login.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
<form action="doget">
	<table align="center">
		<tr>
			<td align="center">用户名 ：</td>
			<td align="center"><input type="text" name="username" /></td>
		</tr>
		<tr>
			<td align="center">密码 ：</td>
			<td align="center"><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td align="center">验证码:</td>
			<td align="center"><input type="text" /></td>
		</tr>
		<tr>
			<td>
			<img src="CheckCode" name="img_checkCode" 
				width="116" height="43" class="img_checkCode" id="id" />
			</td>
			<td><input type="button" onclick="myReload();" value="点我刷新"></td>
		</tr>
		<tr>
			<td align="center"><input type="submit" value="点我登录" >
			</td>
		</tr>
	</table>
	</form>
	<script language="javascript">
		
		function myReload() {
			 document.getElementById("id").src= "CheckCode?hehe="+Math.random();
		}
	</script>
</body>
</html>
