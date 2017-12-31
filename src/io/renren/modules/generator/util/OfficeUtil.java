package io.renren.modules.generator.util;

import io.renren.common.Config;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OfficeUtil {
	public static String uploadFile(String fileName) throws IOException{
		String uuid=null;
		String apiKey = Config.OFFICE_API_KEY;
		File file = new File(fileName);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
		    HttpPost httppost = new HttpPost("http://view.webofficeapi.com/upload");

		    StringBody apiKeyPart = new StringBody(apiKey, ContentType.TEXT_PLAIN);
		    FileBody filePart = new FileBody(file);

		    HttpEntity reqEntity = MultipartEntityBuilder.create()
		            .addPart("api_key", apiKeyPart)
		            .addPart("file", filePart)
		            .build();

		    httppost.setEntity(reqEntity);

		    System.out.println("executing request " + httppost.getRequestLine());
		    CloseableHttpResponse response = httpclient.execute(httppost);
		    try {
		        System.out.println("----------------------------------------");
		        System.out.println(response.getStatusLine());
		        HttpEntity resEntity = response.getEntity();
		        if (resEntity != null) {
		            System.out.println("Response content length: " + resEntity.getContentLength());
		        }
		       String jsonStr= EntityUtils.toString(resEntity);
		       JSONObject  json= JSONObject.fromObject(jsonStr);
		        uuid = json.getString("uuid").trim();
		    } finally {
		        response.close();
		    }
		} finally {
		    httpclient.close();
		}
		return uuid;
	}
	


}
