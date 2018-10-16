package com.itheima.switchManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;



public class SwitchTopo extends HttpServlet {
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String controllerIP = request.getParameter("controllerIP");
		String controllerPort = request.getParameter("controllerPort");
		String a = "curl -s http://"+controllerIP+":"+controllerPort+"/wm/topology/links/json";
		//String a ="ls /";
			String switchInf = run(a);
			System.out.println(a);
			System.out.println(switchInf);
			String switchInf1 = switchInf.substring(1, switchInf.length()-2);
			System.out.println(switchInf1);
			String[] strings =switchInf1.split("},");
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Map<String, Object>> maps = new HashMap<>();
			for(int i=0;i<strings.length;i++){				
				strings[i] = strings[i]+"}";
				System.out.println(strings[i]);
		        map = JSONObject.fromObject(strings[i]);
		        System.out.println(map.get("src-switch"));
		        maps.put("link"+i,map);
			}
//			System.out.println(maps.get("link0").get("src-switch"));
//			System.out.println(maps.get("link0").get("dst-switch"));
			//赋值
			HttpSession session = request.getSession();
			for(int i = 0;i<strings.length;i++){
				//设置交换机的链接
				session.setAttribute("link"+i+"src-switch",maps.get("link"+i).get("src-switch"));
				session.setAttribute("link"+i+"src-port",maps.get("link"+i).get("src-port"));
				session.setAttribute("link"+i+"dst-switch",maps.get("link"+i).get("dst-switch"));
				session.setAttribute("link"+i+"dst-port",maps.get("link"+i).get("dst-port"));
			}
			//设置交换机的数量
			session.setAttribute("linkNumber", strings.length);
			
			request.setAttribute("switchInf", switchInf);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/switchTopoView.jsp");
			requestDispatcher.forward(request, response);

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
