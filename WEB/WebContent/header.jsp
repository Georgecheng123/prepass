<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.jpeg" />
	</div>
	<div class="col-md-5">
<!-- 		<img src="img/header.jpeg" /> -->
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
		<%if (session.getAttribute("username")==null){%>
  			<li><a href="login.jsp">登录</a></li>
			<li><a href="register.jsp">注册</a></li>
  			<%}else {%>
  			用户名：<%= session.getAttribute("username")%>
  			<li><a href="logout.jsp">退出</a></li>
 		    <%}%>

		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index.jsp">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
				<%if (session.getAttribute("username")!=null){%>
					<li class="active"><a href="flowMod.jsp">流表处理<span class="sr-only">(current)</span></a></li>
					<li><a href="http://localhost:8080/ui/pages/index.html">控制器管理</a></li>
					<li><a href="switchManager.jsp">交换机管理</a></li>
					<li><a href="arithmeticManager.jsp">算法管理</a></li>
					<li><a href="#">负载均衡</a></li>
				<%} %>
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>