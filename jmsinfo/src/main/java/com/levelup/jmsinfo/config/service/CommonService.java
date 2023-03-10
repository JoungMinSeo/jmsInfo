package com.levelup.jmsinfo.config.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @purpose κ³΅ν΅ λ‘μ§ 
 * 
 * @  ?? ?Ό            ?? ?        ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ? λ―Όμ        μ΅μ΄??±
 *
 * @author ? λ―Όμ
 * @since  2023.02.24
 *
 */
@Service
public class CommonService {
	
	/**
     * Requestλ‘? λ°μ? ID?? ?¨?€?? κΈ°λ°?Όλ‘? ? ?°? λ°κΈ??€.
     *
     * @param jsonString : json string κ°?
     * @param key : json μ‘°ν key
     * @return json?? λ½μ? value
     */
	public String getJsonData(String jsonString, String key) {
		
		JSONObject jsonObject = new JSONObject(jsonString);
		String result = jsonObject.get(key).toString();

		return result;
	}
	
}
