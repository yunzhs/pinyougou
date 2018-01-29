package com.pinyougou.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * 短信发送的消费者
 * @author msj
 * 
 */
@Component
public class SmsListener {

	
	@Autowired
	private SmsUtil smsUtil;
	
	
	@JmsListener(destination="itcast_sms")
	public void sendSms(Map map){
		//获取activeMQ中的数据
		String mobile=(String) map.get("mobile");
		String sign_name=(String) map.get("sign_name");
		String template_code=(String) map.get("template_code");
		String param=(String) map.get("param");
        System.out.println(mobile);
        System.out.println(sign_name);
        System.out.println(template_code);
        System.out.println(param);
        try {
			//调用短信发送工具发送短信
			SendSmsResponse sendSms = smsUtil.sendSms(mobile, sign_name, template_code, param);
			System.out.println(sendSms.getCode());
			System.out.println(sendSms.getMessage());
			System.out.println(sendSms.getRequestId());
			System.out.println(sendSms.getBizId());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
