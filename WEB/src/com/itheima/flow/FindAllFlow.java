package com.itheima.flow;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.swing.internal.plaf.basic.resources.basic;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Servlet implementation class FindAllFlow
 */
public class FindAllFlow extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String switchId = request.getParameter("switchId");
		String controllerIP = request.getParameter("controllerIP");
		String controllerPort = request.getParameter("controllerPort");
		String find = request.getParameter("findAll");
		String a = "curl http://"+controllerIP+":"+controllerPort+"/wm/staticflowentrypusher/list/"+switchId+"/json";
		String b = "curl http://"+controllerIP+":"+controllerPort+"/wm/staticflowentrypusher/list/all/json";
		if(find.equals("case1")){
			String switchInf = run(b);
			System.out.println(b);
			request.setAttribute("switchInf", switchInf);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/switchValue.jsp");
			requestDispatcher.forward(request, response);
		}else {
			String switchInf = run(a);
			System.out.println(a);
			request.setAttribute("switchInf", switchInf);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/switchValue.jsp");
			requestDispatcher.forward(request, response);
		}

}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
}

    public String run(String url) {
        Runtime runtime = Runtime.getRuntime();
        String s = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(url).getInputStream()));
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
