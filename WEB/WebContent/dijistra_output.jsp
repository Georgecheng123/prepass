<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Dijistra算法显示界面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
		<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<style type="text/css">
		table, td, th
		  {
		  border:1px solid black;
		  }
		td
		  {
		  padding:20px;
		  }
		th
		  {
		  background-color:green;
		  color:white;
		  padding:20px;
		  }
		  </style>
</head>
<body>
		<div class="container-fluid">	
		<!-- 引入header.jsp -->
		<jsp:include page="/header.jsp"></jsp:include>
		
			<div class="container" style="width: 100%; background: url('image/regist_bg.jpg');">
<%-- 		<div class="row">
		<!-- 显示没处理的原生switch信息 -->
			<%String switchInf = (String)request.getAttribute("switchInf");%>
			<%out.print(switchInf);%>
		</div> --%>
			<table align="center">  
     <!-- 第一行 -->  
		     <tr>  
		      <th>最短路径</th>     
		     </tr> 
		     <%Integer linkNumber = (Integer)session.getAttribute("linkNumber"); %>
		     <%for(int i=0;i<linkNumber-1;i++){ 
		    	 
		    	 %>
		     	<tr>
		     		<td><%=request.getAttribute("pathinfo"+i) %></td>
		     	</tr>
		     <%} %>
		      <!-- 第二行 -->  	  
		    </table>  
		</div>
	</div>
		
		<jsp:include page="/footer.jsp"></jsp:include>
		</div>
</body>
</html>