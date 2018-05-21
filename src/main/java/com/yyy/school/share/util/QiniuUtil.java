package com.yyy.school.share.util;

import java.io.IOException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiniuUtil {  

     //设置需要操作的账号的AK和SK
     private static final String ACCESS_KEY = "TuH0B0O95mkXeHk8V8oAwjC4UtEFf-rMffUypEeq";   
     private static final String SECRET_KEY = "i9Nwv7DlzGudpe43lj35XmpVbH5pn3x3xxB6XGyQ";  
     //要上传的空间  
     private static final String bucketname = "schoolshare";      
     //密钥  
     private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);  

     //普通上传  
     public void upload(String filePath, String fileName) throws IOException{  
        //创建上传对象  
        UploadManager uploadManager = new UploadManager();
        try {  
           //调用put方法上传  
           Response res = uploadManager.put(filePath, fileName, auth.uploadToken(bucketname));  
           //打印返回的信息  
           System.out.println(res.toString());   
           } catch (QiniuException e) {  
               Response r = e.response;  
              // 请求失败时打印的异常的信息  
              System.out.println(r.toString());  
              try {  
                  //响应的文本信息  
                System.out.println(r.bodyString());  
              } catch (QiniuException e1) {  
                  //ignore  
              }  
          }         
      }   

     //普通删除
     public void delete(String key) throws IOException {
         //实例化一个BucketManager对象
         BucketManager bucketManager = new BucketManager(auth);
         //此处的33是去掉：http://ongsua0j7.bkt.clouddn.com/,剩下的key就是图片在七牛云的名称
         key = key.substring(33);
         //System.out.println("key---------->" + key);
         try {
              //调用delete方法移动文件
              bucketManager.delete(bucketname, key);
            } catch (QiniuException e) {
              //捕获异常信息
              Response r = e.response;
              System.out.println(r.toString());
            }
     }
}
