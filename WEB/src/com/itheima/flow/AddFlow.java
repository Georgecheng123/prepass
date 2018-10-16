package com.itheima.flow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddFlow
 */
public class AddFlow extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String addFlowMessage = request.getParameter("addFlow");
		addFlowMessage.replace("\n", "");
		String controllerIP = request.getParameter("controllerIP");
		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";
		System.out.println(manner);
		//添加流表
		String switchInf = run(manner);
		String message = "添加流表成功";
		request.getSession().setAttribute("message", message);
		response.sendRedirect(request.getContextPath()+"/flowMod.jsp");
		System.out.println(message);
//		String string = "http://192.168.1.101:8080/wm/staticflowentrypusher/list/all/json";
//		response.sendRedirect(string);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    public String run(String url) {
        Runtime runtime = Runtime.getRuntime();
        String s = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(new String[]{"/bin/bash","-c",url}).getInputStream()));
            //StringBuffer b = new StringBuffer();
            String line=null;
            StringBuffer b=new StringBuffer();
            while ((line=br.readLine())!=null) {
                b.append(line+"\n");
            }
            System.out.println(b.toString());           
            s=	b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return s;
        
    }
}
