package com.sogou.IMEStabilyTest.ProbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sogou.IMEStabilyTest.ProbManager.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Class;                
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;        
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ExecCoreManager {
	private ProbManager m_probmanager;
	private ArrayList<String> m_listActionList;
	private ArrayList<String> m_listPrepareActionList;
	private HashMap<String , Object> m_mapReflict;
	private int m_iDoneActionIndex;
	private int m_iCountActionDone;
	private String m_strPathSrcAction;
	private String m_strPathTemp;
	private String m_strPathLog;
	private long m_lInterval;
	private long m_lStartTime;
	private final long SECONDS = 60*1000;
	private ExecThread m_execthread;
	
	
	public ExecCoreManager() {
		this.m_probmanager = new ProbManager();
		this.m_listActionList = new ArrayList<String>();
		this.m_listPrepareActionList = new ArrayList<String>();
		this.m_mapReflict = new HashMap<String , Object>();
		this.m_iDoneActionIndex = 0;
		this.m_iCountActionDone = 0;
		this.m_lInterval = 0;
		this.m_lStartTime = 0;
		this.m_strPathSrcAction = null;
		this.m_strPathTemp = null;
		this.m_strPathLog = null;
		this.m_execthread = new ExecThread();

	}
	
	public ExecCoreManager(String strPathSrcAction) {
		this.m_probmanager = new ProbManager();
		this.m_listActionList = new ArrayList<String>();
		this.m_listPrepareActionList = new ArrayList<String>();
		this.m_iDoneActionIndex = 0;
		this.m_iCountActionDone = 0;
		this.m_lInterval = 0;
		this.m_lStartTime = 0;
		this.m_strPathSrcAction = strPathSrcAction;
		this.m_strPathTemp = null;
		this.m_strPathLog = null;
		this.m_execthread = new ExecThread();
	}
	
	public ExecCoreManager(String strPathSrcAction , String strPathTemp) {
		this.m_probmanager = new ProbManager();
		this.m_listActionList = new ArrayList<String>();
		this.m_listPrepareActionList = new ArrayList<String>();
		this.m_iDoneActionIndex = 0;
		this.m_iCountActionDone = 0;
		this.m_lInterval = 0;
		this.m_lStartTime = 0;
		this.m_strPathSrcAction = strPathSrcAction;
		this.m_strPathTemp = strPathTemp;
		this.m_strPathLog = null;
		this.m_execthread = new ExecThread();
	}
	
	public ExecCoreManager(String strPathSrcAction , String strPathTemp , String strPathLog) {
		this.m_probmanager = new ProbManager();
		this.m_listActionList = new ArrayList<String>();
		this.m_listPrepareActionList = new ArrayList<String>();
		this.m_iDoneActionIndex = 0;
		this.m_iCountActionDone = 0;
		this.m_lInterval = 0;
		this.m_lStartTime = 0;
		this.m_strPathSrcAction = strPathSrcAction;
		this.m_strPathTemp = strPathTemp;
		this.m_strPathLog = strPathLog;
		this.m_execthread = new ExecThread();
	}
	
	public void SetSrcActionPath( String strPath){
		this.m_strPathSrcAction = strPath;
	}
	
	public void SetLogPath( String strPath){
		this.m_strPathLog = strPath;
	}
	
	public void SetTempPath( String strPath){
		this.m_strPathTemp = strPath;
	}
	
	public void AddAction(String strActionName , int iProbability ){
		this.m_probmanager.AddAction(strActionName, iProbability);	
	}	
	
	public void AddPrepareAction(String strActionName ){
		m_listPrepareActionList.add( strActionName );
	}	
	
	public List<String> GenerateActionList(int iCount){
		if(iCount <= 0){
			iCount = 1;
		}
		this.m_probmanager.GenerateProbilityDistribution();
		
		for(int i = 0; i< iCount -1; i++){
			m_listActionList.add(m_probmanager.GetNextAction());
		}
		return m_listActionList;
	}
	
	public HashMap<String , Integer> SummaryDistribution(){
		String name = new String();
		HashMap<String , Integer> mapRandom = new HashMap<String , Integer>();
		for(int i = 0 ; i < m_listActionList.size(); i++ ){
			name = m_listActionList.get(i);
			if(mapRandom.containsKey(name)){
				int ivalue = mapRandom.get(name);
				ivalue += 1;
				mapRandom.replace(name, ivalue);
			}else{
				mapRandom.put(name, 1);
			}
		}
		return mapRandom;
	}
	
	public void SetInterval( int iMiniute ){
		this.m_lInterval = iMiniute * SECONDS;
	}
		
	private String DoPrepare(int iPrepareActionIndex){
		String strActionName = null;
		String strAction = null;
		String strPackageName = null;		
		strAction = this.m_listPrepareActionList.get(iPrepareActionIndex);
				
		int beginIndex =strAction.lastIndexOf(".");
		strActionName = strAction.substring( beginIndex + 1, strAction.length());
		strPackageName = strAction.substring(0, beginIndex);
		
		try {
			Method bMethod;
			Object bObj;
			if( m_mapReflict.containsKey(strPackageName)){
				Class<?> bClazz = Class.forName(strPackageName);
				bObj = m_mapReflict.get(strPackageName);
				bMethod = bClazz.getDeclaredMethod(strActionName);
			}else{
				Class<?> bClazz = Class.forName(strPackageName);
				bObj = bClazz.newInstance();//父类实例
				bMethod = bClazz.getDeclaredMethod(strActionName);
				m_mapReflict.put(strPackageName, bObj);
			}
			bMethod.invoke(bObj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		return strAction;
	}
	
	public String DoAction(){
		String strActionName = null;
		String strAction = null;
		String strPackageName = null;
		//list中的动作已经执行完了,重头执行
		if( m_iDoneActionIndex >= this.m_listActionList.size()){
			m_iDoneActionIndex = 0;
		}
		
		strAction = this.m_listActionList.get(this.m_iDoneActionIndex);
		
		int beginIndex =strAction.lastIndexOf(".");
		strActionName = strAction.substring( beginIndex + 1, strAction.length());
		strPackageName = strAction.substring(0, beginIndex);
		
		//String BASE_OBJECT_PATH = "com.sogou.IMEStabilyTest.util.ActionTest";
		
		try {
			Method bMethod;
			Object bObj;
			if( m_mapReflict.containsKey(strPackageName)){
				Class<?> bClazz = Class.forName(strPackageName);
				bObj = m_mapReflict.get(strPackageName);
				bMethod = bClazz.getDeclaredMethod(strActionName);
			}else{
				Class<?> bClazz = Class.forName(strPackageName);
				bObj = bClazz.newInstance();//父类实例
				bMethod = bClazz.getDeclaredMethod(strActionName);
				m_mapReflict.put(strPackageName, bObj);
			}
			bMethod.invoke(bObj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		this.m_iDoneActionIndex++;
		
		return strAction;
	}
	
	class ExecThread implements Runnable {
		Thread t;
		private AtomicBoolean m_bFlagRun = new AtomicBoolean();
		ExecThread() {
			t = new Thread(this, "ExecAction Thread");
			System.out.println("Thread: " + t);
			this.m_bFlagRun.set(false);
		}
		public void run() {
			//1.执行前置操作
			for(int i = 0; i< m_listPrepareActionList.size(); i++ ){
				DoPrepare(i);
			}
			
			//2.循环执行随机动作
			m_lStartTime = System.currentTimeMillis();
			while (true) {
				//System.out.println("Running...");
				//System.out.println(DoAction());
				DoAction();
				
				//3.定时记录执行的动作个数
				m_iCountActionDone++;
				if( (m_iCountActionDone % 5) == 0 ){
					SaveActionIndex();
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//4.检查执行时间是否已经到了
				long currentTime = System.currentTimeMillis();
				if ((currentTime - m_lStartTime) >= m_lInterval) {
					System.out.println("执行时间到，退出！");
					break;
				}
				
				//5.检查执行停止是否被设置
				if (!this.m_bFlagRun.get()) {
					System.out.println("执行被停止，退出！");
					break;
				}
			}
			System.out.println("Exiting thread.");
		}
		public void start() {
			t.start();
			this.m_bFlagRun.set(true);
		}
		public void stop() {
			this.m_bFlagRun.set(false);
		}
	}
	
	public void RunAction(){
		m_execthread.start();
	}
	
	public void StopAction(){
		SaveActionIndex();
		m_execthread.stop();
	}
	
	public Boolean SaveActionList( ){
		if( this.m_strPathSrcAction == null){
			return false;
		}
		File file = new File(this.m_strPathSrcAction);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			for (int i = 0; i < this.m_listActionList.size(); i++) {
				writer.write(m_listActionList.get(i).toString());
				writer.newLine();// 换行
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	
	public Boolean LoadActionList( ){
		if( this.m_strPathSrcAction == null){
			return false;
		}
		File file = new File(this.m_strPathSrcAction);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				line++;
				this.m_listActionList.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (reader != null) {
				try {
					reader.close();
					return false;
				} catch (IOException e1) {
				}
			}
		}

		return true;
	}
	
	public Boolean SaveActionIndex( ){
		if( this.m_strPathTemp == null){
			return false;
		}
		File file = new File(this.m_strPathTemp);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			writer.write(this.m_iDoneActionIndex);
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	
	public Boolean LoadActionIndex( ){
		if( this.m_strPathTemp == null){
			return false;
		}
		File file = new File(this.m_strPathTemp);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			tempString = reader.readLine();
			this.m_iDoneActionIndex = Integer.parseInt(tempString);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (reader != null) {
				try {
					reader.close();
					return false;
				} catch (IOException e1) {
				}
			}
		}
		return true;
	}
	

}
