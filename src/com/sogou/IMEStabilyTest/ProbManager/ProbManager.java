/**
 * 
 */
/**
 * @author Dongming
 *
 */

package com.sogou.IMEStabilyTest.ProbManager;

import java.util.*;

/**
 * @author Dongming
 *
 */
public class ProbManager{
	//private Vector m_vParam;
	@SuppressWarnings("rawtypes")
	private Map<String , Vector> m_mapFuncandParam;
	@SuppressWarnings("rawtypes")
	private Map<String , Vector> m_mapProb;
	private int m_iTotalProbility;
	
	@SuppressWarnings("unchecked")
	public ProbManager() {
		//super();
		//this.m_vParam = new Vector();
		this.m_mapFuncandParam = new HashMap <String , Vector>();
		this.m_mapProb = new HashMap <String , Vector>();
		this.m_iTotalProbility = 0;
	}
	
	public void InitFuncs(){
		this.AddAction("打字" , 10 );
		this.AddAction("换皮肤" , 20 );
		this.AddAction("安装皮肤" , 30 );
		this.AddAction("退出键盘" , 40 );
		this.AddAction("调起键盘" , 50 );
		this.AddAction("覆盖安装" , 60 );
	}
	
	public void AddAction(String strActionName , int iProbability ){
		Vector vporbability = new Vector();
		vporbability.add(iProbability);
		this.m_mapFuncandParam.put(strActionName, vporbability);
	}
	
	public void GenerateProbilityDistribution(){
		int iStart = 0;
		int iEnd = 0;
		Iterator iter = this.m_mapFuncandParam.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			Vector val = (Vector)entry.getValue();
			iEnd = iStart + (int) val.get(0);
			Vector prob = new Vector();
			prob.add(iStart);
			prob.add(iEnd);
			this.m_mapProb.put(key, prob);
			iStart = iEnd;
			this.m_iTotalProbility = iStart;
		}
	}
	
	private int GetRandomAction(){
		return (int)(0+Math.random()*((this.m_iTotalProbility-1)-0+1));
	}
	
	private String GetFuncNameByProbility( int iProb ){
		Iterator iter = this.m_mapProb.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			Vector val = (Vector)entry.getValue();
			
			if (iProb >= (int)val.get(0) & iProb < (int)val.get(1)){
				return key;
			}	
		}
		return "";
	}
	
	public String GetNextAction(){
		return this.GetFuncNameByProbility(this.GetRandomAction());
	}
}