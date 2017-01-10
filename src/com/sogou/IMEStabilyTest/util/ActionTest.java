package com.sogou.IMEStabilyTest.util;



public class ActionTest {
	private static ActionTest instance;
	private static boolean flag = false;
	
	public ActionTest (){ 
		System.out.println("ActionTest 构造函数执行");
	}
	
	public void Dotype(){
		System.out.println("Dotype");
	}
	public void DoChangeskin(){
		System.out.println("DoChangeskin");
	}
	public void DoInstallskin(){
		System.out.println("DoInstallskin");
	}
	public void DoHideKeyboard(){
		System.out.println("DoHideKeyboard");
	}
	public void DoShowKeyboard(){
		System.out.println("DoShowKeyboard");
	}
	public void DoCoverInstallIME(){
		System.out.println("DoCoverInstallIME");
	}
}
