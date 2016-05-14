package com.sogou.IMEStabilyTest.ProbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sogou.IMEStabilyTest.ProbManager.*;

import java.lang.Class;                
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;        
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ExecCoreManager {
	private ProbManager m_probmanager;
	private ArrayList<String> m_listActionList;
	private int m_iDoneActionIndex;
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
		this.m_iDoneActionIndex = 0;
		this.m_lInterval = 0;
		this.m_lStartTime = 0;
		this.m_execthread = new ExecThread();
	}
	
	public List<String> GenerateActionList(int iCount){
		if(iCount <= 0){
			iCount = 1;
		}
		this.m_probmanager.InitFuncs();
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
	
	public Boolean SaveActionList( String strPath){
		
		return false;
	}
	
	
	public Boolean LoadActionList( String strPath){
		
		return false;
	}
	
	public String DoAction(){
		String ActionName = null;
		if( m_iDoneActionIndex >= this.m_listActionList.size()){
			//list中的动作已经执行完了,重头执行
			m_iDoneActionIndex = 0;
		}
		
		ActionName = this.m_listActionList.get(this.m_iDoneActionIndex);
		//TODO:添加调用其他class方法的过程
		String BASE_OBJECT_PATH = "com.sogou.IMEStabilyTest.util.ActionTest";
		
		try {
			Class<?> bClazz = Class.forName(BASE_OBJECT_PATH);
			Object bObj = bClazz.newInstance();//父类实例
			Method bMethod;
			try {
				bMethod = bClazz.getDeclaredMethod("Dotype");
				bMethod.invoke(bObj);//父类的bMethod调用父类的getObjectName()
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
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		this.m_iDoneActionIndex++;
		
		return ActionName;
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
			m_lStartTime = System.currentTimeMillis();
			while (true) {
				System.out.println("Running...");
				System.out.println(DoAction());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long currentTime = System.currentTimeMillis();
				if ((currentTime - m_lStartTime) >= m_lInterval) {
					System.out.println("执行时间到，退出！");
					break;
				}
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
		m_execthread.stop();
	}
}
