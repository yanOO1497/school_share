package com.yyy.school.share.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ShareDao {
    
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public String getTableNameByType (Integer type) {
		String tableName = "";
		switch( type ){
			case 0 :
				tableName = "tableview";
				break;
			case 1:
				tableName = "question";
				break;
			case 2:
				tableName = "experienceshare";
				break;
			case 3:
				tableName = "rewardhelp";
				break;
			case 4:
				tableName = "schoolactivity";
				break;
			case 5:
				tableName = "secondarymarket";
				break;
		}
		return tableName;
	}
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//	public List<Map<String, Object>> loadQuestionList(Integer start,
//			Integer count) {
//		String sql = "select * from question order by createTimeStamp desc limit ?,?";
//		return this.jdbcTemplate.queryForList(sql, start, count);
//	}

	public Map<String, Object> getUserInfoByUid(String uid) {
		String sql = "select * from user where id = ?";
		return this.jdbcTemplate.queryForMap(sql, uid);
	}


	public List<Map<String, Object>> getAgree(Integer type, Integer mid) {
		String tableName = this.getTableNameByType(type);
		String sql = "select agreeList from " + tableName + " where id = ?";
		return this.jdbcTemplate.queryForList(sql, mid);
	}
	
	public List<Map<String, Object>> getShareNum(Integer type, Integer mid) {
		String tableName =this.getTableNameByType(type);
		String sql = "select shareList from " + tableName + " where id = ?";
		return this.jdbcTemplate.queryForList(sql, mid);
	}
	
	public List<Map<String, Object>> getCollect(Integer type, Integer mid) {
		String tableName =this.getTableNameByType(type);
		String sql = "select collectList from " + tableName + " where id = ?";
		return this.jdbcTemplate.queryForList(sql, mid);
	}
	
	
	public List<Map<String, Object>> getReport(Integer type, Integer mid) {
		String tableName =this.getTableNameByType(type);
		String sql = "select reportList from " + tableName + "  where id = ?";
		return this.jdbcTemplate.queryForList(sql, mid);
	}

	public void setAgree(Integer type, Integer mid, String agreeStr) {
		String tableName =this.getTableNameByType(type);
		String sql = "update " + tableName + " set agreeList = ? , score = score + 1 where id = ?";
		this.jdbcTemplate.update(sql, agreeStr, mid);
	}
	
	public void setShare(Integer type, Integer mid, String shareArr) {
		String tableName =this.getTableNameByType(type);
		String sql = "update " + tableName + " set shareList = ? , score = score + 2 where id = ?";
		this.jdbcTemplate.update(sql, shareArr, mid);	
	}
	
	public void setCollect(Integer type, Integer mid, String collectStr) {
		String tableName = this.getTableNameByType(type);
		String sql = "update " + tableName + " set collectList = ? , score = score + 2 where id = ?";
		this.jdbcTemplate.update(sql, collectStr, mid);
		
	}
	
	public void setReport(Integer type, Integer mid, String reportStr) {
		String tableName = this.getTableNameByType(type);
		String sql = "update " + tableName + " set reportList = ?  , score = score - 3  where id = ?";
		this.jdbcTemplate.update(sql, reportStr, mid);
		
	}

	public int saveUserInfo(String uid, String nickName, String avatarUrl,  Integer sex) {
		
		String getSql = "select * from user where id = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(getSql, uid);
		if(list.size() != 0 && list != null){
			return 1;
		}
		String sql = "insert into user (id, nickName, avatarUrl, createTimeStamp, sex) values(?,?,?,?,?)";
		return this.jdbcTemplate.update(sql, uid, nickName, avatarUrl, System.currentTimeMillis(), sex);
		
	}
	
	
	
	
	public int setBioByUid(String bio, String uid) {
		String sql = "update user set bio = ? where id = ?";
		return this.jdbcTemplate.update(sql, bio, uid);
	}

//	public int addToQuestion(String content, String picUrl, String uid) {
//		String sql = "insert into question (uid, content, picUrl, createTimeStamp) values(?,?,?,?)";
//		return this.jdbcTemplate.update(sql, uid, content, picUrl, System.currentTimeMillis());
//	}

	public int addToComment(String uid, Integer type, Integer mid,
			String content, String fatherId) {
		String sql = "insert into comment (type, mid, uid, createTimeStamp, content, fatherId) values(?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql, type, mid, uid, System.currentTimeMillis(), content, fatherId);
	}

	public List<Map<String, Object>> getComment(Integer type, Integer mid) {
		String sql = "select * from comment where type = ? and mid = ? order by createTimeStamp desc";
		return this.jdbcTemplate.queryForList(sql, type, mid);
	}

	public String getNickNameByUid(String uid) {
		String sql = "select nickName from user where id = ?";
		return this.jdbcTemplate.queryForList(sql, uid).get(0).get("nickName").toString();
	}

	public List<Map<String, Object>> getQuestionListByUid(Integer start,
			Integer count, String nowUid) {
		String sql = "select * from question where uid = ? order by createTimeStamp desc limit ?,?";
		return this.jdbcTemplate.queryForList(sql, nowUid, start, count);
	}

	public int deleteTableByMidAndType(Integer mid,Integer type) {
		String tableName = this.getTableNameByType(type);
		String sql = "delete from " + tableName + " where id = ?";
		return this.jdbcTemplate.update(sql, mid);
	}

	public void addToFeedback(String uid, String content) {
		String sql = "insert into feedback (uid, content, createTimeStamp) values(?,?,?)";
		this.jdbcTemplate.update(sql, uid, content, System.currentTimeMillis());	
	}

	public Map<String, Object> getMessageByMidAndType(Integer type, Integer mid) {
		String tableName = this.getTableNameByType(type);
		String sql = "select * from " + tableName + " where id = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, mid);
		if (list == null || list.size() == 0) {
			return new HashMap<String, Object>();
		} else {
			return list.get(0);
		}
	}

	public int getCommentNumByTypeAndMid(int type, Integer mid) {
		String sql = "select count(1) as commentNum from comment where mid = ? and type = ?";
		return Integer.parseInt(this.jdbcTemplate.queryForMap(sql, mid, type).get("commentNum").toString());
	}

	public int publish(Integer type, String content, String picUrl, String uid, String reward) {
		String tableName = this.getTableNameByType(type);
		String sql = "";
		if(type == 3) {
			sql = "insert into "+ tableName +" (uid, content, picUrl, createTimeStamp,reward) values(?,?,?,?,?)";
			return this.jdbcTemplate.update(sql, uid, content, picUrl, System.currentTimeMillis(),reward);
		}else {
			sql = "insert into "+ tableName +" (uid, content, picUrl, createTimeStamp) values(?,?,?,?)";
			return this.jdbcTemplate.update(sql, uid, content, picUrl, System.currentTimeMillis());
		}
	}
	public List<Map<String, Object>> getExperienceListByUid(Integer start, Integer count, String nowUid) {
		String sql = "select * from expericenceshare where uid = ? order by createTimeStamp desc limit ?,?";
		return this.jdbcTemplate.queryForList(sql, nowUid, start, count);
	}

	public List<Map<String, Object>> loadTableList(Integer start, Integer count, Integer tableType) {
		String tableName = this.getTableNameByType(tableType);
			String sql = "select * from "+ tableName +" order by createTimeStamp desc limit ?,?";	
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	public List<Map<String, Object>> loadCollectList(Integer start, Integer count ,String nowUid) {
		String sql = "select * from tableview WHERE collectList REGEXP '^"+nowUid+",|,"+nowUid+"$|,"+nowUid+",' order by createTimeStamp desc limit ?,?";	
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	
	public List<Map<String, Object>> loadShareList(Integer start, Integer count, String nowUid) {
		String sql = "select * from tableview WHERE shareList REGEXP '^"+nowUid+",|,"+nowUid+"$|,"+nowUid+",' order by createTimeStamp desc limit ?,?";	
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	
	public List<Map<String, Object>> loadTableListByUid(Integer start, Integer count,String uid) {
		String sql = "select * from tableview  where uid = ? order by createTimeStamp desc limit ?,?";	
		return this.jdbcTemplate.queryForList(sql,uid, start, count);
	}
	
	public Integer loadTablesUidById(Integer tableType,Integer id) {
		String tableName = this.getTableNameByType(tableType);
		String sql = "select uid from "+ tableName +" where id= ? ";	
		return (Integer)this.jdbcTemplate.queryForMap(sql, id).get("uid");
	}
	
	public List<Map<String, Object>> loadBookList(Integer start, Integer count, Integer bookType , String searchName) {
		String sql;
		if (searchName.equals("")) {
			sql = "select * from books where bookType = ? order by createTimeStamp desc limit ?,?";
		} else {
			sql = "select * from books where bookName like '%" + searchName + "%' and bookType = ? order by createTimeStamp desc limit ?,?";
		}
		
		return this.jdbcTemplate.queryForList(sql, bookType , start, count);
	}



	public List<Map<String, Object>> searchCoursewareList(Integer start, Integer count, String searchName) {
		String sql;
		if (searchName.equals("")) {
			sql = "select * from courseware order by createTimeStamp desc limit ?,?";
		} else {
			sql = "select * from courseware where courseName like '%" + searchName + "%'  order by createTimeStamp desc limit ?,?";
		}
		return this.jdbcTemplate.queryForList(sql , start, count);
		
	}


	public List<Map<String, Object>> loadQuesAndShareList(Integer start, Integer count, String searchName) {
		String sql;
		if (searchName.equals("")) {
			sql = "select * from question where score > 4  union select * from experienceshare where score > 4  order by createTimeStamp desc limit ?,?";
		} else {
			sql = "select * from question where content like '%" + searchName + "%' and score > 4  union select * from experienceshare where content like '%" + searchName + "%' and score > 4  order by createTimeStamp desc limit ?,?";
		}
		return this.jdbcTemplate.queryForList(sql , start, count);
	}

	public void addToChatLog(String nowUid, String toUid, String message, int state, Long time) {
		String sql = "insert into chatLog (uid, toUid, content, createTimeStamp, state) values(?,?,?,?,?)";
		this.jdbcTemplate.update(sql, nowUid, toUid, message, time, state);
	}


	//聊天记录标记为已读
	public void setAlreadyRead(String nowUid, String toUid) {
		String sql = "update chatLog set state = 1 where toUid = ? and uid = ?";
		this.jdbcTemplate.update(sql, nowUid, toUid);
	}


	public List<Map<String, Object>> getChatLogDetails(String nowUid,
			String toUid, Integer start, Integer count) {
		if(count == 0) {
			String sql = "select * from chatLog where (uid = ? and toUid = ?) or (uid = ? and toUid = ?)  order by createTimeStamp";
			return this.jdbcTemplate.queryForList(sql, nowUid, toUid, toUid, nowUid);
		}else {
			String sql = "select * from chatLog where (uid = ? and toUid = ?) or (uid = ? and toUid = ?)  order by createTimeStamp  desc limit ?,?";
			return this.jdbcTemplate.queryForList(sql, nowUid, toUid, toUid, nowUid, start, count);
		}
		
	}

	public List<Map<String, Object>> getChats(String nowUid, Integer start,
			Integer count) {
		List<Map<String, Object>> subjects = new ArrayList<Map<String, Object>>();
		String sql = "(select  uid as id from chatLog where toUid = ? group by uid) union" + 
					"(select  toUid as id from chatLog where uid = ? group by toUid)";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql, nowUid, nowUid);
		if (list == null || list.size() == 0){
			return new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> map : list) {
			String toUid = map.get("id").toString();
			List<Map<String, Object>> listDetail = getChatLogDetails(nowUid, toUid,0,0);			
			Map<String, Object> toUidMap = listDetail.get(listDetail.size()-1); //取与该用户最近的一条聊天记录。
			toUidMap.put("unreadNum", getUnreadByUid(nowUid, toUid));
			subjects.add(toUidMap);
		}
		return subjects;
	}


	public int getUnreadByUid(String nowUid, String toUid) {
		String sql = "select count(1) as unread from chatLog where uid = ? and toUid = ? and state = 0";
		return Integer.parseInt(this.jdbcTemplate.queryForMap(sql, toUid, nowUid).get("unread").toString());
	}


	public List<Map<String, Object>> getCarouselInBooks(Integer count) {
		// TODO Auto-generated method stub
		String sql = "select * from books  order by createTimeStamp desc limit 0,?";
		return this.jdbcTemplate.queryForList(sql, count);
	}

	public List<Map<String, Object>> getCarouselInCourseware(Integer count) {
		String sql = "select * from courseware  order by createTimeStamp desc limit 0,?";
		return this.jdbcTemplate.queryForList(sql, count);
	}

	public List<Map<String, Object>> getUnreadReply(String nowUid, Integer start, Integer count) {//未读的回复消息
		String sql = "select * from comment where fatherId in (select id from comment where uid = ?) and state = 0 order by createTimeStamp desc limit ?,?";
		List<Map<String, Object>> list =  this.jdbcTemplate.queryForList(sql, nowUid, start, count);
		return list;
		
	}

	 public static List<Map<String, Object>> getRepetition(List<Map<String, Object>> list1,  
			 List<Map<String, Object>> list2) {  
		 List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();  
	        for (Map<String, Object> map : list2) {//遍历list1  
	            if (list1.contains(map)) {//如果存在这个数  
	                result.add(map);//放进一个list里面，这个list就是交集  
	            }  
	        }  
	        return result;  
	    }  
//	public List<Map<String, Object>> getUnreadComment(String nowUid, Integer start, Integer count) {//未读的评论消息（指代）	
//		
//		String sql1;
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		String tableName[]= {"","question", "experienceshare", "rewardhelp", "schoolactivity", "secondarymarket"};
//		for (int i = 1; i < 6 ; i++) {
//			sql1 = "select id as mid,type from "+tableName[i]+" where uid  = ? order by createTimeStamp";	
//			list.addAll(this.jdbcTemplate.queryForList(sql1, nowUid));
//		}//获取用户发布的所有信息id与type
//		
//		String sql2 = "select mid,type from comment where id in (select id from comment where fatherId = 0 ) and uid != ? order by createTimeStamp ";
//		List<Map<String, Object>> list2 =  this.jdbcTemplate.queryForList(sql2,nowUid);//获取直接评论信息的、且非该用户发表的评论
//			
//		List<Map<String, Object>> list3 = getRepetition(list,list2);//取两者重复项，再到数据库中查找
//		List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();	
//		
//		for (Map<String, Object> map : list3) {//遍历list3  
//			String sql4 = "select * from comment where mid  = ? and type = ? order by createTimeStamp";	
//			list4.addAll(this.jdbcTemplate.queryForList(sql4, (Integer)map.get("mid"),(Integer)map.get("type")));
//        }  
//		
//		return list4;
//	}
		public List<Map<String, Object>> getUnreadComment(String nowUid, Integer start, Integer count) {//未读的评论消息（指代）	
			
			String sql1;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			String tableName[]= {"question", "experienceshare", "rewardhelp", "schoolactivity", "secondarymarket"};
			for (int i = 0; i < 5 ; i++) {
				sql1 = "select id as mid,type from "+tableName[i]+" where uid  = ? order by createTimeStamp desc";	
				list.addAll(this.jdbcTemplate.queryForList(sql1, nowUid));
			}//获取用户发布的所有信息id与type
			
			String sql2 = "select * from comment where id = ? and type = ? and uid != ? order by createTimeStamp desc";
			for (Map<String, Object> map : list) {
				result.addAll(this.jdbcTemplate.queryForList(sql2, map.get("mid").toString(), map.get("type").toString(),nowUid));
			}
			
			return result;
		}


		public int setUserInfo( String nowUid,String nickName,  String bio,String school, String wechat, String qq, Integer sex) {
			String sql = "update user set nickName = ? , school = ? ,bio = ? , wechat = ? ,qq = ? ,sex = ?  where id = ?";
			return this.jdbcTemplate.update(sql, nickName, school, bio, wechat, qq , sex, nowUid);
		}


		public int toggleShowQQ(String nowUid, Integer flag) {
			String sql = "update user set showQQ = ? where id = ?";
			return this.jdbcTemplate.update(sql, flag, nowUid);
		}


		public int toggleShowWechat(String nowUid, Integer flag) {
			String sql = "update user set showWechat = ? where id = ?";
			return this.jdbcTemplate.update(sql, flag, nowUid);
		}


		public String getDeleteImgUrl(Integer mid, Integer type) {
		
			String sql = "select picUrl from tableview where id = ? and type = ?";
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, mid, type);
			return list.get(0).get("picUrl").toString();
		}






	
}
