package com.levelup.jmsinfo.config.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @purpose ê³µí†µ ë¡œì§ 
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?        ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ? •ë¯¼ì„œ        ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.24
 *
 */
@Service
public class CommonService {
	
	/**
     * Requestë¡? ë°›ì? ID?? ?Œ¨?Š¤?›Œ?“œ ê¸°ë°˜?œ¼ë¡? ?† ?°?„ ë°œê¸‰?•œ?‹¤.
     *
     * @param jsonString : json string ê°?
     * @param key : json ì¡°íšŒ key
     * @return json?—?„œ ë½‘ì? value
     */
	public String getJsonData(String jsonString, String key) {
		
		JSONObject jsonObject = new JSONObject(jsonString);
		String result = jsonObject.get(key).toString();

		return result;
	}
	
}
