package com.itheima.arithmetic.prePass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class PrePass
 */
public class PrePass extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //定义一个控制器IP
        String controllerIP = "172.16.104.128";
		//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(savePath);
        System.out.println(savePath);
        //判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            file.mkdir();
        }
        //消息提示
        String message = "";
        String[] filename = new String[2];
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
             //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8"); 
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            int i = 0;
            for(FileItem item : list){    
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    filename[i] = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename[i].trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename[i] = filename[i].substring(filename[i].lastIndexOf("/")+1);
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(savePath + "/" + filename[i]);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while((len=in.read(buffer))>0){
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    message = "文件上传成功！";
                }
                i++;
            }
        }catch (Exception e) {
            message= "文件上传失败！";
            e.printStackTrace();
            
        }
        //request.setAttribute("message",message);
        System.out.println("上传成功");
        
        //读上传的文件
       String finalPath1 = savePath+"/"+filename[0];
       String finalPath2 = savePath +"/"+filename[1];
       File flowFile = new File(finalPath1);      
       File macroFlowFile = new File(finalPath2);
       //普通流
       //普通flow流表下发
       String flow = fileToString(flowFile);
       String[] lines = flow.split("\n");
       String[][] num = new String[lines.length][10];
       for(int i = 0;i<lines.length;i++){
    	   num[i] = lines[i].split(" +");
       }
       //除去num[][2]列
       List<String> flowList = new ArrayList<String>();
       for(int i = 0;i<lines.length;i++){
    	   if(num[i].length>3){
        	   num[i][2] = num[i][3];
        	   String aString = Arrays.toString(num[i]);
        	   if(flowList.contains(aString)){
        		   //如果已经有了这个字符串，则将num[i][0]置空
        		   		num[i][0] = "";
        	   }else {
				flowList.add(aString);
			}
    	   }
       }
       for(int i = 0;i<lines.length;i++){
    	   if(num[i].length>3&&!num[i][0].equals("")){
    	   String ipv4_src = "10.0.0."+num[i][0];
    	   String ipv4_dst = "10.0.0."+num[i][1];
    	   //设置添加流表的交换机
		   MyStack stack = new MyStack(2);
		   MyStack inPort = new MyStack(2);
    	   for(int j = 3;j<num[i].length;j++){
    		   System.out.println(num[i][j]);
    		   //第一个交换机
    		   if(stack.isEmpty()){
    			   stack.push(Integer.parseInt(num[i][j]));
    			   inPort.push(1);
    		   }else if(j<num[i].length-1){
    			//第2-倒数第2个交换机，将1-2，2-3，3-4，...中插入流表在第1，2，3交换机
				String srcSwitch = String.valueOf(stack.pop());
				String dstSwitch = num[i][j];
				stack.push(Integer.parseInt(num[i][j]));
				//找到转发端口，编写流表
				String outPort = findPort(srcSwitch, dstSwitch);
				String nextInPort = findPort(dstSwitch, srcSwitch);
				String inPortString = String.valueOf(inPort.pop()); 
				inPort.push(Integer.parseInt(nextInPort));
				String finalSrcSwitch = "00:00:00:00:00:00:00:0"+srcSwitch;
        		String addFlowMessage = "{\"switch\":\""+finalSrcSwitch+"\",\"name\":\""+"flow-mod2-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"in_port\":\""+inPortString+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+outPort+"\"}";
        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner);
        		String resultString =run(manner);
        		System.out.println(resultString);
			}else if(j==num[i].length-1){
				//分别在j和j-1交换机上添加流表
				String srcSwitch = String.valueOf(stack.pop());
				String dstSwitch = num[i][j];
				//找到转发端口，编写流表
				String outPort = findPort(srcSwitch, dstSwitch);
				String inPortString = String.valueOf(inPort.pop()); 
				String finalSrcSwitch = "00:00:00:00:00:00:00:0"+srcSwitch;
				String finalDstSwitch = "00:00:00:00:00:00:00:0" + dstSwitch;
        		String addFlowMessage = "{\"switch\":\""+finalSrcSwitch+"\",\"name\":\""+"flow-mod2-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"in_port\":\""+inPortString+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+outPort+"\"}";
        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner);
        		run(manner);
        		//在第j个交换机上添加流表
        		String lastInport = findPort(dstSwitch, srcSwitch);
        		String addFlowMessage2 = "{\"switch\":\""+finalDstSwitch+"\",\"name\":\""+"flow-mod2-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"in_port\":\""+lastInport+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+"1"+"\"}";
        		String manner2 = "curl -X POST -d '"+addFlowMessage2+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner2);
        		run(manner2);
			}
    	   }
       }
       }
       //宏流
       //对宏流下发流表项
       String macroFlow = fileToString(macroFlowFile);    
       String[] lines2 = macroFlow.split("\n");
       String[][] num2 = new String[lines2.length][10];
       for(int i = 0;i<lines2.length;i++){
    	   num2[i] = lines2[i].split(" +");
       }
       for(int i = 0;i<lines2.length;i++){
    	   if(num2[i].length>3){
    	   String ipv4_src = "10.0.0."+num2[i][0];
    	   String ipv4_dst = "10.0.0."+num2[i][1];
    	   //设置添加流表的交换机
		   MyStack stack = new MyStack(2);
    	   for(int j = 3;j<num2[i].length;j++){
    		   System.out.println(num2[i][j]);
    		   //第一个交换机，只将交换机编号插入栈
    		   if(stack.isEmpty()){
    			   stack.push(Integer.parseInt(num2[i][j]));
    		   }else if(j<num2[i].length-1){
    			//第2-倒数第2个交换机，将1-2，2-3，3-4，...中插入流表在第1，2，3交换机
				String srcSwitch = String.valueOf(stack.pop());
				String dstSwitch = num2[i][j];
				stack.push(Integer.parseInt(num2[i][j]));
				//找到转发端口，编写流表
				String outPort = findPort(srcSwitch, dstSwitch);
				String finalSrcSwitch = "00:00:00:00:00:00:00:0"+srcSwitch;
        		String addFlowMessage = "{\"switch\":\""+finalSrcSwitch+"\",\"name\":\""+"macroflow-mod-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+outPort+"\"}";
        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner);
        		String resultString =run(manner);
        		System.out.println(resultString);
			}else if(j==num2[i].length-1){
				//分别在j和j-1交换机上添加流表
				String srcSwitch = String.valueOf(stack.pop());
				String dstSwitch = num2[i][j];
				//找到转发端口，编写流表
				String outPort = findPort(srcSwitch, dstSwitch);
				String finalSrcSwitch = "00:00:00:00:00:00:00:0"+srcSwitch;
				String finalDstSwitch = "00:00:00:00:00:00:00:0" + dstSwitch;
        		String addFlowMessage = "{\"switch\":\""+finalSrcSwitch+"\",\"name\":\""+"macroflow-mod-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+outPort+"\"}";
        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner);
        		run(manner);
        		//在第j个交换机上添加流表
        		String addFlowMessage2 = "{\"switch\":\""+finalDstSwitch+"\",\"name\":\""+"macroflow-mod2-"+i+j+"\",\"cookie\":\""+"0"
				+"\",\"priority\":\""+"32768"+"\",\"ipv4_src\":\""+ipv4_src+"\",\"ipv4_dst\":\""+ipv4_dst
				+"\""+",\"eth_type\":\""+"0x0800"+"\",\"active\":\""+"true"
				+"\",\"actions\":\""+"output="+"1"+"\"}";
        		String manner2 = "curl -X POST -d '"+addFlowMessage2+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
        		System.out.println(manner2);
        		run(manner2);
			}
    	   }
       }
       }
       System.out.println("建立流表项完成！");
//       RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
//       requestDispatcher.forward(request, response);
		String mess = "PrePass算法执行成功!";
		request.getSession().setAttribute("message", mess);
		response.sendRedirect(request.getContextPath()+"/flowMod.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static String fileToString(File file){
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while((s = br.readLine() )!= null){
				result.append(System.lineSeparator()+s);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}
	public static String findPort(String a,String b){
		String outPort = "";
		switch(Integer.parseInt(a)){
			case 1:
				if(b.equals("2")){
					outPort ="2";					
				}else if(b.equals("8")){
					outPort ="3";
				}else if(b.equals("3")){
					outPort ="4";
				}else {
					System.out.println("查询端口出错");
				}
				break;
			case 2:
				if(b.equals("1")){
					outPort ="2";
				}else if(b.equals("4")){
					outPort ="3";
				}else{
					System.out.println("查询端口出错");
				}
				break;
			case 3:
				if(b.equals("1")){
					outPort ="2";
				}else if(b.equals("5")){
					outPort = "3";
				}else{
					System.out.println("查询端口出错");						
				}
				break;
			case 4:
				if(b.equals("2")){
					outPort ="2";
				}else if(b.equals("7")){
					outPort = "3";
				}else {
					System.out.println("查询端口出错");
				}
				break;
			case 5:
				if(b.equals("3")){
					outPort = "2";
				}else if(b.equals("6")){
					outPort = "4";
				}else if(b.equals("8")){
					outPort = "3";
				}else {
					System.out.println("查询端口出错");
				}
				break;
			case 6:
				if(b.equals("5")){
					outPort = "2";
				}else if(b.equals("7")){
					outPort = "3";
				}else {
					System.out.println("查询端口出错");
				}
				break;
			case 7:
				if(b.equals("6")){
					outPort = "2";
				}else if(b.equals("8")){
					outPort = "4";
				}else if(b.equals("4")){
					outPort = "3";
				}else {
					System.out.println("查询端口出错");
				}
				break;
			case 8:
				if(b.equals("1")){
					outPort = "2";
				}else if(b.equals("5")){
					outPort = "3";
				}else if(b.equals("7")){
					outPort = "4";
				}else {
					System.out.println("查询端口出错");
				}
				break;	
		}
		return outPort;
	}
	
	public class MyStack {
	    private int[] array;
	    private int maxSize;
	    private int top;
	     
	    public MyStack(int size){
	        this.maxSize = size;
	        array = new int[size];
	        top = -1;
	    }
	     
	    //压入数据
	    public void push(int value){
	        if(top < maxSize-1){
	            array[++top] = value;
	        }
	    }
	     
	    //弹出栈顶数据
	    public int pop(){
	        return array[top--];
	    }
	     
	    //访问栈顶数据
	    public int peek(){
	        return array[top];
	    }
	     
	    //判断栈是否为空
	    public boolean isEmpty(){
	        return (top == -1);
	    }
	     
	    //判断栈是否满了
	    public boolean isFull(){
	        return (top == maxSize-1);
	    }
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
