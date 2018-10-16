<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>流表信息界面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
		<div class="container-fluid">	
		<!-- 引入header.jsp -->
		<jsp:include page="/header.jsp"></jsp:include>
		
			<div class="container" style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<%String switchInf = (String)request.getAttribute("switchInf");%>
			<%out.print(switchInf);%>
			
		</div>
		</div>
	</div>
		
		<jsp:include page="/footer.jsp"></jsp:include>
		</div>
</body>
</html>