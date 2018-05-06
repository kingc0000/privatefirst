package com.kekeinfo.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.PinYinName;
import com.kekeinfo.web.entity.PinYinUser;
import com.kekeinfo.web.entity.PinYinUserName;

public class PinyinUtils {
	
	
	public static List<PinYin> getPinyinList(List<Object[]> objs){
		String[] firstLetter={
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
				"r","s","t","u","v","w","x","y","z"};
		List<String> ch =new ArrayList<String>(Arrays.asList(firstLetter));
		List<PinYin> lists = new ArrayList<PinYin>();
		if(objs !=null && objs.size()>0){	
			
			PinYin pinYin = new PinYin();
			//pinYin.setCode("A");
			String lett ="A";
			int lastLett =(int) ch.get(0).charAt(0);
			for(int i=0;i<objs.size();i++){
				int letter = (int)objs.get(i)[2].toString().toLowerCase().charAt(0);
				if(letter>lastLett){
					
					if(lett.equalsIgnoreCase(String.valueOf((char)(letter)).toUpperCase())){
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter)).toUpperCase());
					}else{
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter-1)).toUpperCase());
					}
					
					if(pinYin.getLists()!=null && pinYin.getLists().size()>0)
					lists.add(pinYin);
					//lists = new ArrayList<PinYin>();
					/**
					if(ch.size()>0){
						ch.remove(0);
					}*/
					
					for(int k=0;k<ch.size();k++){
						lastLett =(int) ch.get(k).charAt(0);
						if(letter>lastLett) {
							ch.remove(0);
							 k--;
						}else{
							break;
						}
					}
					lett=objs.get(i)[2].toString().toUpperCase();
					pinYin = new PinYin();
					//pinYin.setCode(objs.get(i)[2].toString().toUpperCase());
					PinYinName pinYinName = new PinYinName();
					pinYinName.setId(objs.get(i)[0].toString());
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					List<PinYinName> plist = new ArrayList<PinYinName>(); 
					plist.add(pinYinName);
					pinYin.setLists(plist);
					
				}else{
					PinYinName pinYinName = new PinYinName();
					pinYinName.setId(objs.get(i)[0].toString());
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					List<PinYinName> plist = pinYin.getLists();
					if(plist == null) plist = new ArrayList<PinYinName>();
					plist.add(pinYinName);
					pinYin.setLists(plist);					
				}
				
			}
			
			//if(lists !=null && lists.size()>0){
				int letter = (int)objs.get(objs.size()-1)[2].toString().toLowerCase().charAt(0);
				//maps.put(lett+"-"+String.valueOf((char)letter).toUpperCase(), lists);
				pinYin.setCode(lett+"-"+String.valueOf((char)letter).toUpperCase());
				lists.add(pinYin);
			//}
		}
		
		return lists;
	}

	public static List<PinYinUserName> getPinyinUserList(List<Object[]> objs){
		String[] firstLetter={
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
				"r","s","t","u","v","w","x","y","z"};
		List<String> ch =new ArrayList<String>(Arrays.asList(firstLetter));
		List<PinYinUserName> lists = new ArrayList<PinYinUserName>();
		if(objs !=null && objs.size()>0){	
			
			PinYinUserName pinYin = new PinYinUserName();
			//pinYin.setCode("A");
			String lett ="A";
			int lastLett =(int) ch.get(0).charAt(0);
			for(int i=0;i<objs.size();i++){
				int letter = (int)objs.get(i)[2].toString().toLowerCase().charAt(0);
				if(letter>lastLett){
					
					if(lett.equalsIgnoreCase(String.valueOf((char)(letter)).toUpperCase())){
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter)).toUpperCase());
					}else{
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter-1)).toUpperCase());
					}
					
					if(pinYin.getLists()!=null && pinYin.getLists().size()>0)
					lists.add(pinYin);
					//lists = new ArrayList<PinYin>();
					/**
					if(ch.size()>0){
						ch.remove(0);
					}*/
					
					for(int k=0;k<ch.size();k++){
						lastLett =(int) ch.get(k).charAt(0);
						if(letter>lastLett) {
							ch.remove(0);
							 k--;
						}else{
							break;
						}
					}
					lett=objs.get(i)[2].toString().toUpperCase();
					pinYin = new PinYinUserName();
					//pinYin.setCode(objs.get(i)[2].toString().toUpperCase());
					PinYinUser pinYinName = new PinYinUser();
					pinYinName.setId(objs.get(i)[0].toString());
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					pinYinName.setPhone(objs.get(i)[3].toString().toUpperCase());
					List<PinYinUser> plist = new ArrayList<PinYinUser>(); 
					plist.add(pinYinName);
					pinYin.setLists(plist);
					
				}else{
					PinYinUser pinYinName = new PinYinUser();
					pinYinName.setId(objs.get(i)[0].toString());
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					if(objs.get(i)[3]!=null){
						pinYinName.setPhone(objs.get(i)[3].toString().toUpperCase());
					}
					List<PinYinUser> plist = pinYin.getLists();
					if(plist == null) plist = new ArrayList<PinYinUser>();
					plist.add(pinYinName);
					pinYin.setLists(plist);					
				}
				
			}
			
			//if(lists !=null && lists.size()>0){
				int letter = (int)objs.get(objs.size()-1)[2].toString().toLowerCase().charAt(0);
				//maps.put(lett+"-"+String.valueOf((char)letter).toUpperCase(), lists);
				pinYin.setCode(lett+"-"+String.valueOf((char)letter).toUpperCase());
				lists.add(pinYin);
			//}
		}
		
		return lists;
	}
}
