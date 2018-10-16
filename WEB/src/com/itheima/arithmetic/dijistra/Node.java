package com.itheima.arithmetic.dijistra;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String name;
    private Map<Node,Integer> child=new HashMap<Node,Integer>();
    private Map<Node, String> port = new HashMap<Node,String>();
    public Node(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<Node, Integer> getChild() {
        return child;
    }
    public void setChild(Map<Node, Integer> child) {
        this.child = child;
    }
	public Map<Node, String> getPort() {
		return port;
	}
	public void setPort(Map<Node, String> port) {
		this.port = port;
	}
}
