package com.sogou.IMEStabilyTest.ProbManager;

import java.util.HashMap;
import java.util.Iterator;

import com.sogou.IMEStabilyTest.ProbManager.*;

public class MainTest {

	public static void main(String[] args) {
		ExecCoreManager execcoremanager = new ExecCoreManager();
//		execcoremanager.AddAction("Dotype" , 10 );
//		execcoremanager.AddAction("DoChangeskin" , 20 );
//		execcoremanager.AddAction("DoInstallskin" , 30 );
//		execcoremanager.AddAction("DoHideKeyboard" , 40 );
//		execcoremanager.AddAction("DoShowKeyboard" , 50 );
//		execcoremanager.AddAction("DoCoverInstallIME" , 60 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.Dotype" , 10 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.DoChangeskin" , 20 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.DoInstallskin" , 30 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.DoHideKeyboard" , 40 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.DoShowKeyboard" , 50 );
		execcoremanager.AddAction("com.sogou.IMEStabilyTest.util.ActionTest.DoCoverInstallIME" , 60 );
		execcoremanager.GenerateActionList(50);
		execcoremanager.SetSrcActionPath("E:\\action.txt");
		execcoremanager.SetInterval(2);
		execcoremanager.RunAction();

//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		execcoremanager.StopAction();
	}

}
