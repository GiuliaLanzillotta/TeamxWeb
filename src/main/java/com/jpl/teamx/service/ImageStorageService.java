package com.jpl.teamx.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class ImageStorageService {
	
	@Autowired
	private AwsService awsService;
	private static final String bucketName = "teamx-images";
	
	public ImageStorageService() {
		
	}
	
	
	public String storeImage(MultipartFile file, String s3ObjectKey) {
		AmazonS3 s3client = awsService.getS3client();
		try {
			InputStream is = file.getInputStream();
			
			//store s3 file
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("image");
			s3client.putObject(new PutObjectRequest(bucketName, s3ObjectKey, is,metadata).withCannedAcl(CannedAccessControlList.PublicRead));
			S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName,s3ObjectKey));
			String picUrl = s3Object.getObjectContent().getHttpRequest().getURI().toString();  
			return picUrl;
		}
		catch(IOException e) {
			e.printStackTrace();
			return "error during uploading photos ";
		}
	}

}
