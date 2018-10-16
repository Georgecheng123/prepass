#!/bin/sh

m="m1"
dpid="01" #11
ip="112/123"
ippair11="127"
ippair12="124"
ippair13="123"
ippair21="122"
ippair22="121"
ippair31="120"
ippair32="119"
ippair41="118"
ippair42="117"
ippair51="116"
ippair52="115"
ippair53="114"
ippair61="113"
ippair62="112"
ippair71="111"
ippair72="110"
ippair73="109"
ippair81="108"
ippair82="107"
ippair83="106"

#172.16.104.128
#192.168.1.101
#172.20.10.3
controllerIP="tcp:172.16.104.128:6653"

#本脚本创建一个single,2 结构，并连接OVS，控制器设置为172.17.62.234:6653， out-band模式

#
# h3(eth0)-(eth1)s2(eth2)---(eth2)s1(eth3)---(eth2)s3(eth1)-(eth0)h3
#                               (eth1)
#                                 | 
#                                 h1(eth0)
# 运行完执行 sudo mn -c 清理， 并执行sudo ip netns del h1 h2 

#创建host和switch之间的连接
sudo ip link add "$m"-s1-eth1 type veth peer name "$m"-h1-eth0
sudo ip link add "$m"-s2-eth1 type veth peer name "$m"-h2-eth0
sudo ip link add "$m"-s3-eth1 type veth peer name "$m"-h3-eth0
sudo ip link add "$m"-s4-eth1 type veth peer name "$m"-h4-eth0
sudo ip link add "$m"-s5-eth1 type veth peer name "$m"-h5-eth0
sudo ip link add "$m"-s6-eth1 type veth peer name "$m"-h6-eth0
sudo ip link add "$m"-s7-eth1 type veth peer name "$m"-h7-eth0
sudo ip link add "$m"-s8-eth1 type veth peer name "$m"-h8-eth0

#创建交换机之间的连接
sudo ip link add "$m"-s1-eth2 type veth peer name "$m"-s2-eth2
sudo ip link add "$m"-s1-eth3 type veth peer name "$m"-s8-eth2
sudo ip link add "$m"-s1-eth4 type veth peer name "$m"-s3-eth2
sudo ip link add "$m"-s2-eth3 type veth peer name "$m"-s4-eth2
sudo ip link add "$m"-s3-eth3 type veth peer name "$m"-s5-eth2
sudo ip link add "$m"-s5-eth3 type veth peer name "$m"-s8-eth3
sudo ip link add "$m"-s5-eth4 type veth peer name "$m"-s6-eth2
sudo ip link add "$m"-s6-eth3 type veth peer name "$m"-s7-eth2
sudo ip link add "$m"-s8-eth4 type veth peer name "$m"-s7-eth4
sudo ip link add "$m"-s7-eth3 type veth peer name "$m"-s4-eth3

#创建host1，host2，host3的namespace
#sudo ip -all netns del
sudo ip netns add "$m"-h1
sudo ip netns add "$m"-h2
sudo ip netns add "$m"-h3
sudo ip netns add "$m"-h4
sudo ip netns add "$m"-h5
sudo ip netns add "$m"-h6
sudo ip netns add "$m"-h7
sudo ip netns add "$m"-h8

#将主机的interface放入相应的namespace实现运行隔离
sudo ip link set "$m"-h1-eth0 netns "$m"-h1
sudo ip link set "$m"-h2-eth0 netns "$m"-h2
sudo ip link set "$m"-h3-eth0 netns "$m"-h3
sudo ip link set "$m"-h4-eth0 netns "$m"-h4
sudo ip link set "$m"-h5-eth0 netns "$m"-h5
sudo ip link set "$m"-h6-eth0 netns "$m"-h6
sudo ip link set "$m"-h7-eth0 netns "$m"-h7
sudo ip link set "$m"-h8-eth0 netns "$m"-h8


#在OVS上添加交换机(生成internal端口)
sudo ovs-vsctl add-br "$m"-s1
sudo ovs-vsctl add-br "$m"-s2
sudo ovs-vsctl add-br "$m"-s3
sudo ovs-vsctl add-br "$m"-s4
sudo ovs-vsctl add-br "$m"-s5
sudo ovs-vsctl add-br "$m"-s6
sudo ovs-vsctl add-br "$m"-s7
sudo ovs-vsctl add-br "$m"-s8

#为s1添加eth1，eth2
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth1
#sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth2 -- set interface "$m"-s1-eth2 type=gre option:remote_ip=192.168.1."$ippair22"
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth2 -- set interface "$m"-s1-eth2 option:remote_ip=192.168.1."$ippair11"
#sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth3 -- set interface "$m"-s1-eth3 type=gre option:remote_ip=192.168.1."$ippair32"
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth3 -- set interface "$m"-s1-eth3 option:remote_ip=192.168.1."$ippair12"
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth4 -- set interface "$m"-s1-eth4 option:remote_ip=192.168.1."$ippair13"
#s2
sudo ovs-vsctl add-port "$m"-s2 "$m"-s2-eth1
sudo ovs-vsctl add-port "$m"-s2 "$m"-s2-eth2 -- set interface "$m"-s2-eth2 option:remote_ip=192.168.1."$ippair21"
sudo ovs-vsctl add-port "$m"-s2 "$m"-s2-eth3 -- set interface "$m"-s2-eth3 option:remote_ip=192.168.1."$ippair22"
#s3
sudo ovs-vsctl add-port "$m"-s3 "$m"-s3-eth1
sudo ovs-vsctl add-port "$m"-s3 "$m"-s3-eth2 -- set interface "$m"-s3-eth2 option:remote_ip=192.168.1."$ippair31"
sudo ovs-vsctl add-port "$m"-s3 "$m"-s3-eth3 -- set interface "$m"-s3-eth3 option:remote_ip=192.168.1."$ippair32"
#4
sudo ovs-vsctl add-port "$m"-s4 "$m"-s4-eth1
sudo ovs-vsctl add-port "$m"-s4 "$m"-s4-eth2 -- set interface "$m"-s4-eth2 option:remote_ip=192.168.1."$ippair41"
sudo ovs-vsctl add-port "$m"-s4 "$m"-s4-eth3 -- set interface "$m"-s4-eth3 option:remote_ip=192.168.1."$ippair42"
#5
sudo ovs-vsctl add-port "$m"-s5 "$m"-s5-eth1
sudo ovs-vsctl add-port "$m"-s5 "$m"-s5-eth2 -- set interface "$m"-s5-eth2 option:remote_ip=192.168.1."$ippair51"
sudo ovs-vsctl add-port "$m"-s5 "$m"-s5-eth3 -- set interface "$m"-s5-eth3 option:remote_ip=192.168.1."$ippair52"
sudo ovs-vsctl add-port "$m"-s5 "$m"-s5-eth4 -- set interface "$m"-s5-eth4 option:remote_ip=192.168.1."$ippair53"
#6
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth1
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth2 -- set interface "$m"-s6-eth2 option:remote_ip=192.168.1."$ippair61"
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth3 -- set interface "$m"-s6-eth3 option:remote_ip=192.168.1."$ippair62"
#7
sudo ovs-vsctl add-port "$m"-s7 "$m"-s7-eth1
sudo ovs-vsctl add-port "$m"-s7 "$m"-s7-eth2 -- set interface "$m"-s7-eth2 option:remote_ip=192.168.1."$ippair71"
sudo ovs-vsctl add-port "$m"-s7 "$m"-s7-eth3 -- set interface "$m"-s7-eth3 option:remote_ip=192.168.1."$ippair72"
sudo ovs-vsctl add-port "$m"-s7 "$m"-s7-eth4 -- set interface "$m"-s7-eth4 option:remote_ip=192.168.1."$ippair73"
#8
sudo ovs-vsctl add-port "$m"-s8 "$m"-s8-eth1
sudo ovs-vsctl add-port "$m"-s8 "$m"-s8-eth2 -- set interface "$m"-s8-eth2 option:remote_ip=192.168.1."$ippair81"
sudo ovs-vsctl add-port "$m"-s8 "$m"-s8-eth3 -- set interface "$m"-s8-eth3 option:remote_ip=192.168.1."$ippair82"
sudo ovs-vsctl add-port "$m"-s8 "$m"-s8-eth4 -- set interface "$m"-s8-eth4 option:remote_ip=192.168.1."$ippair83"



#为s1设置控制器
sudo ovs-vsctl set-controller "$m"-s1 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s1 secure

sudo ovs-vsctl set-controller "$m"-s2 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s2 secure
sudo ovs-vsctl set-controller "$m"-s3 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s3 secure
sudo ovs-vsctl set-controller "$m"-s4 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s4 secure
sudo ovs-vsctl set-controller "$m"-s5 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s5 secure
sudo ovs-vsctl set-controller "$m"-s6 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s6 secure
sudo ovs-vsctl set-controller "$m"-s7 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s7 secure
sudo ovs-vsctl set-controller "$m"-s8 "$controllerIP"
sudo ovs-vsctl set-fail-mode "$m"-s8 secure


#为s1修改成out-band模式
sudo ovs-vsctl set bridge "$m"-s1 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s2 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s3 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s4 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s5 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s6 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s7 other_config:disable-in-band=true
sudo ovs-vsctl set bridge "$m"-s8 other_config:disable-in-band=true


#为s1设置datapath-id
sudo ovs-vsctl set bridge "$m"-s1 other_config:datapath-id=0000000000000001
sudo ovs-vsctl set bridge "$m"-s2 other_config:datapath-id=0000000000000002
sudo ovs-vsctl set bridge "$m"-s3 other_config:datapath-id=0000000000000003
sudo ovs-vsctl set bridge "$m"-s4 other_config:datapath-id=0000000000000004
sudo ovs-vsctl set bridge "$m"-s5 other_config:datapath-id=0000000000000005
sudo ovs-vsctl set bridge "$m"-s6 other_config:datapath-id=0000000000000006
sudo ovs-vsctl set bridge "$m"-s7 other_config:datapath-id=0000000000000007
sudo ovs-vsctl set bridge "$m"-s8 other_config:datapath-id=0000000000000008


#为h1，h2，h3配置MAC地址和IP地址，并启用这些网卡接口
sudo ip netns exec "$m"-h1 ifconfig "$m"-h1-eth0 hw ether 00:00:00:00:00:01
sudo ip netns exec "$m"-h1 ip addr add 10.0.0.1/16 dev "$m"-h1-eth0
sudo ip netns exec "$m"-h1 ifconfig "$m"-h1-eth0 up

sudo ip netns exec "$m"-h2 ifconfig "$m"-h2-eth0 hw ether 00:00:00:00:00:02
sudo ip netns exec "$m"-h2 ip addr add 10.0.0.2/16 dev "$m"-h2-eth0
sudo ip netns exec "$m"-h2 ifconfig "$m"-h2-eth0 up

sudo ip netns exec "$m"-h3 ifconfig "$m"-h3-eth0 hw ether 00:00:00:00:00:03
sudo ip netns exec "$m"-h3 ip addr add 10.0.0.3/16 dev "$m"-h3-eth0
sudo ip netns exec "$m"-h3 ifconfig "$m"-h3-eth0 up

sudo ip netns exec "$m"-h4 ifconfig "$m"-h4-eth0 hw ether 00:00:00:00:00:04
sudo ip netns exec "$m"-h4 ip addr add 10.0.0.4/16 dev "$m"-h4-eth0
sudo ip netns exec "$m"-h4 ifconfig "$m"-h4-eth0 up

sudo ip netns exec "$m"-h5 ifconfig "$m"-h5-eth0 hw ether 00:00:00:00:00:05
sudo ip netns exec "$m"-h5 ip addr add 10.0.0.5/16 dev "$m"-h5-eth0
sudo ip netns exec "$m"-h5 ifconfig "$m"-h5-eth0 up

sudo ip netns exec "$m"-h6 ifconfig "$m"-h6-eth0 hw ether 00:00:00:00:00:06
sudo ip netns exec "$m"-h6 ip addr add 10.0.0.6/16 dev "$m"-h6-eth0
sudo ip netns exec "$m"-h6 ifconfig "$m"-h6-eth0 up

sudo ip netns exec "$m"-h8 ifconfig "$m"-h8-eth0 hw ether 00:00:00:00:00:08
sudo ip netns exec "$m"-h8 ip addr add 10.0.0.8/16 dev "$m"-h8-eth0
sudo ip netns exec "$m"-h8 ifconfig "$m"-h8-eth0 up

sudo ip netns exec "$m"-h7 ifconfig "$m"-h7-eth0 hw ether 00:00:00:00:00:07
sudo ip netns exec "$m"-h7 ip addr add 10.0.0.7/16 dev "$m"-h7-eth0
sudo ip netns exec "$m"-h7 ifconfig "$m"-h7-eth0 up



#启用所有交换机的接口
sudo ifconfig "$m"-s1-eth1 up
sudo ifconfig "$m"-s1-eth2 up
sudo ifconfig "$m"-s1-eth3 up
sudo ifconfig "$m"-s1-eth4 up

sudo ifconfig "$m"-s2-eth1 up
sudo ifconfig "$m"-s2-eth2 up
sudo ifconfig "$m"-s2-eth3 up

sudo ifconfig "$m"-s3-eth1 up
sudo ifconfig "$m"-s3-eth2 up
sudo ifconfig "$m"-s3-eth3 up

sudo ifconfig "$m"-s4-eth1 up
sudo ifconfig "$m"-s4-eth2 up
sudo ifconfig "$m"-s4-eth3 up

sudo ifconfig "$m"-s5-eth1 up
sudo ifconfig "$m"-s5-eth2 up
sudo ifconfig "$m"-s5-eth3 up
sudo ifconfig "$m"-s5-eth4 up

sudo ifconfig "$m"-s6-eth1 up
sudo ifconfig "$m"-s6-eth2 up
sudo ifconfig "$m"-s6-eth3 up

sudo ifconfig "$m"-s7-eth1 up
sudo ifconfig "$m"-s7-eth2 up
sudo ifconfig "$m"-s7-eth3 up
sudo ifconfig "$m"-s7-eth4 up

sudo ifconfig "$m"-s8-eth1 up
sudo ifconfig "$m"-s8-eth2 up
sudo ifconfig "$m"-s8-eth3 up
sudo ifconfig "$m"-s8-eth4 up


#为交换机管理口配置IP地址
#sudo ifconfig "$m"-s1 172.16.62."$ip" netmask 255.255.255.0  

#为交换机配置GRE通道端口
:<<!
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth2 -- set interface "$m"-s1-eth2 type=gre option:remote_ip=172.16.62."$ippair31"
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth3 -- set interface "$m"-s1-eth3 type=gre option:remote_ip=172.16.62."$ippair41"
sudo ovs-vsctl add-port "$m"-s1 "$m"-s1-eth5 -- set interface "$m"-s1-eth5 type=gre option:remote_ip=172.16.62."$ippair82"
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth2 -- set interface "$m"-s6-eth2 type=gre option:remote_ip=172.16.62."$ippair34"
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth3 -- set interface "$m"-s6-eth3 type=gre option:remote_ip=172.16.62."$ippair54"
sudo ovs-vsctl add-port "$m"-s6 "$m"-s6-eth6 -- set interface "$m"-s6-eth6 type=gre option:remote_ip=172.16.62."$ippair84"
!

#启动三个xterm，用于测试
sudo ip netns exec "$m"-h1 xterm &
sudo ip netns exec "$m"-h2 xterm &
sudo ip netns exec "$m"-h3 xterm &
sudo ip netns exec "$m"-h4 xterm &
sudo ip netns exec "$m"-h5 xterm &
sudo ip netns exec "$m"-h6 xterm &
sudo ip netns exec "$m"-h7 xterm &
sudo ip netns exec "$m"-h8 xterm &


