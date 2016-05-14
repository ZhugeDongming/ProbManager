package com.sogou.IMEStabilyTest.ProbManager;

import java.util.HashMap;
import java.util.Iterator;

import com.sogou.IMEStabilyTest.ProbManager.*;

public class MainTest {

	public static void main(String[] args) {
		ExecCoreManager execcoremanager = new ExecCoreManager();
		execcoremanager.GenerateActionList(50);
		execcoremanager.SetInterval(2);
		execcoremanager.RunAction();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execcoremanager.StopAction();
	}

}
