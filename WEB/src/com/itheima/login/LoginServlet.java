package com.itheima.login;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.register.User;
import com.itheima.utils.DataSourceUtils;




public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//1.获得用户名密码
		String username = request.getParameter("username");
		String password=request.getParameter("password");
		
		//2.调用一个业务方法进行该用户查询
		 User user = null;
		try {
			user = login(username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//3.通过user是否为空判断用户名密码是否正确
		if(user!=null){
			//代表用户名密码正确
			//跳转到网站的首页
			//添加session
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		}else {
			//用户名或密码错误
			//跳回当前login.jsp页面
			request.setAttribute("loginInfo", "用户密码错误");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
			requestDispatcher.forward(request, response);

		}
	}
	public User login(String username,String password) throws SQLException{
		QueryRunner qRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user where username = ? and password = ?";
		User user = qRunner.query(sql, new BeanHandler<User>(User.class),username,password );
		return user;
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}