package com.kekeinfo.core.modules.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * {"audits":[{"id", "1"},{"id", "2"}, {"id", "3"}], "approves":[{"id", "1"},{"id", "2"}, {"id", "3"}]}
 * @author sam
 *
 */
public class DreportConfig implements JSONAware {

	private List<Map<String, String>> audits = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> approves = new ArrayList<Map<String, String>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject data = new JSONObject();
		data.put("audits", this.getAudits());
		data.put("approves", this.getApproves());
		return data.toJSONString();
	}

	public List<Map<String, String>> getAudits() {
		return audits;
	}

	public void setAudits(List<Map<String, String>> audits) {
		this.audits = audits;
	}

	public List<Map<String, String>> getApproves() {
		return approves;
	}

	public void setApproves(List<Map<String, String>> approves) {
		this.approves = approves;
	}
	
	public static void main(String[] args) {
		DreportConfig config = new DreportConfig();
		for (int i = 0; i < 3; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "id_"+i);
			config.getAudits().add(map);
			config.getApproves().add(map);
		}
		System.out.println(config.toJSONString());
	}

}
