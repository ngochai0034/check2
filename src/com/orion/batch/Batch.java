package com.orion.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;

import intellsoft.db.DBUtil;
import intellsoft.db.RecordSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.orion.config.Config;

public class Batch {
	public int InsertBatch(int iProductId, String sBatchName, String sBatchManDate, String sBatchExpDate, String sBatchPromotion, String sBatchWarning, String sBatchDetail, String sBatchManual, String sBatchPrivate, String iUserEmail){
		String sSql = "insert into CHECK2_BATCH "
				+ "(BATCH_ID, BATCH_NAME, BATCH_DETAIL, BATCH_MANUAL, MAN_DATE, EXP_DATE, PRODUCT_ID, CREATED_BY, CREATED_DATE, IS_DELETE, BATCH_WARNING, BATCH_PROMOTION) values "
				+ "(CHECK2_BATCH_SEQ.nextval, '"+sBatchName+"', '"+sBatchDetail+"', '"+sBatchManual+"', TO_DATE('"+sBatchManDate+"', 'yyyy/mm/dd'), TO_DATE('"+sBatchExpDate+"', 'yyyy/mm/dd'),"+iProductId+", '"+iUserEmail+"', SYSDATE,0, '"+sBatchWarning+"', '"+sBatchPromotion+"')";
		String sBatchId = DBUtil.executeUpdateEx(Config.GetDbCheck, sSql, "BATCH_ID");
		if(sBatchId != null){
			return Integer.parseInt(sBatchId);
		}
		return 0;
	}
	public boolean UpdateBatch(int sBathId,String sBatchName, String sBatchDetail, String sBatchManual, String sBatchManDate, String sBatchExpDate,  int iBatchStatus,String iBatchPromotion,String iBatchWarning,String iBatchPrivate){
		String sSql = "update CHECK2_BATCH set "
				+ "BATCH_NAME='"+sBatchName+"', BATCH_DETAIL='"+sBatchDetail+"', BATCH_MANUAL='"+sBatchManual+"', "
				+ " MAN_DATE=TO_DATE('"+sBatchManDate+"', 'yyyy/mm/dd'), "
				+ "EXP_DATE=TO_DATE('"+sBatchExpDate+"', 'yyyy/mm/dd') , BATCH_STATUS="+iBatchStatus+", "
				+ "BATCH_WARNING = '"+iBatchWarning+"' , BATCH_PROMOTION = '"+iBatchPromotion+"' , BATCH_PRIVATE = '"+iBatchPrivate+"' "
				+ "where BATCH_ID="+sBathId+" ";
		int iRes = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
		if(iRes <= 0){
			return false;
		}
//		sSql = "update CHECK2_BATCH set BATCH_STATUS = 0 "
//				+ "WHERE BATCH_STATUS=1 and "
//				+ "PRODUCT_ID in (select PRODUCT_ID from CHECK2_BATCH WHERE BATCH_ID="+sBathId+") and BATCH_ID <> "+BatchId+"";
//		iRes = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
//		if(iRes < 0){
//			return false;
//		}
		return true;
	}
	
	public String GetListOfBatchByProduct(int iProductId){
		String sSql = "select a.* "
				+ "from check2_batch a "
				+ "where a.product_id="+iProductId+" and a.is_delete=0 ";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrBatch = new JSONArray();
		while(rs.next()){
			int iBatchId = 0;
			String sBatchName = "";
			String sBatchManDate = "";
			String sBatchExpDate = "";
			String sBatchDetail = "";
			String sBatchPromotion = "";
			String sBatchWarning = "";
			String sBatchManual = "";
			String sBatchPrivate = "";
			String sCreatedBy = "";	
			String sBatchThumbnail = "";
			if(rs.getInt("BATCH_ID") != 0){iBatchId = rs.getInt("BATCH_ID");} else{iBatchId = 0;}
			if(rs.getString("BATCH_NAME") != null){sBatchName = rs.getString("BATCH_NAME");} else{sBatchName = "";}
			if(rs.getDate("MAN_DATE") != null){sBatchManDate = rs.getDate("MAN_DATE").toString();} else{sBatchManDate = "";}
			if(rs.getDate("EXP_DATE") != null){sBatchExpDate = rs.getDate("EXP_DATE").toString();} else{sBatchExpDate = "";}
			if(rs.getString("BATCH_PROMOTION") != null){sBatchPromotion = rs.getString("BATCH_PROMOTION");} else{sBatchPromotion = "";}
			if(rs.getString("BATCH_WARNING") != null){sBatchWarning = rs.getString("BATCH_WARNING");} else{sBatchWarning = "";}
			if(rs.getString("BATCH_DETAIL") != null){sBatchDetail = rs.getString("BATCH_DETAIL");} else{sBatchDetail = "";}
			if(rs.getString("BATCH_MANUAL") != null){sBatchManual = rs.getString("BATCH_MANUAL");} else{sBatchManual = "";}
			if(rs.getString("BATCH_PRIVATE") != null){sBatchPrivate = rs.getString("BATCH_PRIVATE");} else{sBatchPrivate = "";}
			
			if(rs.getString("CREATED_BY") != null){sCreatedBy = rs.getString("CREATED_BY");} else{sCreatedBy = "";}
			//--- picture ---
			String sMsgFileName = iBatchId + ".txt";
			File f = new File("D://check2/batch/thumbnail/" + sMsgFileName);
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
					sBatchThumbnail += sCurrentLine;
				} 
			} catch (IOException e) {
				sBatchThumbnail = "";
			}
			//-- end of picture ---
			JSONObject jsonobjBatch = new JSONObject();
			try {
				jsonobjBatch.put("bat_id", iBatchId);
				jsonobjBatch.put("bat_name", sBatchName);
				jsonobjBatch.put("bat_mandate", sBatchManDate);
				jsonobjBatch.put("bat_expdate", sBatchExpDate);
				jsonobjBatch.put("bat_promotion", sBatchPromotion);
				jsonobjBatch.put("bat_warning", sBatchWarning);
				jsonobjBatch.put("bat_detail", sBatchDetail);
				jsonobjBatch.put("bat_manual", sBatchManual);
				jsonobjBatch.put("bat_thumbnail", sBatchThumbnail);
				jsonobjBatch.put("bat_private", sBatchPrivate);				
				jsonobjBatch.put("created_by", sCreatedBy);
				
				jsonarrBatch.put(jsonobjBatch);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrBatch.toString();
	}
	public String GetListOfBatchNameByProduct(int iProductId){
		String sSql = "select a.BATCH_ID, a.BATCH_NAME "
				+ "from CHECK2_BATCH a "
				+ "where a.PRODUCT_ID="+iProductId+" and a.IS_DELETE=0 "
				+ "order by BATCH_ID ASC";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrBatch = new JSONArray();
		while(rs.next()){
			int iBatchId = 0;
			String sBatchName = "";
			
			if(rs.getInt("BATCH_ID") != 0){iBatchId = rs.getInt("BATCH_ID");} else{iBatchId = 0;}
			if(rs.getString("BATCH_NAME") != null){sBatchName = rs.getString("BATCH_NAME");} else{sBatchName = "";}
			JSONObject jsonobjBatch = new JSONObject();
			try {
				jsonobjBatch.put("bat_id", iBatchId);
				jsonobjBatch.put("bat_name", sBatchName);
				
				jsonarrBatch.put(jsonobjBatch);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrBatch.toString();
	}
	
	public boolean DeleteBatch(int iBatchId){
		String sSql = "update  CHECK2_BATCH "
				+ "set IS_DELETE = "+1+" "
				+ "where BATCH_ID = "+iBatchId+"";
		int iRes = DBUtil.executeUpdate(Config.GetDbCheck, sSql);		
		if(iRes < 0){
			return false;
		}		
		return true;
	}
	
	public boolean UpdatePicture(int iBatchId, String sBatchPicture){
		try {
			File dirMsgStore = new File("d:\\check2" + "/batch/picture/");
			if(!dirMsgStore.exists()){
				dirMsgStore.mkdirs();
			}
			String sMsgFileName = iBatchId + ".txt";
			File f = new File(dirMsgStore, sMsgFileName);
			//if file does not exists, then create it
			if(!f.exists()){
				f.createNewFile();
			}
			
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sBatchPicture);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			return false;
		}
		return true;	
	}
	public boolean UpdateThumbnail(int iBatchId, String sBatchThumbnail){
		try {
			File dirMsgStore = new File("d:\\check2" + "/batch/thumbnail/");
			if(!dirMsgStore.exists()){
				dirMsgStore.mkdirs();
			}
			String sMsgFileName = iBatchId + ".txt";
			File f = new File(dirMsgStore, sMsgFileName);
			//if file does not exists, then create it
			if(!f.exists()){
				f.createNewFile();
			}
			
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sBatchThumbnail);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			return false;
		}
		return true;	
	}
	
	public String GetBatchById(int iBatchId){
		String sSql = "select a.BATCH_NAME, a.BATCH_DETAIL, a.BATCH_PROMOTION, a.BATCH_WARNING, a.MAN_DATE, a.EXP_DATE, a.BATCH_AMOUNT, "
				+ "nvl(c.ACTIVATED,0) ACTIVATED, nvl(d.BOUGHT,0) BOUGHT, a.BATCH_MODE, BATCH_STATUS, a.CREATED_MEMBERID, b.USR_NAME, b.USR_FULLNAME "
				+ "from EZ_BATCH a, EZ_USERS b, "
				+ "(SELECT count(EZ_ITEM.ITEM_STATUS) ACTIVATED FROM EZ_ITEM WHERE EZ_ITEM.ITEM_STATUS=3 and EZ_ITEM.BATCH_ID="+iBatchId+") c , "
				+ "(SELECT count(EZ_ITEM.ITEM_STATUS) BOUGHT FROM EZ_ITEM WHERE EZ_ITEM.ITEM_STATUS=5 and EZ_ITEM.BATCH_ID="+iBatchId+") d "
				+ "where a.BATCH_ID="+iBatchId+" and a.CREATED_MEMBERID=b.USR_ID ";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);
		
		
		JSONArray jsonarrBatch = new JSONArray();
		JSONObject jsonobjBatch = new JSONObject();
		while(rs.next()){
//			int iBatchId = 0;
			String sBatchName = "";
			String sBatchDetail = "";
			String sBatchPromotion = "";
			String sBatchWarning = "";
			Date dBatchManDate = null;
			Date dBatchExpDate = null;
			int iBatchAmount = 0;
			int iBatchMode = 0;
			int iActivated = 0;
			int iBought = 0;
			int iBatchStatus = 0;
			String sBatchPicture = "";
			
			int iUserId = 0;
			String sUserName = "";
			String sUserFullname = "";
			
			if(rs.getInt("BATCH_ID") != 0){iBatchId = rs.getInt("BATCH_ID");} else{iBatchId = 0;}
			if(rs.getString("BATCH_NAME") != null){sBatchName = rs.getString("BATCH_NAME");} else{sBatchName = "";}
			if(rs.getString("BATCH_DETAIL") != ""){sBatchDetail = rs.getString("BATCH_DETAIL");} else{sBatchDetail = "";}
			if(rs.getString("BATCH_PROMOTION") != ""){sBatchPromotion = rs.getString("BATCH_PROMOTION");} else{sBatchPromotion = "";}
			if(rs.getString("BATCH_WARNING") != ""){sBatchWarning = rs.getString("BATCH_WARNING");} else{sBatchWarning = "";}
			if(rs.getDate("MAN_DATE") != null){dBatchManDate = rs.getDate("MAN_DATE");} else{dBatchManDate = null;}
			if(rs.getDate("EXP_DATE") != null){dBatchExpDate = rs.getDate("EXP_DATE");} else{dBatchExpDate = null;}
			if(rs.getInt("BATCH_AMOUNT") != 0){iBatchAmount = rs.getInt("BATCH_AMOUNT");} else{iBatchAmount = 0;}
			if(rs.getInt("BATCH_MODE") != 0){iBatchMode = rs.getInt("BATCH_MODE");} else{iBatchMode = 0;}
			if(rs.getInt("ACTIVATED") != 0){iActivated = rs.getInt("ACTIVATED");} else{iActivated = 0;}
			if(rs.getInt("BOUGHT") != 0){iBought = rs.getInt("BOUGHT");} else{iBought = 0;}
			if(rs.getInt("BATCH_STATUS") != 0){iBatchStatus = rs.getInt("BATCH_STATUS");} else{iBatchStatus = 0;}
			
			if(rs.getInt("CREATED_MEMBERID") != 0){iUserId = rs.getInt("CREATED_MEMBERID");} else{iUserId = 0;}
			if(rs.getString("USR_NAME") != ""){sUserName = rs.getString("USR_NAME");} else{sUserName = "";}
			if(rs.getString("USR_FULLNAME") != ""){sUserFullname = rs.getString("USR_FULLNAME");} else{sUserFullname = "";}
			//--- picture ---
			File dirMsgStore = new File("D://check2/batch/picture/");
			if(!dirMsgStore.exists()){
				dirMsgStore.mkdirs();
			}
			String sMsgFileName = iBatchId + ".txt";
			File f = new File(dirMsgStore, sMsgFileName);
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					sBatchPicture = "";
				}
			}
			try (BufferedReader br = new BufferedReader(new FileReader(f)))
			{ 
				String sCurrentLine; 
				while ((sCurrentLine = br.readLine()) != null) {
					sBatchPicture += sCurrentLine;
				} 
			} catch (IOException e) {
				sBatchPicture = "";
			}
			//-- end of picture ---
			try {
				jsonobjBatch.put("bat_id", iBatchId);
				jsonobjBatch.put("bat_name", sBatchName);
				jsonobjBatch.put("bat_detail", sBatchDetail);
				jsonobjBatch.put("bat_promotion", sBatchPromotion);
				jsonobjBatch.put("bat_warning", sBatchWarning);
				jsonobjBatch.put("bat_mandate", dBatchManDate);
				jsonobjBatch.put("bat_expdate", dBatchExpDate);
				jsonobjBatch.put("bat_amount", iBatchAmount);
				jsonobjBatch.put("bat_activated", iActivated);
				jsonobjBatch.put("bat_bought", iBought);
				jsonobjBatch.put("bat_mode", iBatchMode);
				jsonobjBatch.put("bat_status", iBatchStatus);
				jsonobjBatch.put("bat_picture", sBatchPicture);
				
				jsonobjBatch.put("usr_id", iUserId);
				jsonobjBatch.put("usr_name", sUserName);
				jsonobjBatch.put("usr_fullname", sUserFullname);
				
//				jsonarrBatch.put(jsonobjBatch);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonobjBatch.toString();
	}
	
	public String GetImageBatch(int iBatchId){
		String sBatchPicture = "";
		File dirMsgStore = new File("D://check2/batch/picture/");
		if(!dirMsgStore.exists()){
			dirMsgStore.mkdirs();
		}
		String sMsgFileName = iBatchId + ".txt";
		File f = new File(dirMsgStore, sMsgFileName);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				sBatchPicture = "";
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f)))
		{ 
			String sCurrentLine; 
			while ((sCurrentLine = br.readLine()) != null) {
				sBatchPicture += sCurrentLine;
			} 
		} catch (IOException e) {
			sBatchPicture = "";
		}		
		return sBatchPicture;
	}
	
	public String GetListOfProductIdAndNameByCompany(int iProductId){
		String sSql = "select a.batch_id,a.batch_name "
				+ "from CHECK2_BATCH a "
				+ "where PRODUCT_ID="+iProductId+" and IS_DELETE=0 ";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);		
		JSONArray jsonarrProduct = new JSONArray();
		while(rs.next()){
			int iBatchId = 0;
			String sBatchName = "";
			
			if(rs.getInt("PRODUCT_ID") != 0){iBatchId = rs.getInt("PRODUCT_ID");} else{iBatchId = 0;}
			if(rs.getString("PRODUCT_NAME") != null){sBatchName = rs.getString("PRODUCT_NAME");} else{sBatchName = "";}

			JSONObject jsonobjProduct = new JSONObject();
			try {
				jsonobjProduct.put("bat_id", iProductId);
				jsonobjProduct.put("bat_name", sBatchName);
			} catch (JSONException e) {
				continue;
			}
		}
		return jsonarrProduct.toString();
	}
	public boolean CheckSmsConfig(int iBatchId){
		String sSql = "select a.BATCH_ID "
				+ "from CHECK2_SMS_CONFIG a "
				+ "WHERE BATCH_ID="+iBatchId+"";
		String sValue = DBUtil.getOneValueString(Config.GetDbCheck, sSql);
		if(sValue == null){
			sSql = "INSERT INTO CHECK2_SMS_CONFIG "
					+ "(BATCH_ID) VALUES "
					+ "("+iBatchId+")";
			int iValue = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
			if(iValue <= 0){
				return false;
			}
			return true;
		}
		return true;		
	}
	public String GetSmsConfig(int iBatchId){
		String sSql = "select a.* "
				+ "from CHECK2_SMS_CONFIG a "
				+ "WHERE BATCH_ID="+iBatchId+"";
		RecordSet rs=DBUtil.executeQuery(Config.GetDbCheck, sSql);
		JSONObject jsonobjBatch = new JSONObject();
		while(rs.next()){
			String sSmsConfig3 = "", sSmsConfig5 = "", sSmsConfigErr = "";
			if(rs.getString("SMS_CONFIG_3") != null) {sSmsConfig3 = rs.getString("SMS_CONFIG_3");}else{sSmsConfig3 = "";}
			if(rs.getString("SMS_CONFIG_5") != null) {sSmsConfig5 = rs.getString("SMS_CONFIG_5");}else{sSmsConfig5 = "";}
			if(rs.getString("SMS_CONFIG_ERR") != null) {sSmsConfigErr = rs.getString("SMS_CONFIG_ERR");}else{sSmsConfigErr = "";}
			try {
				jsonobjBatch.put("sms_config_3", sSmsConfig3);
				jsonobjBatch.put("sms_config_5", sSmsConfig5);
				jsonobjBatch.put("sms_config_err", sSmsConfigErr);
			} catch (JSONException e) {
				continue;
			}
			break;
		}
		return jsonobjBatch.toString();
	}
	public int UpdateSmsConfig(int iBatchId, String sSmsConfig3, String sSmsConfig5, String sSmsConfigErr){
		String sSql = "UPDATE CHECK2_SMS_CONFIG "
				+ "SET SMS_CONFIG_3='"+sSmsConfig3+"', SMS_CONFIG_5='"+sSmsConfig5+"', SMS_CONFIG_ERR='"+sSmsConfigErr+"' "
				+ "WHERE BATCH_ID="+iBatchId+"";
		int iValue = DBUtil.executeUpdate(Config.GetDbCheck, sSql);
		return iValue;
	}
}
