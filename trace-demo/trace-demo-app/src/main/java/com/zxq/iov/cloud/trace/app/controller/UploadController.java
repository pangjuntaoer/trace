package com.zxq.iov.cloud.trace.app.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zxq.iov.oss.OssException;
import com.zxq.iov.oss.UploadResult;
import com.zxq.iov.oss.storage.OssHelper;

@Controller
@RequestMapping("/upload")
public class UploadController {
	
	@RequestMapping(method=RequestMethod.POST)
	public void uploadFile() {
		
        String localFilePath = "e:/software/animal527.jpg";
        File fileObject = new File(localFilePath);
        try {
        	OssHelper ossHelper = OssHelper.newInstance();
            UploadResult uploadResult =  ossHelper.upload(fileObject);

            String privateDownloadUrl = ossHelper.privateDownloadUrl(uploadResult.getKey());
            System.out.println("key: " + uploadResult.getKey());
            System.out.println(privateDownloadUrl);
            //String publicDownloadUrl = ossHelper.publicDownloadUrl(uploadResult.getKey());
        } catch (OssException e) {
        	e.printStackTrace();
        }
	}

}
