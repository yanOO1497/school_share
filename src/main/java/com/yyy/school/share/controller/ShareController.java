package com.yyy.school.share.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
	
	//全部发布使用同一个接口
//	@RequestMapping(value = "addToQuestion.do")
//	public ResponseEntity<String> addToQuestion(String content,  @RequestParam(value = "picUrl", defaultValue = "")String picUrl, String uid){
//		System.out.println("addToQuestion call");
//		
//		int i = this.shareService.addToQuestion(content, picUrl, uid);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if(i != 1){
//			result.put("code", 99);
//			result.put("msg", "创建失败");
//			return jsonEntity(result);
//		}
//		result.put("code", 100);
//		result.put("msg", "创建成功");
//		return jsonEntity(result);
//	}
	
//	@RequestMapping(value = "loadQuestionList.do")
//	public ResponseEntity<String> loadQuestionList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
//			System.out.println("loadQuestionList call");
//
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 100);
//			result.put("msg", "查询成功");
//			result.put("type", "question");
//			result.put("nowUid", nowUid);
//			result.put("subjects", this.shareService.loadQuestionList(start, count, nowUid));
//			return jsonEntity(result);
//	}
	
//	@RequestMapping(value = "loadExperienceList.do")
//	public ResponseEntity<String> loadExperienceList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
//			System.out.println("loadExperienceList call");
//
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 100);
//			result.put("msg", "查询成功");
//			result.put("type", "question");
//			result.put("nowUid", nowUid);
//			result.put("subjects", this.shareService.loadExperienceList(start, count, nowUid));
//			return jsonEntity(result);
//	}
//	@RequestMapping(value = "loadActivityList.do")
//	public ResponseEntity<String> loadActivityList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
//			System.out.println("loadActivityList call");
//
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 100);
//			result.put("msg", "查询成功");
//			result.put("type", "question");
//			result.put("nowUid", nowUid);
//			result.put("subjects", this.shareService.loadActivityList(start, count, nowUid));
//			return jsonEntity(result);
//	}
//	@RequestMapping(value = "loadMarketList.do")
//	public ResponseEntity<String> loadMarketList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
//			System.out.println("loadMarketList call");
//
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 100);
//			result.put("msg", "查询成功");
//			result.put("type", "question");
//			result.put("nowUid", nowUid);
//			result.put("subjects", this.shareService.loadMarketList(start, count, nowUid));
//			return jsonEntity(result);
//	}
//	
//	@RequestMapping(value = "loadRewardHelpList.do")
//	public ResponseEntity<String> loadRewardHelpList(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
//			System.out.println("loadRewardHelpList call");
//
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 100);
//			result.put("msg", "查询成功");
//			result.put("type", "question");
//			result.put("nowUid", nowUid);
//			result.put("subjects", this.shareService.loadRewardHelpList(start, count, nowUid));
//			return jsonEntity(result);
//	}
	
	@RequestMapping(value = "loadTableList.do")
	public ResponseEntity<String> loadTableList(Integer start, Integer tableType, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
			System.out.println("loadTableList call");

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("type", "question");
			result.put("nowUid", nowUid);
			result.put("subjects", this.shareService.loadTableList(start, count, nowUid,tableType));
			return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadBookList.do")
	public ResponseEntity<String> loadBookList(Integer start, Integer bookType, Integer count){
			System.out.println("loadBookList call");

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("subjects", this.shareService.loadBookList(start, count, bookType));
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
	

	@RequestMapping(value = "setShare.do")
	public ResponseEntity<String> setShare(String uid, Integer type, Integer mid){
		System.out.println("setShare call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "修改成功");
		result.put("result", this.shareService.setShare(uid, type, mid));
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
	
	public ResponseEntity<String> getExperienceListByUid(Integer start, Integer count, @RequestParam(value = "nowUid", defaultValue = "0") String nowUid){
		System.out.println("getExperienceListByUid call");

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("type", "question");
		result.put("nowUid", nowUid);
		result.put("subjects", this.shareService.getExperienceListByUid(start, count, nowUid));
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
	
	@RequestMapping(value = "uploadPic.do", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPic(HttpServletRequest request) throws Exception{
		System.out.println("uploadPic call" );
		Long time = System.currentTimeMillis();
		
//		String uid = request.getParameter("uid");
//		Integer type = 1;
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String uid = multipartRequest.getParameter("uid");
		Integer type = 1;
		System.out.println("uploadPic call" + uid + " " );
		MultipartFile multipartFile = multipartRequest.getFile("image");
		String originalFilename = multipartFile.getOriginalFilename();
		String fileType = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
		String filePath = "D:\\image\\" + uid + "_" + time + fileType;
		String fileName = type + "_" + uid + "_" + time + fileType;
		File convFile = new File(filePath);
		multipartFile.transferTo(convFile);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "图片上传成功");
		result.put("picUrl", this.shareService.uploadPic(filePath, fileName));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "publish.do")
	public ResponseEntity<String> publish(String uid,String[] successUrl, 
			@RequestParam(value = "cancelUrl", defaultValue = "")String[] cancelUrl, Integer type, String content, 
			@RequestParam(value = "reward", defaultValue = "")String reward, Integer flag) throws IOException {
		System.out.println("publish call");
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		int i = this.shareService.publish(uid, successUrl, cancelUrl, type, content, reward, flag);
		if (i != 1){
			result.put("code", 99);
			result.put("msg", "发布失败");
			return jsonEntity(result);
		} else {
			result.put("code", 100);
			result.put("msg", "发布成功");
			return jsonEntity(result);
		}
	}
	@RequestMapping(value = "sentMessage.do")
	public ResponseEntity<String> sentMessage(String uid, String content, String nowUid) throws IOException {
		System.out.println("publish call");
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		int i = this.shareService.sentMessage(uid, content,  nowUid);
		if (i != 1){
			result.put("code", 99);
			result.put("msg", "发布失败");
			return jsonEntity(result);
		} else {
			result.put("code", 100);
			result.put("msg", "发布成功");
			return jsonEntity(result);
		}
	}
	
}
