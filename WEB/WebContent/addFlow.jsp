<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>添加流表界面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="css/style.css" type="text/css" />
		
		<style>
		body {
			margin-top: 20px;
			margin: 0 auto;
		}
		
		.carousel-inner .item img {
			width: 100%;
			height: 300px;
		}
		
		font {
			color: #3164af;
			font-size: 18px;
			font-weight: normal;
			padding: 0 10px;
		}
		</style>
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
				<font>添加流表</font>Add Flow
				<form class="form-horizontal" style="margin-top: 5px;" action="/WEB/addFlow" method="post">
					<div class="form-group">
						<p><span style="color:gray">请输入要添加的流表信息，格式如下：{"switch":"00:00:00:00:00:01","name":"flow-mod-1","cookie":"0","priority":"32768","in_port":"2","active":"true","actions":"output=2"}</span></p>
					</div>
					<div class="form-group">
						<label for="switchsId" class="col-sm-2 control-label" >流表信息</label>
					</div>
					<div class="form-group">
						<label for="controllerIP" class="col-sm-2 control-label" >控制器IP</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="controllerIP" name="controllerIP"
								placeholder="请输入控制器的IP">
						</div>
					</div>
						<div class="form-group">
<!-- 							<col-sm-6 input type="text" class="form-control" id="switchsId" name="switchsId" size="50px"
								placeholder="请输入要删除的交换机ID"> -->
							<label for="addFlow" class="col-sm-2 control-label" >添加流表数据</label>
							<div class="col-sm-6">
								<textarea name="addFlow" id="addFlow" cols="50" rows="10" placeholder="请输入要添加的流表数据"></textarea>
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