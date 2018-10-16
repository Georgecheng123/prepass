package com.itheima.header;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获得指定头
		String header = request.getHeader("User-Agent");
		System.out.println(header);
		//2.获得所有头的名称
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String nextElement = headerNames.nextElement();
			String headerValue = request.getHeader(nextElement);
			System.out.println("headerName:"+headerValue);		
		}

		};
		

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}