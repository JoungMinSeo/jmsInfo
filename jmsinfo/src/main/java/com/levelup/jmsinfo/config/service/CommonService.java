package com.levelup.jmsinfo.config.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @purpose 공통 로직 
 * 
 * @  ?��?��?��            ?��?��?��        ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ?��민서        최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.24
 *
 */
@Service
public class CommonService {
	
	/**
     * Request�? 받�? ID?? ?��?��?��?�� 기반?���? ?��?��?�� 발급?��?��.
     *
     * @param jsonString : json string �?
     * @param key : json 조회 key
     * @return json?��?�� 뽑�? value
     */
	public String getJsonData(String jsonString, String key) {
		
		JSONObject jsonObject = new JSONObject(jsonString);
		String result = jsonObject.get(key).toString();

		return result;
	}
	
}
