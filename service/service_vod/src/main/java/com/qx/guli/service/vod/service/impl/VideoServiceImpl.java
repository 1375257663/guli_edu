package com.qx.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.qx.guli.common.base.result.ResultCodeEnum;
import com.qx.guli.service.vod.service.VideoService;
import com.qx.guli.service.vod.util.AliyunVodSDKUtils;
import com.qx.guli.service.vod.util.VodProperties;
import com.qx.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Classname VideoServiceImpl
 * @Description TODO
 * @Date 2020/6/16 23:52
 * @Created by 卿星
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(MultipartFile file) {

        try {
            // 文件标题
            String title = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            // 文件流
            InputStream inputStream = file.getInputStream();
            // 获取文件完整名
            String originalFilename = file.getOriginalFilename();
            // 设置请求参数
            UploadStreamRequest request = new UploadStreamRequest(vodProperties.getKeyid(), vodProperties.getKeysecret(), title, originalFilename, inputStream);
//            UploadVideoRequest request = new UploadVideoRequest(vodProperties.getKeyid(), vodProperties.getKeysecret(), title, originalFilename);
//             创建上传文件类
            UploadVideoImpl uploader = new UploadVideoImpl();
            // 设置模板组id
//            request.setTemplateGroupId(vodProperties.getTemplateGroupId());
            // 设置工作流id
//            request.setWorkflowId(vodProperties.getWorkflowId());
            // 上传文件
            UploadStreamResponse response = uploader.uploadStream(request);
//            UploadVideoResponse response = uploader.uploadVideo(request);
//             获取
            String requestId = response.getRequestId();
            // 获取videoId
            String videoId = response.getVideoId();
            System.out.println("requestId = " + requestId + "\n");
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            // 没有正确的返回videoId说明上传失败
            if(!StringUtils.isEmpty(videoId)){
                return videoId;
            } else {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
                log.info("视频上传失败！","message: "+response.getMessage(),"code:"+response.getCode());
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

    }

    @Override
    public boolean removeVideoById(String id) {

        // 设置接受响应
        DeleteVideoResponse response = null;
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
            //        DeleteVideoResponse response = new DeleteVideoResponse();

            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(id);
            // 返回响应
            response  = client.getAcsResponse(request);
            System.out.print("RequestId = " + response.getRequestId() + "\n");
            return true;
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }

    }

    @Override
    public void removeMediaVideoByIdList(List<String> idList) throws ClientException {
        // 创建client
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

        // 创建StringBuilder
        StringBuilder sourceIds = new StringBuilder();
        // 创建请求对象
        DeleteVideoRequest request = new DeleteVideoRequest();
        int size = idList.size();
        // 遍历取出sourceId
        for(int i = 0; i < size; i++){
            String sourceId = idList.get(i);
            sourceIds.append(sourceId);
            // 如果StringBuilder中存储了20个则上传一次
            // 当上传文件为最后一个，或删除文件满20时，都将执行删除
            if( i == size -1 || i % 20 == 19){
                //支持传入多个视频ID，多个用逗号分隔,最多20个
                request.setVideoIds(sourceIds.toString());
                // 执行删除
                client.getAcsResponse(request);
                // 删除后新建StringBuilder对象,再添加数据
                sourceIds = new StringBuilder();
            } else if (i % 20 < 19){
                sourceIds.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {

        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

//        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            // 设置数据源ID
            request.setVideoId(videoSourceId);
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            // 返回凭证
            return response.getPlayAuth();
    }

}
