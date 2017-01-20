package com.orion.product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intellsoft.db.DBUtil;
import intellsoft.db.RecordSet;

import com.orion.config.Config;

public class Product {
	public int LogProduct(int iProductId, int iUserId, String sReq){
		String sSql = "insert into EZ_LOG_PRODUCT "
				+ "(ID, PRODUCT_ID, LASTUPDATED_DATE, LASTUPDATED_MEMBERID, LASTUPDATED_MEMBERNAME, DETAIL) values "
				+ "(EZ_LOG_PRODUCT_SEQ.nextval, "+iProductId+", SYSDATE, "+iUserId+", '', '"+sReq+"')";
		String iRes = DBUtil.executeUpdateEx(Config.GetDbCheck, sSql, "ID");
		if(iRes == null){
			return 0;
		}
		return Integer.parseInt(iRes);
	}
	public int InsertProduct(String sProductName, String sProductTag, int iProductType, String sProductDetail , int iCompanyId ,String isEmailUser,int iProductStatus, String sProductManualLink, String sProductDesPrivate){
		String sSql = "insert into CHECK2_PRODUCT "
				+ "(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TAG, PRODUCT_TYPE, PRODUCT_DETAIL,COMPANY_ID,CREATED_DATE,CREATED_BY,PRODUCT_STATUS, PRODUCT_MANUALLINK,PRODUCT_PRIVATE,IS_DELETE) values "
				+ "(CHECK2_PRODUCT_SEQ.nextval, '"+sProductName+"', '"+sProductTag+"', '"+iProductType+"', '"+sProductDetail+"', "+iCompanyId+", SYSDATE,  '"+isEmailUser+"',"+iProductStatus+",'"+sProductManualLink+"','"+sProductDesPrivate+"',0)";
		String iRes = DBUtil.executeUpdateEx(Config.GetDbCheck, sSql, "PRODUCT_ID");
		if(iRes == null){
			return 0;
		}
		return Integer.parseInt(iRes);
	}
	public boolean UpdateProduct(int iProductId, String sProductName, String sProductTag, int iProductType, String sProductDetail ,   String sProductManualLink,int iProductStatus, String sProductDesPrivate){
		String sSql = "update CHECK2_PRODUCT set "
				+ "PRODUCT_NAME='"+sProductName+"', PRODUCT_TAG='"+sProductTag+"', PRODUCT_TYPE="+iProductType+", "
						+ "PRODUCT_DETAIL='"+sProductDetail+"', "
								+ "PRODUCT_MANUALLINK='"+sProductManualLink+"', PRODUCT_STATUS="+iProductStatus+", PRODUCT_PRIVATE='"+sProductDesPrivate+"' "
						+ "where PRODUCT_ID="+iProductId+"";
		int iRes = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
		if(iRes <= 0){
			return false;
		}
		return true;
	}
	
	public String GetListOfProductByCompany(int iCompanyId){
		String sSql = "select a.* "
				+ "from CHECK2_PRODUCT a "
				+ "where COMPANY_ID="+iCompanyId+" and IS_DELETE=0 "
				+ "order by PRODUCT_ID";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iProductId = 0;
			String sProductName = "";
			String sProductTag = "";
			int iProductType = 0;
			String sProductDetail = "";
			int iProductExpDay = 0;
			int iProductStatus = 0;
			String sProductManualLink = "";
			String sProductPicture = "";
			String sProductDesPrivate = "";
			
			int iBatchTotal = 0;
			int iItemTotal = 0;
			int iItemSold = 0;
			
			String iUserEmail = "";
			String sUserName = "";
			String sUserFullname = "";
			
			if(rs.getInt("PRODUCT_ID") != 0){iProductId = rs.getInt("PRODUCT_ID");} else{iProductId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sProductName = rs.getString("PRODUCT_NAME");} else{sProductName = "";}
			if(rs.getString("PRODUCT_TAG") != ""){sProductTag = rs.getString("PRODUCT_TAG");} else{sProductTag = "";}
			if(rs.getInt("PRODUCT_TYPE") != 0){iProductType = rs.getInt("PRODUCT_TYPE");} else{iProductType = 0;}
			if(rs.getString("PRODUCT_DETAIL") != null){sProductDetail = rs.getString("PRODUCT_DETAIL");} else{sProductDetail = "";}
			if(rs.getInt("PRODUCT_EXP_DAY") != 0){iProductExpDay = rs.getInt("PRODUCT_EXP_DAY");} else{iProductExpDay = 0;}
			if(rs.getInt("PRODUCT_STATUS") != 0){iProductStatus = rs.getInt("PRODUCT_STATUS");} else{iProductStatus = 0;}
			if(rs.getString("PRODUCT_MANUALLINK") != null){sProductManualLink = rs.getString("PRODUCT_MANUALLINK");} else{sProductManualLink = "";}
			if(rs.getString("PRODUCT_PRIVATE") != null){sProductDesPrivate = rs.getString("PRODUCT_PRIVATE");} else{sProductDesPrivate = "";}
			
			if(rs.getString("CREATED_BY") != null){iUserEmail = rs.getString("CREATED_BY");} else{iUserEmail = "";}
//			if(rs.getString("USR_NAME") != ""){sUserName = rs.getString("USR_NAME");} else{sUserName = "";}
//			if(rs.getString("USR_FULLNAME") != ""){sUserFullname = rs.getString("USR_FULLNAME");} else{sUserFullname = "";}
			//--- picture ---
			String sMsgFileName = iProductId + ".txt";
			File f = new File("D://check2/product/thumbnail/" + sMsgFileName);
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			try (BufferedReader br = new BufferedReader(new FileReader(f)))
			{ 
				String sCurrentLine; 
				while ((sCurrentLine = br.readLine()) != null) {
					sProductPicture += sCurrentLine;
				} 
			} catch (IOException e) {
				sProductPicture = "";
			}
			//-- end of picture ---
			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("pro_id", iProductId);
				jsonobjProduct.put("pro_name", sProductName);
				jsonobjProduct.put("pro_tag", sProductTag);
				jsonobjProduct.put("pro_type", iProductType);
				jsonobjProduct.put("pro_detail", sProductDetail);
				jsonobjProduct.put("pro_status", iProductStatus);
				jsonobjProduct.put("pro_manuallink", sProductManualLink);
				jsonobjProduct.put("pro_thumbnail", sProductPicture);
				jsonobjProduct.put("pro_description_private", sProductDesPrivate);
				
				jsonobjProduct.put("use_email", iUserEmail);
				
				jsonarrProduct.put(jsonobjProduct);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
// lay danh sach cac lo hang cua san pham
//	public String GetListBatchByProductId(int iProductid){
//		String sSql = "select  a.batch_name , b.amount
//FROM CHECK2_BATCH A, CHECK2_ACTIVE B 
//WHERE A.BATCH_ID = B.BATCH_ID AND A.PRODUCT_ID = "++" ";
//		return "";
//	}
	
	public String GetListOfProductByCompanyEx(int iCompanyId, int iNext){
		String sSql = "select * from"
				+ "(select ROWNUM r, a.PRODUCT_ID, a.PRODUCT_NAME, a.PRODUCT_TAG, a.PRODUCT_TYPE, a.PRODUCT_DETAIL, a.PRODUCT_EXP_DAY, a.PRODUCT_MANUALLINK, "
				+ "a.CREATED_DATE, a.CREATED_MEMBERID, a.PRODUCT_STATUS, b.USR_NAME, b.USR_FULLNAME, "
				+ "(SELECT count(*) FROM EZ_BATCH x where x.PRODUCT_ID=a.PRODUCT_ID) BATCH_TOTAL, "
				+ "(select  COUNT(x.ITEM_ID) from EZ_ITEM x, EZ_BATCH y where x.BATCH_ID=y.BATCH_ID and y.PRODUCT_ID=a.PRODUCT_ID) ITEM_TOTAL, "
				+ "(select  COUNT(x.ITEM_ID) from EZ_ITEM x, EZ_BATCH y where x.ITEM_STATUS<>3 and x.BATCH_ID=y.BATCH_ID and y.PRODUCT_ID=a.PRODUCT_ID) ITEM_SOLD "
				+ "from EZ_PRODUCT a, EZ_USERS b "
				+ "where COMPANY_ID="+iCompanyId+" and a.CREATED_MEMBERID=b.USR_ID "
				+ "order by PRODUCT_ID ) "
				+ "where r between 1+("+iNext+"-1)*3 and "+iNext+"*3";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);
		
		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iProductId = 0;
			String sProductName = "";
			String sProductTag = "";
			int iProductType = 0;
			String sProductDetail = "";
			int iProductExpDay = 0;
			int iProductStatus = 0;
			String sProductManualLink = "";
			String sProductPicture = "";
			String sProductDesPrivate = "";
			
			int iBatchTotal = 0;
			int iItemTotal = 0;
			int iItemSold = 0;
			
			int iUserId = 0;
			String sUserName = "";
			String sUserFullname = "";
			
			if(rs.getInt("PRODUCT_ID") != 0){iProductId = rs.getInt("PRODUCT_ID");} else{iProductId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sProductName = rs.getString("PRODUCT_NAME");} else{sProductName = "";}
			if(rs.getString("PRODUCT_TAG") != ""){sProductTag = rs.getString("PRODUCT_TAG");} else{sProductTag = "";}
			if(rs.getInt("PRODUCT_TYPE") != 0){iProductType = rs.getInt("PRODUCT_TYPE");} else{iProductType = 0;}
			if(rs.getString("PRODUCT_DETAIL") != null){sProductDetail = rs.getString("PRODUCT_DETAIL");} else{sProductDetail = "";}
			if(rs.getInt("PRODUCT_EXP_DAY") != 0){iProductExpDay = rs.getInt("PRODUCT_EXP_DAY");} else{iProductExpDay = 0;}
			if(rs.getInt("PRODUCT_STATUS") != 0){iProductStatus = rs.getInt("PRODUCT_STATUS");} else{iProductStatus = 0;}
			if(rs.getString("PRODUCT_MANUALLINK") != null){sProductManualLink = rs.getString("PRODUCT_MANUALLINK");} else{sProductManualLink = "";}
			if(rs.getString("DESCRIPTION_PRIVATE") != null){sProductDesPrivate = rs.getString("DESCRIPTION_PRIVATE");} else{sProductDesPrivate = "";}
			if(rs.getInt("BATCH_TOTAL") != 0){iBatchTotal = rs.getInt("BATCH_TOTAL");} else{iBatchTotal = 0;}
			if(rs.getInt("ITEM_TOTAL") != 0){iItemTotal = rs.getInt("ITEM_TOTAL");} else{iItemTotal = 0;}
			if(rs.getInt("ITEM_SOLD") != 0){iItemSold = rs.getInt("ITEM_SOLD");} else{iItemSold = 0;}
			
			if(rs.getInt("CREATED_MEMBERID") != 0){iUserId = rs.getInt("CREATED_MEMBERID");} else{iUserId = 0;}
			if(rs.getString("USR_NAME") != ""){sUserName = rs.getString("USR_NAME");} else{sUserName = "";}
			if(rs.getString("USR_FULLNAME") != ""){sUserFullname = rs.getString("USR_FULLNAME");} else{sUserFullname = "";}
			//--- picture ---
			String sMsgFileName = iProductId + ".txt";
			File f = new File("D://ezcheck/product/picture/" + sMsgFileName);
			if(!f.exists()){
				f.mkdirs();
			}
			try (BufferedReader br = new BufferedReader(new FileReader(f)))
			{ 
				String sCurrentLine; 
				while ((sCurrentLine = br.readLine()) != null) {
					sProductPicture += sCurrentLine;
				} 
			} catch (IOException e) {
				sProductPicture = "";
			}
			//-- end of picture ---
			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("pro_id", iProductId);
				jsonobjProduct.put("pro_name", sProductName);
				jsonobjProduct.put("pro_tag", sProductTag);
				jsonobjProduct.put("pro_type", iProductType);
				jsonobjProduct.put("pro_detail", sProductDetail);
				jsonobjProduct.put("exp_day", iProductExpDay);
				jsonobjProduct.put("pro_status", iProductStatus);
				jsonobjProduct.put("pro_manuallink", sProductManualLink);
				jsonobjProduct.put("pro_picture", sProductPicture);
				jsonobjProduct.put("pro_description_private", sProductDesPrivate);
				
				jsonobjProduct.put("pro_batchtotal", iBatchTotal);
				jsonobjProduct.put("pro_itemtotal", iItemTotal);
				jsonobjProduct.put("pro_itemsold", iItemSold);
				
				jsonobjProduct.put("usr_id", iUserId);
				jsonobjProduct.put("usr_name", sUserName);
				jsonobjProduct.put("usr_fullname", sUserFullname);
				
				jsonarrProduct.put(jsonobjProduct);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
	public String GetListOfProductNameByCompany(int iCompanyId){
		String sSql = "SELECT a.PRODUCT_ID, a.PRODUCT_NAME "
				+ "FROM CHECK2_PRODUCT a "
				+ "WHERE a.COMPANY_ID="+iCompanyId+" and a.IS_DELETE=0 "
				+ "ORDER BY a.PRODUCT_ID";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iProductId = 0;
			String sProductName = "";			
			
			if(rs.getInt("PRODUCT_ID") != 0){iProductId = rs.getInt("PRODUCT_ID");} else{iProductId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sProductName = rs.getString("PRODUCT_NAME");} else{sProductName = "";}
			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("pro_id", iProductId);
				jsonobjProduct.put("pro_name", sProductName);						
				jsonarrProduct.put(jsonobjProduct);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
	public String GetListOfProductNameByCompanyEx(int iCompanyId){
		String sSql = "select a.PRODUCT_ID, a.PRODUCT_NAME "
				+ "from EZ_PRODUCT a "
				+ "where COMPANY_ID="+iCompanyId+" "
				+ "order by PRODUCT_ID";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);
		
		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iProductId = 0;
			String sProductName = "";
			if(rs.getInt("PRODUCT_ID") != 0){iProductId = rs.getInt("PRODUCT_ID");} else{iProductId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sProductName = rs.getString("PRODUCT_NAME");} else{sProductName = "";}
			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("pro_id", iProductId);
				jsonobjProduct.put("pro_name", sProductName);						
				jsonarrProduct.put(jsonobjProduct);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
	public boolean DeleteProduct(int iProductId){
		String sSql = "update CHECK2_PRODUCT "
				+ "set is_delete = 1 "
				+ "where product_id = "+iProductId+"";
		int iRes = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
		
		if(iRes < 0){
			return false;
		}		
		return true;
	}
	public boolean UpdatePicture(int iProductId, String sProductPicture){
		
		try {
			File dirMsgStore = new File("d:\\check2" + "/product/picture/");
			if(!dirMsgStore.exists()){
				dirMsgStore.mkdirs();
			}
			String sMsgFileName = iProductId + ".txt";
			File f = new File(dirMsgStore, sMsgFileName);
			//if file does not exists, then create it
			if(!f.exists()){
				f.createNewFile();
			}
			
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sProductPicture);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			return false;
		}
		return true;		
	}
	public boolean UpdatePictureThumbnail(int iProductId, String sProductPicture){
		try {
			File dirMsgStore = new File("d:\\check2" + "/product/thumbnail/");
			if(!dirMsgStore.exists()){
				dirMsgStore.mkdirs();
			}
			String sMsgFileName = iProductId + ".txt";
			File f = new File(dirMsgStore, sMsgFileName);
			//if file does not exists, then create it
			if(!f.exists()){
				f.createNewFile();
			}
			
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sProductPicture);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			return false;
		}
		return true;	
	}
	
	public String GetImagePicture(int iProductId){
		String sProductPicture = "";
		File dirMsgStore = new File("D://check2/product/picture/");
		if(!dirMsgStore.exists()){
			dirMsgStore.mkdirs();
		}
		String sMsgFileName = iProductId + ".txt";
		File f = new File(dirMsgStore, sMsgFileName);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				return sProductPicture;
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f)))
		{ 
			String sCurrentLine; 
			while ((sCurrentLine = br.readLine()) != null) {
				sProductPicture += sCurrentLine;
			} 
		} catch (IOException e) {
			sProductPicture = "";
		}
		
		
		return sProductPicture;
	}
	
	public String GetListOfProductIdAndNameByCompany(int iCompanyId){
		String sSql = "select a.product_id,a.product_name "
				+ "from CHECK2_PRODUCT a "
				+ "where COMPANY_ID="+iCompanyId+" and IS_DELETE=0 "
				+ "order by PRODUCT_ID";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iProductId = 0;
			String sProductName = "";
			
			if(rs.getInt("PRODUCT_ID") != 0){iProductId = rs.getInt("PRODUCT_ID");} else{iProductId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sProductName = rs.getString("PRODUCT_NAME");} else{sProductName = "";}

			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("pro_id", iProductId);
				jsonobjProduct.put("pro_name", sProductName);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
}
