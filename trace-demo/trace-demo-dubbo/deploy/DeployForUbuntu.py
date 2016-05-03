#!/usr/bin/python
# -*- coding: utf-8 -*-
import ConfigParser,paramiko,datetime,os,sys,string,fileinput

class Deploy:

	@staticmethod
	def upload():
		cf = ConfigParser.ConfigParser() 
		cf.read("./deploy.properties") 
		s = cf.sections() 
		connParas = cf.items('connParas')
		ip = cf.get("connParas", "ip")
		username = cf.get("connParas", "username")
		password = cf.get("connParas", "password")
		port=22
		#本地路径
		l_dir='../target/'
		#远程路径
		r_dir='/data/Downloads/'
            
		#打印分割符
		print('    Server : '+ip)

		#建立连接
		t=paramiko.Transport((ip,int(port)))
		t.connect(username=username,password=password)
		sftp=paramiko.SFTPClient.from_transport(t)
    
		#分别上传文件
		files=os.listdir(l_dir)
		for f in files:
			#本地路径+文件名
			l_file=os.path.join(l_dir,f)
			sufix = l_file[l_file.rfind('.'):]
			if('.war' != sufix):
				continue
			#远程路径+文件名
			r_file=os.path.join(r_dir,f)
			print('        '+l_file+' ---> '+r_file)
			#上传
			sftp.put(l_file,r_file)
			break
		t.close() 
		
	@staticmethod
	def exeScript():
		cf = ConfigParser.ConfigParser() 
		cf.read("./deploy.properties") 
		s = cf.sections() 
		connParas = cf.items('connParas')
		ip = cf.get("connParas", "ip")
		username = cf.get("connParas", "username")
		password = cf.get("connParas", "password")

		#建立连接
		s=paramiko.SSHClient()
		s.set_missing_host_key_policy(paramiko.AutoAddPolicy())
		s.connect(hostname = ip,username = username, password = password)

		cmdList = cf.items('cmdList')
		for commond in cmdList:
			stdin,stdout,stderr=s.exec_command(commond[1])
			print stdout.read()
		#关闭连接
		s.close()

Deploy.upload()
Deploy.exeScript()

