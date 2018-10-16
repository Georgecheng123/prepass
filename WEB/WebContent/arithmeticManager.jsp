<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>流表处理页面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<style type="text/css">
			#Content-left1 {
			height: 400px;
            width: 30%;
            padding-left:40px;
            padding-top:40px;
            margin: 20px; /*设置元素跟其他元素的距离为20像素*/
            float: left; /*设置浮动，实现多列效果，div+Css布局中很重要的*/
            /* background: #90C; */
			}
			#Content-left2 {
			height: 400px;
            width: 30%;
            padding-left:40px;
            padding-top:40px;
            margin: 20px; /*设置元素跟其他元素的距离为20像素*/
            float: left; /*设置浮动，实现多列效果，div+Css布局中很重要的*/
            /* background: #90C; */
			}
			#Content-left3 {
			height: 400px;
            width: 30%;
            padding-left:40px;
            padding-top:40px;          
            margin: 20px; /*设置元素跟其他元素的距离为20像素*/
            float: left; /*设置浮动，实现多列效果，div+Css布局中很重要的*/
            /* background: #90C; */
			}
			#Content-bottom{
			background:#1234;
			text-align:center;
	 		margin-right:50px; 
			}
		</style>
	</head>
<body>
		<div class="container-fluid">	
		<!-- 引入header.jsp -->
		<jsp:include page="/header.jsp"></jsp:include>
		<div id="Content-left1">
			<img alt="1" src="img/pic1.jpeg">
			<div id="Content-bottom"><h2><a href="dijistra_input.jsp">迪杰斯特拉算法</a></h2></div>
		</div>
		<div id="Content-left1">
			<img alt="1" src="img/pic1.jpeg">
			<div id="Content-bottom"><h2><a href="prePass_input.jsp">PrePass算法</a></h2></div>
		</div>
		<jsp:include page="/footer.jsp"></jsp:include>
		</div>
	</body>
</body>
</html>