package com.orion.product;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/product")
public class RESTProductService {
	private String iUserEmail;
	@Path("/insert")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String InsertProductByCompanyMemberApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;		
		String sProductName = "";
		String sProductTag = "";
		int iProductType = 0;
		String sProductDetail = "";
		String sProductManualLink = "";
		int iProductStatus = 0;
		String sProductDesPrivate = "";
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			
			sProductName = jsonobjReq.getString("pro_name");
			sProductTag = jsonobjReq.getString("pro_tag");
			iProductType = jsonobjReq.getInt("pro_type");
			sProductDetail = jsonobjReq.getString("pro_detail");
			sProductManualLink = jsonobjReq.getString("pro_manuallink");
			if(sReq.contains("pro_description_private")){
				sProductDesPrivate = jsonobjReq.getString("pro_description_private");
			}
			iProductStatus = jsonobjReq.getInt("pro_status");	
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Product product = new Product();
		int iProductId = product.InsertProduct(sProductName, sProductTag, iProductType, sProductDetail,iCompanyId, iUserEmail, iProductStatus,sProductManualLink, sProductDesPrivate);
		if(iProductId == 0){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"id\":" + iProductId + "}";
	}
	@Path("/update")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String UpdateProductByCompanyMemberApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;		
		int iProductId = 0;
		String sProductName = "";
		String sProductTag = "";
		int iProductType = 0;
		String sProductDetail = "";
		int iProductExpDay = 0;		
		String sProductManualLink = "";
		int iProductStatus = 0;
		String sProductDesPrivate = "";
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");
			iCompanyId = jsonobjReq.getInt("com_id");
			
			iProductId = jsonobjReq.getInt("pro_id");
			sProductName = jsonobjReq.getString("pro_name");
			sProductTag = jsonobjReq.getString("pro_tag");
			iProductType = jsonobjReq.getInt("pro_type");
			sProductDetail = jsonobjReq.getString("pro_detail");	
			sProductManualLink = jsonobjReq.getString("pro_manuallink");
			iProductStatus = jsonobjReq.getInt("pro_status");	
			if(sReq.contains("pro_description_private")){
				sProductDesPrivate = jsonobjReq.getString("pro_description_private");
			}
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Product product = new Product();
		if(product.UpdateProduct(iProductId, sProductName, sProductTag, iProductType, sProductDetail, sProductManualLink, iProductStatus, sProductDesPrivate) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	//---------------------------------------
	@Path("/getlistbycompany")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetListOfProductByCompanyApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Product product = new Product();
		String sProduct = product.GetListOfProductByCompany(iCompanyId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"product\":" + sProduct + "}";
	}
	
	//============================================================================================================================
	// lấy sách sách lô hàng và số lượng đã bán theo productid
	@Path("/getlistbatchamountbyproduct")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetListOfBatchByProductApi(String sReq){
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		int iProductId = 0;
		try {
			JSONObject jsonObject = new JSONObject(sReq);
			sSession = jsonObject.getString("use_session");
			iCompanyId = jsonObject.getInt("com_id");
			iProductId = jsonObject.getInt("pro_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Product product = new Product();
		
		
		return "";
	}
	@Path("/getlistnamebycompany")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetListOfProductNameByCompanyApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}
		Product product = new Product();
		String sProduct = product.GetListOfProductNameByCompany(iCompanyId);
		return "{\"code\":" + 200 + ", \"description\":\"" + "Thành công" + "\", \"product\":" + sProduct + "}";
	}
	
//	//----------------------------------------
	@Path("/delete")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String DeleteProductByCompanyMemberApi(String sReq) {
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
		Product product = new Product();
		if(product.DeleteProduct(iProductId) == false){
			return "{\"code\":" + 900 + ", \"description\":\"" + "Lỗi kết nối database" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	//---
	@Path("/updatepicture")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String UpdateProductPictureApi(String sReq) {
		sReq = sReq.replaceAll("'", "");
		String sSession = "";
		int iCompanyId = 0;
		
		int iProductId = 0;
		String sProductPicture = "";
		String sProductThumbnail = "";
		
		try {
			JSONObject jsonobjReq = new JSONObject(sReq);
			sSession = jsonobjReq.getString("use_session");	
			iCompanyId = jsonobjReq.getInt("com_id");
			
			iProductId = jsonobjReq.getInt("pro_id");
			sProductPicture = jsonobjReq.getString("pro_picture");
			sProductThumbnail = jsonobjReq.getString("pro_thumbnail");
		} catch (JSONException e) {
			return "{\"code\":" + 800 + ", \"desc\":\"" + e.toString().replaceAll("\"", "") + "\"}";
		}
		User user = new User();
		String sUserEmail = user.GetUserEmailBySession(sSession);
		if(sUserEmail == null){
			return "{\"code\":" + 700 + ", \"description\":\"" + "Nguoi dung chua dang nhap" + "\"}";
		}		
		Product product = new Product();
		if(product.UpdatePicture(iProductId, sProductPicture) == false){
			return "{\"code\":" + 901 + ", \"description\":\"" + "Loi tao file picture" + "\"}";
		}
		if(product.UpdatePictureThumbnail(iProductId, sProductThumbnail) == false){
			return "{\"code\":" + 902 + ", \"description\":\"" + "Loi tao file thumbnail" + "\"}";
		}
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\"}";
	}
	
	@Path("/getimageproductbyid")
	@POST
	@Produces("application/json; charset=UTF-8")
	public String GetImageProductById(String sReq){
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
		Product product = new Product();
		String a = product.GetImagePicture(iProductId);
		System.out.println(a);
		System.out.println("========================================================================================================301");
		return "{\"code\":" + 200 + ", \"description\":\"" + "thành công" + "\", \"pro_image\":\"" + a + "\"}";
	}

}
