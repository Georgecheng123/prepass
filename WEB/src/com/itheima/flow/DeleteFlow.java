package com.itheima.flow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteFlow
 */
public class DeleteFlow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFlow() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String switchsId = request.getParameter("switchsId");
		String controllerIP = request.getParameter("controllerIP");
		String controllerPort = request.getParameter("controllerPort");
		String delete = request.getParameter("deleteAll");
		String flowName = request.getParameter("flowName");
		if(delete.equals("value1")){
			String command = "curl http://"+controllerIP+":"+controllerPort+"/wm/staticflowentrypusher/clear/all/json";
			String aString = run(command);
			String message = "删除成功1";
			request.getSession().setAttribute("message", message);
			response.sendRedirect(request.getContextPath()+"/flowMod.jsp");
			System.out.println(message);
		}else if(delete.equals("value2")){
			String command = "curl http://"+controllerIP+":"+controllerPort+"/wm/staticflowentrypusher/clear/"+switchsId+"/json";
			System.out.println(command);
			String aString = run(command);
			String message = "删除成功2";
			request.getSession().setAttribute("message", message);
			response.sendRedirect(request.getContextPath()+"/flowMod.jsp");
			System.out.println(message);
		}
		else if(delete.equals("value3")){
			String command2 = "curl -X DELETE -d '{\"name\":\""+flowName+"\"}' http://"+controllerIP+":"+controllerPort+"/wm/staticflowentrypusher/json";
			String aString = command2;
			System.out.println(aString);
			String meString = run(aString);
			String message = "删除成功3";
			request.getSession().setAttribute("message", message);
			response.sendRedirect(request.getContextPath()+"/flowMod.jsp");
			System.out.println(message);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    public String run(String url) {
    	String s =null;
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(new String[]{"/bin/bash","-c",url}).getInputStream()));
            //StringBuffer b = new StringBuffer();
            String line=null;
            StringBuffer b=new StringBuffer();
            while ((line=br.readLine())!=null) {
                b.append(line+"\n");
            }
            System.out.println(b.toString());           
            s=b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }     
        return s;
    }

}
