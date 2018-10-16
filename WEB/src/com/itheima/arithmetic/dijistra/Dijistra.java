package com.itheima.arithmetic.dijistra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





/**
 * Servlet implementation class Dijistra
 */
public class Dijistra extends HttpServlet {
    Set<Node> open=new HashSet<Node>();
    Set<Node> close=new HashSet<Node>();
	 Map<String,Integer> path=new HashMap<String,Integer>();//封装路径距离
	 Map<String,String> pathInfo=new HashMap<String,String>();//封装路径信息
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 HttpSession session = request.getSession();
		 String first_switchId = request.getParameter("switchId");
		 String controllerIP = request.getParameter("controllerIP");
		 int linkNumber = (int)session.getAttribute("linkNumber");
		 //默认设交换机最多为100个
//		 Node[] nodes = new Node[100];
		 Set<String> switches = new HashSet<>();
		 //设置参数
		 //选择与first_switchId相连的交换机，设置cost，不相连的交换机设置为不可达
			for(int i = 0;i<linkNumber;i++){				
				String srcSwitch = (String) session.getAttribute("link"+i+"src-switch");
				String dstSwitch = (String) session.getAttribute("link"+i+"dst-switch");
				int srcPort = (int) session.getAttribute("link"+i+"src-port");
				int dstPort = (int) session.getAttribute("link"+i+"dst-port");
				if(!(switches.contains(srcSwitch))){
					switches.add(srcSwitch);
				}
				if(!(switches.contains(dstSwitch))){
					switches.add(dstSwitch);
				}
				if(srcSwitch.equals(first_switchId)){
					path.put(dstSwitch, 1);
					pathInfo.put(dstSwitch, srcSwitch+"-"+srcPort+","+dstSwitch+"-"+dstPort);
				}else if(dstSwitch.equals(first_switchId)){
					path.put(srcSwitch, 1);
					pathInfo.put(srcSwitch, dstSwitch+"-"+dstPort+","+srcSwitch+"-"+srcPort);
				}else {
					if(!(path.containsKey(srcSwitch))){
						path.put(srcSwitch, Integer.MAX_VALUE);
						pathInfo.put(srcSwitch, first_switchId);
					}
					if(!(path.containsKey(dstSwitch))){
						path.put(dstSwitch, Integer.MAX_VALUE);
						pathInfo.put(dstSwitch, first_switchId);
					}					
				}
			}
			Iterator<String> iterator = switches.iterator();
			int switchNumber = switches.size();
			Node[] nodes = new Node[switchNumber];
			int i = 0;
			while (iterator.hasNext()) {
				String sw = iterator.next();
				nodes[i] = new Node(sw);
				System.out.println(i+"     "+sw);
				++i;
			}
			for(Node node:nodes){
				System.out.println("交换机名字:"+node.getName());
			}
			for(int k = 0;k<linkNumber;k++){
				String srcSwitch = (String) session.getAttribute("link"+k+"src-switch");
				String dstSwitch = (String) session.getAttribute("link"+k+"dst-switch");
				int srcPort = (int) session.getAttribute("link"+k+"src-port");
				int dstPort = (int) session.getAttribute("link"+k+"dst-port");
				for(int e = 0;e<switches.size();e++){
					for(int p = 0;p<switches.size();p++){
						if(nodes[e].getName().equals(srcSwitch)&&nodes[p].getName().equals(dstSwitch)){
							//添加一条源到目的的路径
							//双向设置路径和端口
							nodes[e].getChild().put(nodes[p], 1);
							nodes[e].getPort().put(nodes[p], srcPort+"");
							nodes[p].getChild().put(nodes[e], 1);
							nodes[p].getPort().put(nodes[e], dstPort+"");
							}
					}
				}
			}
			Node firstNode = null;
			for(int r = 0;r<nodes.length;r++){
				if(nodes[r].getName().equals(first_switchId)){
					close.add(nodes[r]);
					firstNode = nodes[r];
				}else {
					open.add(nodes[r]);
				}
			}
			
			computePath(firstNode);
			//printPathInfo();
	        Set<Map.Entry<String, String>> pathInfos=pathInfo.entrySet();
	        int nums = 0;
	        for(Map.Entry<String, String> pathInfo:pathInfos){
	            System.out.println(first_switchId+" to "+pathInfo.getKey()+"||"+pathInfo.getValue());
	            request.setAttribute("pathinfo"+nums, first_switchId+" to "+pathInfo.getKey()+"||"+pathInfo.getValue());
	            nums++;
	        }
	 
	        //在各个交换机上添加流表
	        //规定x号交换机连接的x号主机端口号为1，
	        //如：xx:xx:xx:xx:xx:xx:xx:01号交换机连接 的主机ip为10.0.0.1 mac地址为 xx:xx:xx:xx:xx:01
	        int flowNum = 0;//对流表计数

	        for(Map.Entry<String, String> path:pathInfos){
	        	String startNode = first_switchId;
	        	String endNode = ((Entry<String, String>) path).getKey();
	        	String[] midNode = ((Entry<String, String>) path).getValue().split(",");
	        	if(midNode.length==2){
	        		//分别在两个直接相连的交换机上插入两条流表项
	        	for(int t = 0;t<midNode.length;t++){
	    	        FlowEntry flowEntry = new FlowEntry();
	        		//交换机ID
	        		String startDataPathID = null;
	        		String endDataPathID = null;
	        		String startToEndPort = null;
	        		String endToStartPort = null;
	        		for(int k = 0;k<midNode.length;k++){
	        			String[] pidAndPort = midNode[k].split("-");
	        			if(k==0){
	        				startDataPathID = pidAndPort[0];
	        				startToEndPort = pidAndPort[1];
	        			}else if(k==1){
							endDataPathID = pidAndPort[0];
							endToStartPort = pidAndPort[1];
						}
	        		}
	        		int endNumber = Integer.parseInt(endDataPathID.substring(endDataPathID.length()-2));
	        		if(t==0){
	        			flowEntry.setSwitchName(startDataPathID);
	        		}else if(t==1){
	        			flowEntry.setSwitchName(endDataPathID);
					}	        		
	        		flowEntry.setName("flow-mod-"+flowNum);
	        		flowNum++;
	        		flowEntry.setCookie("0");
	        		flowEntry.setPriority("32768");
	        		if(t==0){
	        			flowEntry.setIn_port("1");
	        		}else if(t==1){
	        			flowEntry.setIn_port(endToStartPort);
	        		}
	        		flowEntry.setEth_dst("00:00:00:00:00:"+endDataPathID.substring(endDataPathID.length()-2));
	        		flowEntry.setIpv4_dst("10.0.0."+endNumber); 
	        		flowEntry.setEth_type("0x0800");
	        		flowEntry.setActive("true");
	        		if(t==0){
	        			flowEntry.setActions("output="+startToEndPort);
	        		}else if(t==1){
	        			flowEntry.setActions("output=1");
	        		}
//	        		System.out.println(startDataPathID+"   "+startToEndPort+"  "+endDataPathID);
	        		String addFlowMessage = "{\"switch\":\""+flowEntry.getSwitchName()+"\",\"name\":\""+flowEntry.getName()+"\",\"cookie\":\""+flowEntry.getCookie()
	        												+"\",\"priority\":\""+flowEntry.getPriority()+"\",\"in_port\":\""+flowEntry.getIn_port()+"\",\"eth_dst\":\""+flowEntry.getEth_dst()
	        												+"\",\"ipv4_dst\":\""+flowEntry.getIpv4_dst()+"\",\"eth_type\":\""+flowEntry.getEth_type()+"\",\"active\":\""+flowEntry.getActive()
	        												+"\",\"actions\":\""+flowEntry.getActions()+"\"}";
	        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
	        		System.out.println(manner);
	        		run(manner);
	        		}
	        	}else if(midNode.length>2){
	        		int midPid = 0;
	        		for(int p =0;p<=(midNode.length/2);p++){
		    	        FlowEntry flowEntry = new FlowEntry();
		        		//交换机ID
		        		String startDataPathID = null;
		        		String endDataPathID = null;
		        		String[] midDataPathID = new String[midNode.length-2];
		        		
		        		//String[] startToEndPort = null;
		        		HashMap<String, String> outPort = new HashMap<>();
		        		HashMap<String, String> inPort = new HashMap<>();
		        		int midNum = 0;//记录中间midDataPathID序号
		        		for(int k = 0;k<midNode.length;k++){		        			
		        			String[] pidAndPort = midNode[k].split("-");
		        			if(k==0){
		        				//经过的第一个交换机
		        				startDataPathID = pidAndPort[0];
		        				outPort.put(startDataPathID, pidAndPort[1]);
		        			}else if(k==midNode.length-1){
		        				//经过的最后一个交换机
								endDataPathID = pidAndPort[0];
								inPort.put(endDataPathID, pidAndPort[1]);
							}else {
								//经过的中间交换机
								midDataPathID[midNum] = pidAndPort[0];
								//k为基数，代表路径进入的端口，k为偶数，代表路径出去的端口
								if(k%2==1){
									inPort.put(midDataPathID[midNum], pidAndPort[1]);
								}else if(k%2==0){
									outPort.put(midDataPathID[midNum], pidAndPort[1]);
								}
								//midNum为0，1是一个交换机，2，3是一个交换机
								midNum++;
							}
		        		}
		        		int endNumber = Integer.parseInt(endDataPathID.substring(endDataPathID.length()-2));
		        		int midNumber[] = new int[(midNode.length)/2-1];
		        		for(int num = 0;num<(midNode.length/2-1);num++){
		        			midNumber[num] = Integer.parseInt((midDataPathID[num/2]).substring((midDataPathID[num/2]).length()-2));
		        		}
		        		
		        		if(p==0){
		        			flowEntry.setSwitchName(startDataPathID);
		        		}else if(p==midNode.length/2 ){
		        			flowEntry.setSwitchName(endDataPathID);
		        		}else {
							flowEntry.setSwitchName(midDataPathID[midPid]);							
//							midPid+=2;  记得最后加这个！！！
						}		        		
		        		flowEntry.setName("flow-mod-"+flowNum);
		        		flowNum++;
		        		flowEntry.setCookie("0");
		        		flowEntry.setPriority("32768");
		        		if(p==0){
		        			flowEntry.setIn_port("1");
		        		}else if(p==midNode.length/2){
							flowEntry.setIn_port(inPort.get(endDataPathID));
						}else {
							flowEntry.setIn_port(inPort.get(midDataPathID[midPid]));
						}
		        		
		        		flowEntry.setEth_dst("00:00:00:00:00:"+endDataPathID.substring(endDataPathID.length()-2));
		        		flowEntry.setIpv4_dst("10.0.0."+endNumber); 
		        		flowEntry.setEth_type("0x0800");
		        		flowEntry.setActive("true");
		        		if(p==0){
		        			flowEntry.setActions("output="+outPort.get(startDataPathID));
		        		}else if(p==midNode.length/2){
							flowEntry.setActions("output=1");
						}else {
							flowEntry.setActions("output="+outPort.get(midDataPathID[midPid]));
							midPid++;
						}
		        		
	//	        		System.out.println(startDataPathID+"   "+startToEndPort+"  "+endDataPathID);
		        		String addFlowMessage = "{\"switch\":\""+flowEntry.getSwitchName()+"\",\"name\":\""+flowEntry.getName()+"\",\"cookie\":\""+flowEntry.getCookie()
		        												+"\",\"priority\":\""+flowEntry.getPriority()+"\",\"in_port\":\""+flowEntry.getIn_port()+"\",\"eth_dst\":\""+flowEntry.getEth_dst()
		        												+"\",\"ipv4_dst\":\""+flowEntry.getIpv4_dst()+"\",\"eth_type\":\""+flowEntry.getEth_type()+"\",\"active\":\""+flowEntry.getActive()
		        												+"\",\"actions\":\""+flowEntry.getActions()+"\"}";
		        		String manner = "curl -X POST -d '"+addFlowMessage+"' http://"+controllerIP+":8080/wm/staticentrypusher/json";        		
		        		System.out.println(manner);
		        		run(manner);
		        	}
	        	}
	        }
	        //清空数据
	        open=new HashSet<Node>();
	        close=new HashSet<Node>();
	    	path=new HashMap<String,Integer>();//封装路径距离
	    	pathInfo=new HashMap<String,String>();//封装路径信息
	       RequestDispatcher requestDispatcher =  request.getRequestDispatcher("/dijistra_output.jsp");
	       requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    public void computePath(Node start){
        Node nearest=getShortestPath(start);//取距离start节点最近的子节点,放入close
        if(nearest==null){
            return;
        }
        close.add(nearest);
        open.remove(nearest);
        Map<Node,Integer> childs=nearest.getChild();
        for(Node child:childs.keySet()){
            if(open.contains(child)){//如果子节点在open中
                Integer newCompute=path.get(nearest.getName())+childs.get(child);
                if(path.get(child.getName())>newCompute){//之前设置的距离大于新计算出来的距离
                    path.put(child.getName(), newCompute);
                    String msg1 = pathInfo.get(nearest.getName())+","+nearest.getName()+"-"+nearest.getPort().get(child)+","+child.getName()+"-"+child.getPort().get(nearest);
                    String msg2 = pathInfo.get(nearest.getName())+","+child.getName()+"-"+nearest.getPort().get(child)+","+child.getName();
                    pathInfo.put(child.getName(), msg1);
                    msg1=null;
                }
            }
        }
        computePath(start);//重复执行自己,确保所有子节点被遍历
        computePath(nearest);//向外一层层递归,直至所有顶点被遍历
    }
//    public void printPathInfo(){
//        Set<Map.Entry<String, String>> pathInfos=pathInfo.entrySet();
//        for(Map.Entry<String, String> pathInfo:pathInfos){
//            System.out.println(first_switchId+" to "+pathInfo.getKey()+"||"+pathInfo.getValue());
//        }
//    }
    /**
     * 获取与node最近的子节点
     */
    private Node getShortestPath(Node node){
        Node res=null;
        int minDis=Integer.MAX_VALUE;
        Map<Node,Integer> childs=node.getChild();
        for(Node child:childs.keySet()){
            if(open.contains(child)){
                int distance=childs.get(child);
                if(distance<minDis){
                    minDis=distance;
                    res=child;
                }
            }
        }
        return res;
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
