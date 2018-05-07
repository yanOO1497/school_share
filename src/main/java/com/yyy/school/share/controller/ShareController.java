package com.yyy.school.share.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

import com.yyy.school.share.service.ShareService;

import static com.yyy.school.share.util.JsonHelper.jsonEntity;

@Controller
@RequestMapping(value = "/share")
public class ShareController {

	@Autowired
	private ShareService shareService;
	
	@RequestMapping(value = "saveUserInfo.do")
	public ResponseEntity<String> saveUserInfo(String uid, String nickName, String avatarUrl, String school, Integer sex){
		System.out.println("saveUserInfo call");
		
		int i = this.shareService.saveUserInfo(uid, nickName, avatarUrl, school, sex);
		Map<String, Object> result = new HashMap<String, Object>();
		if(i != 1){
			result.put("code", 99);
			result.put("msg", "添加失败");
			return jsonEntity(result);
		}
		result.put("code", 100);
		result.put("msg", "添加成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "setBioByUid.do")
	public ResponseEntity<String> setBioByUid(String bio, String uid){
		System.out.println("setBioByUid call");
		
		int i = this.shareService.setBioByUid(bio, uid);
		Map<String, Object> result = new HashMap<String, Object>();
		if(i != 1){
			result.put("code", 99);
			result.put("msg", "修改失败");
			return jsonEntity(result);
		}
		result.put("code", 100);
		result.put("msg", "修改成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "addToQuestion.do")
	public ResponseEntity<String> addToQuestion(String content,  @RequestParam(value = "picUrl", defaultValue = "")String picUrl, String uid){
		System.out.println("addToQuestion call");
		
		int i = this.shareService.addToQuestion(content, picUrl, uid);
		Map<String, Object> result = new HashMap<String, Object>();
		if(i != 1){
			result.put("code", 99);
			result.put("msg", "创建失败");
			return jsonEntity(result);
		}
		result.put("code", 100);
		result.put("msg", "创建成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadQuestionList.do")
	public ResponseEntity<String> loadQuestionList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
			System.out.println("loadQuestionList call");

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("type", "question");
			result.put("nowUid", nowUid);
			result.put("subjects", this.shareService.loadQuestionList(start, count, nowUid));
			return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadUserInfoDetails.do")
	public ResponseEntity<String> loadUserInfoDetails(String uid){
		System.out.println("loadUserInfoDetails call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("result", this.shareService.loadUserInfoDetails(uid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "setAgree.do")
	public ResponseEntity<String> setAgree(String uid, Integer type, Integer mid, Integer agreeFlag){
		System.out.println("setAgree call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "修改成功");
		result.put("result", this.shareService.setAgree(uid, type, mid, agreeFlag));
		return jsonEntity(result);
	}
	

	@RequestMapping(value = "setDisagree.do")
	public ResponseEntity<String> setDisagree(String uid, Integer type, Integer mid, Integer disagreeFlag){
		System.out.println("setDisagree call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "修改成功");
		result.put("result", this.shareService.setDisagree(uid, type, mid, disagreeFlag));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "setCollect.do")
	public ResponseEntity<String> setCollect(String uid, Integer type, Integer mid, Integer collectFlag){
		System.out.println("setCollect call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "修改成功");
		result.put("result", this.shareService.setCollect(uid, type, mid, collectFlag));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "setReport.do")
	public ResponseEntity<String> setReport(String uid, Integer type, Integer mid){
		System.out.println("setReport call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "举报成功");
		result.put("result", this.shareService.setReport(uid, type, mid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "addToComment.do")
	public ResponseEntity<String> addToComment(String uid, Integer type, Integer mid, String content, @RequestParam(value = "fatherId", defaultValue = "0") String fatherId){
		System.out.println("addToComment call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "评论成功");
		result.put("result", this.shareService.addToComment(uid, type, mid, content, fatherId));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getComment.do")
	public ResponseEntity<String> getComment(Integer type, Integer mid){
		System.out.println("getComment call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "获取评论成功");
		result.put("result", this.shareService.getComment(type, mid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getQuestionListByUid.do")
	public ResponseEntity<String> getQuestionListByUid(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
			System.out.println("getQuestionListByUid call");

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("type", "question");
			result.put("nowUid", nowUid);
			result.put("subjects", this.shareService.getQuestionListByUid(start, count, nowUid));
			return jsonEntity(result);
	}
	
	@RequestMapping(value = "deleteFromQuestionByMid.do")
	public ResponseEntity<String> deleteFromQuestionByMid(Integer mid){
		System.out.println("deleteFromQuestionByMid call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		int i = this.shareService.deleteFromQuestionByMid(mid);
		if(i != 1){
			result.put("code", 99);
			result.put("msg", "删除失败");
		}
		result.put("code", 100);
		result.put("msg", "删除成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "addToFeedback.do")
	public ResponseEntity<String> addToFeedback(String uid, String content){
		System.out.println("addToFeedback");
		
		Map<String, Object> result = new HashMap<String, Object>();
		this.shareService.addToFeedback(uid, content);
		result.put("code", 100);
		result.put("msg", "反馈成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getMessageByMidAndType.do")
	public ResponseEntity<String> getMessageByMidAndType(Integer type, Integer mid, String nowUid){
		System.out.println("getMessageByMidAndType type:" + type + " mid:" + mid);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("result", this.shareService.getMessageByMidAndType(type, mid, nowUid));
		result.put("subjects", this.shareService.getComment(type, mid));
		return jsonEntity(result);
	}
	
	//TODO
	@RequestMapping(value = "uploadPic.do")
	public ResponseEntity<String> uploadPic(String uid, HttpServletRequest request) throws Exception{
		System.out.println("uploadPic call");
		Long time = System.currentTimeMillis();
			
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("image");
		String originalFilename = multipartFile.getOriginalFilename();
		String type = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
		String filePath = "D:\\image\\" + uid + "_" + time + type;
		String fileName = uid + "_" + time + type;
		File convFile = new File(filePath);
		multipartFile.transferTo(convFile);
		System.out.println(System.currentTimeMillis() - time);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "图片上传成功");
		result.put("result", this.shareService.uploadPic(filePath, fileName));
		System.out.println(System.currentTimeMillis() - time);
		return jsonEntity(result);
	}
}
