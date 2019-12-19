package com.itheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * 七牛云测试类
 * 版本：7.2.0
 *
 * @author wangxin
 * @version 1.0
 */
public class QiNiuTest {

    /**
     * 七牛云上传本地文件测试
     */
    //@Test
    public void testUpload() {
        //构造一个带指定 Region 对象的配置类
        //Zone.zone0() 区域配置 华东地区
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "A_jKJnB1bpEPHn1QdqzPpelrCPU6QfJbJnv-_RR4";
        String secretKey = "CldWf-r2Z6mEkuqQD8zEOVj5U_jIRK-Dcea6T9oB";
        String bucket = "health-itheima81";//存储空间名称
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\admin\\Desktop\\aaa.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "bbb.jpg";

        Auth auth = Auth.create(accessKey, secretKey);//鉴权
        String upToken = auth.uploadToken(bucket);//上传文件

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }


    /**
     * 删除文件测试
     */
   //@Test
    public void testDel(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释

        String accessKey = "A_jKJnB1bpEPHn1QdqzPpelrCPU6QfJbJnv-_RR4";
        String secretKey = "CldWf-r2Z6mEkuqQD8zEOVj5U_jIRK-Dcea6T9oB";
        String bucket = "health-itheima81";//存储空间名称
        String key = "bbb.jpg";//需要删除的文件名称

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }
}
