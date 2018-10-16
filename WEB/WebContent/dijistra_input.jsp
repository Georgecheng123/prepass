<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>输入要查的交换机界面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
		<div class="container-fluid">	
		<!-- 引入header.jsp -->
		<jsp:include page="/header.jsp"></jsp:include>
		
			<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>输入起始交换机</font>Input the first switch
				<form class="form-horizontal" style="margin-top: 5px;" action="/WEB/dijistra" method="post">
					<div class="form-group">
						<label for="switchId" class="col-sm-2 control-label" >交换机ID</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="switchId" name="switchId"
								placeholder="请输入起始交换机ID">
						</div>
					</div>
					<div class="form-group">
						<label for="controllerIP" class="col-sm-2 control-label" >控制器IP</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="controllerIP" name="controllerIP"
								placeholder="请输入控制器IP">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" width="100" value="提交" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>
		
		<jsp:include page="/footer.jsp"></jsp:include>
		</div>
</body>
</html>