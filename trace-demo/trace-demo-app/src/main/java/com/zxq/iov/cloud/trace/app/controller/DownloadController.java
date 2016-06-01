package com.zxq.iov.cloud.trace.app.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zxq.iov.oss.storage.OssHelper;

@Controller
@RequestMapping("/download")
public class DownloadController {

	@RequestMapping(method=RequestMethod.POST)
	public void downloadFile() throws IOException {
		OssHelper ossHelper = OssHelper.newInstance();
        String key = "a7290090e3a247fa92e72fa8843aa79a.jpg";
        byte[] objBytes = ossHelper.downloadPrivate(key);
        
        
        File file = new File("e:/software/animal528.jpg");
        OutputStream output = new FileOutputStream(file);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write(objBytes);
        bufferedOutput.flush();
        bufferedOutput.close();
        output.close();
	}
	
}
