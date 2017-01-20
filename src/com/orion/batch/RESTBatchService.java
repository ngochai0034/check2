package com.orion.batch;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

import com.orion.product.User;

@Path("/batch")
public class RESTBatchService {
	@Path("/insert")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String InsertBatchByCompanyMemberApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;		
		int iProductId = 0;
		String sBatchName = "";
		String sBatchManDate = "";
		String sBatchExpDate = "";
		String sBatchDetail = "";
		String sBatchPromotion = "";
		String sBatchWarning = "";
		String sBatchManual = "";
		String sBatchPrivate = "";
		int iBatchMode = 0;
		int iBatchStatus = 0;
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");			
			iProductId = jsonobjReq.getInt("pro_id");
			sBatchName = jsonobjReq.getString("bat_name");
			sBatchManDate = jsonobjReq.getString("bat_mandate");
			sBatchExpDate = jsonobjReq.getString("bat_expdate");
			sBatchPromotion = jsonobjReq.getString("bat_promotion");
			sBatchWarning = jsonobjReq.getString("bat_warning");
			sBatchDetail = jsonobjReq.getString("bat_detail");
			sBatchManual = jsonobjReq.getString("bat_manual");
			if(sReq.contains("pro_description_private")){
				sBatchPrivate = jsonobjReq.getString("bat_private");				
			}
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		int iBatchId = batch.InsertBatch(iProductId, sBatchName, sBatchManDate, sBatchExpDate, sBatchPromotion, sBatchWarning, sBatchDetail, sBatchManual, sBatchPrivate, sUserEmail);
		if(iBatchId == 0){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"bat_id\":" + iBatchId + "}";
	}
	@Path("/update")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String UpdateBatchByCompanyMemberApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		int iBatchId = 0;
		String sBatchName = "";
		String sBatchDetail = "";
		String sBatchManual = "";
		
		int iBatchStatus = 0;
		String sBatchManDate = "";
		String sBatchExpDate = "";
		String sBatchDesPrivate = "";
		String sBatchPromotion = "";
		String sBatchPrivate = "";
		String sBatchWarning = "";
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			
			iBatchId = jsonobjReq.getInt("bat_id");
			sBatchName = jsonobjReq.getString("bat_name");
			sBatchDetail = jsonobjReq.getString("bat_detail");
			sBatchManual = jsonobjReq.getString("bat_manual");
			
			iBatchStatus = jsonobjReq.getInt("bat_status");
			sBatchManDate = jsonobjReq.getString("bat_mandate");
			sBatchExpDate = jsonobjReq.getString("bat_expdate");
			
			sBatchPromotion = jsonobjReq.getString("bat_promotion");
			sBatchPrivate = jsonobjReq.getString("bat_private");
			sBatchWarning = jsonobjReq.getString("bat_warning");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		if(batch.UpdateBatch(iBatchId, sBatchName, sBatchDetail, sBatchManual, sBatchManDate, sBatchExpDate, iBatchStatus,sBatchPromotion,sBatchWarning,sBatchPrivate) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	
	//---------------------------------------
	@Path("/getlistbyproduct")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetListOfBatchByProductApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		int iProductId = 0;
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
			iProductId = jsonobjReq.getInt("pro_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		String sBatch = batch.GetListOfBatchByProduct(iProductId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"batch\":" + sBatch + "}";
	}
	

	
	@Path("/getlistofbatchnamebyproduct")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetListOfBatchNameByProductApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		int iProductId = 0;
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
			iProductId = jsonobjReq.getInt("pro_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		String sBatch = batch.GetListOfBatchNameByProduct(iProductId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"batch\":" + sBatch + "}";
	}
	//----------------------------------------
	@Path("/delete")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String DeleteBatchByCompanyMemberApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		int iBatchId = 0;		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			iBatchId = jsonobjReq.getInt("bat_id");			
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		if(batch.DeleteBatch(iBatchId) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	
	//---
	@Path("/updatepicture")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String UpdateBatchPictureApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		int iBatchId = 0;
		String sBatchPicture = "";
		String sBatchThumbnail = "";
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
			
			iBatchId = jsonobjReq.getInt("bat_id");
			sBatchPicture = jsonobjReq.getString("bat_picture");
			sBatchThumbnail = jsonobjReq.getString("bat_thumbnail");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		if(batch.UpdatePicture(iBatchId, sBatchPicture) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Loi luu picture" + "\"}";
		}
		if(batch.UpdateThumbnail(iBatchId, sBatchThumbnail) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Loi luu thubnail" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	@Path("/getbatchbyid")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetBatchByIdApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		int iBatchId = 0;
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
			iBatchId = jsonobjReq.getInt("bat_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		String sBatch = batch.GetBatchById(iBatchId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"batch\":" + sBatch + "}";
	}
	@Path("/getimagebatchbyid")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetImageBatchById(String sReq){
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		int iBatchId = 0;		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			iBatchId = jsonobjReq.getInt("bat_id");			
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		String a = batch.GetImageBatch(iBatchId);
		System.out.println(a);
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\", \"bat_image\":\"" + a + "\"}";
	}
	
	@Path("/getlistbatchnamebycompanyid")
	@POST
	@Produces("applicatiion/json; charset=UTF-8")
	public String GetProductNameByCompanyId(String sReq){
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int sCompanyId = 0;
		int sProductId = 0;
		try {
			JSONObject jsonObject = new JSONObject(sReq);
			sSession = jsonObject.getString("use_session");
			sCompanyId = jsonObject.getInt("com_id");
			sProductId = jsonObject.getInt("pro_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		int sUserLevel = user.GetMemberLevelInCompanyByUserEmail(sUserEmail, sCompanyId);
		if(sUserLevel>3){
			return "{\"code\":" + 410 + ", \"description\":\"" + "Người dùng không có quyền" + "\"}";
		}
		Batch batch = new Batch();
		String a = batch.GetListOfBatchNameByProduct(sProductId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\", \"batch\":" + a + "}";
	}
	@Path("/getsmsconfig")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetSmsConfigApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0, iBatchId = 0;
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			iBatchId = jsonobjReq.getInt("bat_id");
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"code\":" + 800 + ", \"desc\":\"" + "JSON fail" + "\"}";
		}
		User user = new User();
		String sEmail = user.GetUserEmailBySession(sSession);
		if(sEmail == null){
			return "{\"code\":" + 700 + ", \"desc\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		batch.CheckSmsConfig(iBatchId);
		String sValue = batch.GetSmsConfig(iBatchId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\", \"sms\":" + sValue + "}";
	}
	@Path("/updatesmsconfig")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String UpdateSmsConfigApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = ""; 
		String sSmsConfig3 = "", sSmsConfig5 = "", sSmsConfigErr = "";
		int iCompanyId = 0, iBatchId = 0;
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			iBatchId = jsonobjReq.getInt("bat_id");
			sSmsConfig3 = jsonobjReq.getString("sms_config_3");
			sSmsConfig5 = jsonobjReq.getString("sms_config_5");
			sSmsConfigErr = jsonobjReq.getString("sms_config_err");
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"code\":" + 800 + ", \"desc\":\"" + "JSON fail" + "\"}";
		}
		User user = new User();
		String sEmail = user.GetUserEmailBySession(sSession);
		if(sEmail == null){
			return "{\"code\":" + 700 + ", \"desc\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Batch batch = new Batch();
		int ivalue = batch.UpdateSmsConfig(iBatchId, sSmsConfig3, sSmsConfig5, sSmsConfigErr);
		if(ivalue<=0){
			return "{\"code\":" + 300 + ", \"desc\":\"" + "Cap nhat du lieu loi" + "\"}";			
		}
		return "{\"code\":" + 200 + ", \"desc\":\"" + "Thanh cong!" + "\"}";
	}
}
