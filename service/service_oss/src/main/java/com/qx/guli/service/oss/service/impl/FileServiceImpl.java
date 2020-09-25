package com.qx.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.qx.guli.service.oss.service.FileService;
import com.qx.guli.service.oss.util.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Classname FileServiceImpl
 * @Description TODO
 * @Date 2020/6/7 17:17
 * @Created by 卿星
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFileName) {
        // Endpoint北京
        String endpoint = ossProperties.getEndpoint();
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        String bucketName = ossProperties.getBucketname();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 判断oss实例是否存在，如果不存在则创建，如果存在则获取
        if(!ossClient.doesBucketExist(bucketName)){
            // 创建bucket
            ossClient.createBucket(bucketName);
            // 设置oss的访问权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        // 生成文件

        // 生成日期格式文件夹+文件:avatar/2019/02/26/文件名
        String folder = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        // 文件名
        String fileName = UUID.randomUUID().toString().replace("-","");
        System.out.println(originalFileName);
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String key = module +"/"+ folder +"/"+ fileName +fileExtension;


        // 创建PutObjectRequest对象。
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new File(folder));
//
        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(bucketName,key,inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        // https://crowdfundingqx.oss-cn-beijing.aliyuncs.com/20200527/0a593ddccb6e4a418af987d62ceff0bd.png
        return "http://"+bucketName+"."+endpoint+"/"+key;
    }

    @Override
    public void removeFile(String url) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        String bucketName = ossProperties.getBucketname();

        // https://online-teach-file
        // https://online-teach-file.oss-cn-beijing.aliyuncs.com/teacher/2019/10/30/65423f14-49a9-4092-baf5-6d0ef9686a85.png
        // https://guli-edu-qx.oss-cn-beijing.aliyuncs.com/avator/2020/06/08/8e019ba42e7e4ec7b04f5951d4035db9.jpg
        String host = "http://"+bucketName+"."+endpoint+"/";
        // 切割得到文件相对地址,substring()方法只传一个参数是切割参数及其后面
        System.out.println(host.length());
        System.out.println("url"+url);
        System.out.println("host"+host);
        String objectName = url.substring(host.length());
        System.out.println("objectName"+objectName);
// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

// 关闭OSSClient。
        ossClient.shutdown();
    }
}
