package com.itheima.arithmetic.dijistra;

public class FlowEntry {
	String switchName;
	String name;
	String cookie;
	String priority;
	String in_port;
	String eth_dst;
	String eth_src;
	String ipv4_dst;
	String ipv4_src;
	String eth_type;
	String active;
	String actions;
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getIn_port() {
		return in_port;
	}
	public void setIn_port(String in_port) {
		this.in_port = in_port;
	}
	public String getEth_dst() {
		return eth_dst;
	}
	public void setEth_dst(String eth_dst) {
		this.eth_dst = eth_dst;
	}
	public String getEth_src() {
		return eth_src;
	}
	public void setEth_src(String eth_src) {
		this.eth_src = eth_src;
	}
	public String getIpv4_dst() {
		return ipv4_dst;
	}
	public void setIpv4_dst(String ipv4_dst) {
		this.ipv4_dst = ipv4_dst;
	}
	public String getIpv4_src() {
		return ipv4_src;
	}
	public void setIpv4_src(String ipv4_src) {
		this.ipv4_src = ipv4_src;
	}
	public String getEth_type() {
		return eth_type;
	}
	public void setEth_type(String eth_type) {
		this.eth_type = eth_type;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
}
