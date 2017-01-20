package com.orion.product;

import intellsoft.db.DBUtil;

import com.orion.config.Config;

public class User {
	public String GetUserEmailBySession(String sSession){
		String sSql = "SELECT USER_EMAIL FROM CHECK2_USER WHERE USER_SESSION='"+sSession+"'";
		String sValue = DBUtil.getOneValueString(Config.GetDbCheck, sSql);
		return sValue;
	}
	
	//-------------
	public int GetMemberLevelInCompanyByUserEmail(String iUserEmail, int iCompanyId){
//		System.out.println("where MEMBER_EMAIL="+iUserEmail+" and COMPANY_ID="+iCompanyId+"");
		String sSql = "select MEMBER_LEVEL "
				+ "from CHECK2_COMPANY_MEMBER "
				+ "where MEMBER_EMAIL='"+iUserEmail+"' and COMPANY_ID="+iCompanyId+"";
		String rt=DBUtil.getOneValueString(Config.GetDbCheck, sSql);
		if(rt == null){
			return 0;
		}
		System.out.println(rt);
		return Integer.parseInt(rt);
	}
	public int GetMemberTypeInCompanyByMemberId(int iMemberId){
		String sSql = "select MEMBER_TYPE "
				+ "from EZ_COMPANY_MEMBER "
				+ "where COMPANY_MEMBER_ID="+iMemberId+"";
		String rt=DBUtil.getOneValueString(Config.GetDbCheck, sSql);
		if(rt == null){
			return 0;
		}
		System.out.println(rt);
		return Integer.parseInt(rt);
	}
}
