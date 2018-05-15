package com.yyy.school.share.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
//add
//import javax.jms.JMSException;  
//import javax.jms.Message;  
//import javax.jms.MessageListener;  
//import javax.jms.TextMessage;  
//  
//import org.apache.log4j.Logger;  
//import org.springframework.stereotype.Component;  
//  
//import com.alibaba.fastjson.JSON; 
//add
import com.yyy.school.share.dao.ShareDao;
import com.yyy.school.share.util.QiniuUtil;
//import com.yyy.school.share.util.WebsocketController;

@Service
public class ShareService {

	@Autowired
	private ShareDao shareDao;
	
	public static QiniuUtil qiniuUtil = new QiniuUtil();
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 处理获取信息列表的点赞等信息
	 public List<Map<String, Object>> getMoreListDetail (List<Map<String, Object>> list,String nowUid,Integer type) {
	    	for (Map<String, Object> map : list) {
				String uid = map.get("uid").toString();
				Integer mid = (Integer)map.get("mid");
//				List<Map<String, Object>> comList = this.getComment(type, mid);
				map.put("commentNum", this.shareDao.getCommentNumByTypeAndMid(type, (Integer)map.get("id")));
				Map<String, Object> userMap = this.shareDao.getUserInfoByUid(uid);
				map.put("nickName", userMap.get("nickName").toString());
				map.put("avatarUrl", userMap.get("avatarUrl").toString());
				map.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
				//统计点赞、踩、收藏、举报人数，并判断当前用户的点赞情况。
				map.put("agreeFlag", 0);
				map.put("collectFlag", 0);
				map.put("reportFlag", 0);
			    Object agreeStr = map.get("agreeList");
			    Object shareStr = map.get("shareList");
			    Object collectStr = map.get("collectList");
			    Object reportStr = map.get("reportList");
			    if(agreeStr == null){
			    	map.put("agreeNum", 0);
			    } else {
			    	List<String> agreeList = Arrays.asList(agreeStr.toString().split(","));
			    	map.put("agreeNum", agreeList.size());
			    	for (String str : agreeList) {
						if (str.equals(nowUid.toString())){
							map.put("agreeFlag", 1);
							break;
						}
					}
			    }
			    if(shareStr == null){
			    	map.put("shareNum", 0);
			    } else {
			    	List<String> shareList = Arrays.asList(shareStr.toString().split(","));
			    	map.put("shareNum", shareList.size());
			    }
			    if(collectStr == null){
			    	map.put("collectNum", 0);
			    } else {
			    	List<String> collectList = Arrays.asList(collectStr.toString().split(","));
			    	map.put("collectNum", collectList.size());
			    	for (String str : collectList) {
			    		if (str.equals(nowUid.toString())){
			    			map.put("collectFlag", 1);
			    			break;
			    		}
			    	}
			    }
			    if(reportStr == null){
			    	map.put("reportNum", 0);
			    } else {
			    	List<String> reportList = Arrays.asList(reportStr.toString().split(","));
			    	map.put("reportNum", reportList.size());
			    	for (String str : reportList) {
			    		if (str.equals(nowUid.toString())){
			    			map.put("reportFlag", 1);
			    			break;
			    		}
			    	}
			    }
			}
	    	return list;
	    }
		
//	public List<Map<String, Object>> loadQuestionList(Integer start, Integer count, String nowUid) {
//		List<Map<String, Object>> list = this.shareDao.loadQuestionList(start, count);
//		
//		list = this.getMoreListDetail(list, nowUid);
//		//举报次数超过20，不显示
//		for (int i = list.size()-1; i >= 0; i--){
//			if ((Integer)list.get(i).get("reportNum") >= 20){
//				list.remove(i);
//			}
//		}
//		return list;
//	}
	
	public List<Map<String, Object>> loadTableList(Integer start, Integer count, String nowUid,Integer tableType) {
		List<Map<String, Object>> list = this.shareDao.loadTableList(start, count ,tableType);
		
		list = this.getMoreListDetail(list, nowUid,tableType);
		//举报次数超过20，不显示
		for (int i = list.size()-1; i >= 0; i--){
			if ((Integer)list.get(i).get("reportNum") >= 20){
				list.remove(i);
			}
		}
		return list;
	}
	
	public Map<String, Object> loadUserInfoDetails(String uid) {
		return this.shareDao.getUserInfoByUid(uid);
	}

	public Map<String, Object> setAgree(String uid, Integer type, Integer mid, Integer agreeFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		int newFlag = agreeFlag == 0 ? 1 : 0;
		List<Map<String, Object>> list = this.shareDao.getAgree(type, mid);
		Object agree = list.get(0).get("agreeList");
		String agreeStr = "";
		if (agree != null && !agree.equals("")){
			agreeStr = agree.toString();
		}
		List<String> agreeList_1 = Arrays.asList(agreeStr.split(","));//Arrays.asList() 返回的是Arrays的内部类ArrayList， 而不是java.util.ArrayList
		List<String> agreeList = new ArrayList<String>(agreeList_1);
		if (newFlag == 1){
			if(agreeList.get(0).equals("")){  //防止无数据时生成的是 ,uid
				agreeList = new ArrayList<String>();
			}
			agreeList.add(uid.toString());
		} else {
			for (int i = agreeList.size()-1; i >= 0; i--){
				if (agreeList.get(i).equals(uid.toString())){
					agreeList.remove(i);
					break;
				}
			}
		}
		agreeStr = agreeList.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		this.shareDao.setAgree(type, mid, agreeStr);
		map.put("agreeNum", agreeList.size());
		map.put("agreeFlag", newFlag);
		return map;
	}
	
	public Map<String, Object> setShare(String uid, Integer type, Integer mid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.shareDao.getShareNum(type, mid);
		Object share = list.get(0).get("shareList");
		String shareStr = "";
		if (share != null && !share.equals("")){
			shareStr = share.toString();
		}
		List<String> shareList_1 = Arrays.asList(shareStr.split(","));//Arrays.asList() 返回的是Arrays的内部类ArrayList， 而不是java.util.ArrayList
		List<String> shareList = new ArrayList<String>(shareList_1);
		
		if(shareList.get(0).equals("")){  //防止无数据时生成的是 ,uid
			shareList = new ArrayList<String>();
		}
		shareList.add(uid.toString());
		shareStr = shareList.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		this.shareDao.setShare(type, mid, shareStr);
		map.put("shareNum", shareList.size());
		return map;
	}
	
	public Map<String, Object> setCollect(String uid, Integer type, Integer mid, Integer collectFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		int newFlag = collectFlag == 0 ? 1 : 0;
		List<Map<String, Object>> list = this.shareDao.getCollect(type, mid);
		Object collect = list.get(0).get("collectList");
		String collectStr = "";
		if (collect != null && !collect.equals("")){
			collectStr = collect.toString();
		}
		List<String> collectList_1 = Arrays.asList(collectStr.split(","));//Arrays.asList() 返回的是Arrays的内部类ArrayList， 而不是java.util.ArrayList
		List<String> collectList = new ArrayList<String>(collectList_1);
		if (newFlag == 1){
			if(collectList.get(0).equals("")){  //防止无数据时生成的是 ,uid
				collectList = new ArrayList<String>();
			}
			collectList.add(uid.toString());
		} else {
			for (int i = collectList.size()-1; i >= 0; i--){
				if (collectList.get(i).equals(uid.toString())){
					collectList.remove(i);
					break;
				}
			}
		}
		collectStr = collectList.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		this.shareDao.setCollect(type, mid, collectStr);
		map.put("collectNum", collectList.size());
		map.put("collectFlag", newFlag);
		return map;
	}
	
	public Map<String, Object> setReport(String uid, Integer type, Integer mid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.shareDao.getReport(type, mid);
		Object report = list.get(0).get("reportList");
		String reportStr = "";
		if (report != null && !report.equals("")){
			reportStr = report.toString();
		}
		List<String> reportList_1 = Arrays.asList(reportStr.split(","));//Arrays.asList() 返回的是Arrays的内部类ArrayList， 而不是java.util.ArrayList
		List<String> reportList = new ArrayList<String>(reportList_1);
		if(reportList.get(0).equals("")){  //防止无数据时生成的是 ,uid
			reportList = new ArrayList<String>();
		}
		reportList.add(uid.toString());
		reportStr = reportList.toString().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
		this.shareDao.setReport(type, mid, reportStr);
		map.put("reportNum", reportList.size());
		map.put("reportFlag", 1);
		return map;
	}

	public int saveUserInfo(String uid, String nickName, String avatarUrl, String school,
			Integer sex) {
		return this.shareDao.saveUserInfo(uid, nickName, avatarUrl, school, sex);
		
	}

	public int setBioByUid(String bio, String uid) {
		return this.shareDao.setBioByUid(bio, uid);
	}

//	public int addToQuestion(String content, String picUrl, String uid) {
//		return this.shareDao.addToQuestion(content, picUrl, uid);
//	}

	public int addToComment(String uid, Integer type, Integer mid,
			String content, String fatherId) {
		return this.shareDao.addToComment(uid, type, mid, content, fatherId);
	}

	public List<Map<String, Object>> getComment(Integer type, Integer mid) {
		List<Map<String, Object>> list = this.shareDao.getComment(type, mid);
		for (Map<String, Object> map : list) {
			map.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
			map.put("nickName", this.shareDao.getNickNameByUid(map.get("uid").toString()));
			String fatherId = map.get("fatherId").toString();
			if(! fatherId.equals("0")){
				map.put("fatherNickName", this.shareDao.getNickNameByUid(fatherId));
			}
		}
		return list;
	}
   
    public List<Map<String, Object>> getQuestionListByUid(Integer start, Integer count,
			String nowUid) {
		List<Map<String, Object>> list = this.shareDao.getQuestionListByUid(start, count, nowUid);
		list = this.getMoreListDetail(list, nowUid,1);
		return list;
	}

	public int deleteFromQuestionByMid(Integer mid) {
		return this.shareDao.deleteFromQuestionByMid(mid);
	}

	public String uploadPic(String filePath, String fileName) {
		
		 String picUrl = "http://p8aftlhm5.bkt.clouddn.com/" + fileName;  //拼接picUrl
         try {
             //上传到七牛云
             qiniuUtil.upload(filePath, fileName);
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
        	 File deleteFile = new File(filePath); //删除本地副本
             deleteFile.delete();
         }
         return picUrl;
	}

	public void addToFeedback(String uid, String content) {
		this.shareDao.addToFeedback(uid, content);
		
	}

	public Map<String, Object> getMessageByMidAndType(Integer type, Integer mid, String nowUid) {
		Map<String, Object> map = this.shareDao.getMessageByMidAndType(type, mid);
		map.put("commentNum", this.shareDao.getCommentNumByTypeAndMid(type, (Integer)map.get("id")));
		String uid = map.get("uid").toString();
		Map<String, Object> userMap = this.shareDao.getUserInfoByUid(uid);
		map.put("nickName", userMap.get("nickName").toString());
		map.put("avatarUrl", userMap.get("avatarUrl").toString());
		map.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
		//统计点赞、踩、收藏、举报人数，并判断当前用户的点赞情况。
		map.put("agreeFlag", 0);
		map.put("disagreeFlag", 0);
		map.put("collectFlag", 0);
		map.put("reportFlag", 0);
	    Object agreeStr = map.get("agreeList");
	    Object disagreeStr = map.get("disagreeList");
	    Object collectStr = map.get("collectList");
	    Object reportStr = map.get("reportList");
	    if(agreeStr == null){
	    	map.put("agreeNum", 0);
	    } else {
	    	List<String> agreeList = Arrays.asList(agreeStr.toString().split(","));
	    	map.put("agreeNum", agreeList.size());
	    	for (String str : agreeList) {
				if (str.equals(nowUid.toString())){
					map.put("agreeFlag", 1);
					break;
				}
			}
	    }
	    if(disagreeStr == null){
	    	map.put("disagreeNum", 0);
	    } else {
	    	List<String> disagreeList = Arrays.asList(disagreeStr.toString().split(","));
	    	map.put("disagreeNum", disagreeList.size());
	    	for (String str : disagreeList) {
	    		if (str.equals(nowUid.toString())){
	    			map.put("disagreeFlag", 1);
	    			break;
	    		}
	    	}
	    }
	    if(collectStr == null){
	    	map.put("collectNum", 0);
	    } else {
	    	List<String> collectList = Arrays.asList(collectStr.toString().split(","));
	    	map.put("collectNum", collectList.size());
	    	for (String str : collectList) {
	    		if (str.equals(nowUid.toString())){
	    			map.put("collectFlag", 1);
	    			break;
	    		}
	    	}
	    }
	    if(reportStr == null){
	    	map.put("reportNum", 0);
	    } else {
	    	List<String> reportList = Arrays.asList(reportStr.toString().split(","));
	    	map.put("reportNum", reportList.size());
	    	for (String str : reportList) {
	    		if (str.equals(nowUid.toString())){
	    			map.put("reportFlag", 1);
	    			break;
	    		}
	    	}
	    }
		return map;
	}
    
	//TODO thread?
	public int publish(String uid, String[] successUrl, String[] cancelUrl,
			Integer type, String content, String reward, Integer flag) throws IOException {
		
		if(cancelUrl != null) {
			for(int i = 0; i < cancelUrl.length; i++){
				String cancelStr = cancelUrl[i].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
				qiniuUtil.delete(cancelStr);
			}
			if (flag == 0) {
				return 0;
			}
		}
		String picUrl = "";
		if(successUrl != null) {
			picUrl = StringUtils.join(successUrl, ",").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
			
		}
		
		return this.shareDao.publish(type, content, picUrl, uid, reward);
		
		
	}

	public Object getExperienceListByUid(Integer start, Integer count, String nowUid) {
		List<Map<String, Object>> list = this.shareDao.getExperienceListByUid(start, count, nowUid);
		list = this.getMoreListDetail(list, nowUid , 2);
		return null;
	}

	public int sentMessage(String uid, String content, String nowUid) {
		
		
		return 0;
	}

	public List<Map<String, Object>> loadBookList(Integer start, Integer count, Integer bookType) {
		List<Map<String, Object>> list = this.shareDao.loadBookList(start, count ,bookType);
		for (Map<String, Object> map : list) {
			Map<String, Object> userMap = this.shareDao.getUserInfoByUid(map.get("uid").toString());
			map.put("nickName", userMap.get("nickName").toString());
			map.put("avatarUrl", userMap.get("avatarUrl").toString());
			map.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
		}
		return list;
	}

	public List<Map<String, Object>> searchCoursewareList(Integer start, Integer count,  String searchName) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.shareDao.searchCoursewareList(start, count ,searchName);
		for (Map<String, Object> map : list) {
			Map<String, Object> userMap = this.shareDao.getUserInfoByUid(map.get("uid").toString());
			map.put("nickName", userMap.get("nickName").toString());
			map.put("avatarUrl", userMap.get("avatarUrl").toString());
			map.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
		}
		return list;
	}

//	public Object loadExperienceList(Integer start, Integer count, String nowUid) {
//		List<Map<String, Object>> list = this.shareDao.loadExperienceList(start, count);
//		
//		list = this.getMoreListDetail(list, nowUid);
//		//举报次数超过20，不显示
//		for (int i = list.size()-1; i >= 0; i--){
//			if ((Integer)list.get(i).get("reportNum") >= 20){
//				list.remove(i);
//			}
//		}
//		return list;
//	}

//	public Object loadActivityList(Integer start, Integer count, String nowUid) {
//		List<Map<String, Object>> list = this.shareDao.loadActivityList(start, count);
//		list = this.getMoreListDetail(list, nowUid);
//		//举报次数超过20，不显示
//		for (int i = list.size()-1; i >= 0; i--){
//			if ((Integer)list.get(i).get("reportNum") >= 20){
//				list.remove(i);
//			}
//		}
//		return list;
//	}

//	public Object loadMarketList(Integer start, Integer count, String nowUid) {
//		List<Map<String, Object>> list = this.shareDao.loadMarketList(start, count);
//		list = this.getMoreListDetail(list, nowUid);
//		//举报次数超过20，不显示
//		for (int i = list.size()-1; i >= 0; i--){
//			if ((Integer)list.get(i).get("reportNum") >= 20){
//				list.remove(i);
//			}
//		}
//		return list;
//	}

//	public Object loadRewardHelpList(Integer start, Integer count, String nowUid) {
//		List<Map<String, Object>> list = this.shareDao.loadRewardHelpList(start, count);
//		list = this.getMoreListDetail(list, nowUid);
//		//举报次数超过20，不显示
//		for (int i = list.size()-1; i >= 0; i--){
//			if ((Integer)list.get(i).get("reportNum") >= 20){
//				list.remove(i);
//			}
//		}
//		return list;
//	}

		
	
}
