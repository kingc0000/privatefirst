package com.kekeinfo.core.utils;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.generic.exception.ServiceException;

@Component("cache")
public class CacheUtils {
	
    @Inject
    @Qualifier("serviceCache")
    private Cache cache;
	
	public final static String REFERENCE_CACHE = "REF";
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
//
//	private final static String KEY_DELIMITER = "_";
	


	public void putInCache(Object object, String keyName) throws ServiceException {
		cache.put(keyName, object);
	}
	

	public Object getFromCache(String keyName) throws ServiceException {
		ValueWrapper vw = cache.get(keyName);
		if(vw!=null) {
			return vw.get();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCacheKeys() throws ServiceException {
		
		  net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache) cache.getNativeCache();
		  return cacheImpl.getKeys();
//		  List<String> returnKeys = new ArrayList<String>();
//		  for (Object key: cacheImpl.getKeys()) {
//				try {
//					String sKey = (String)key;
//					
//					// a key should be <storeId>_<rest of the key>
//					int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
//					
//					if(delimiterPosition>0 && Character.isDigit(sKey.charAt(0))) {
//					
//						String keyRemaining = sKey.substring(delimiterPosition+1);
//						returnKeys.add(keyRemaining);
//					}
//
//				} catch (Exception e) {
//					LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
//				}  
//		  }
//
//		return returnKeys;
	}
	
	public void shutDownCache() throws Exception {
		
	}
	
	public void removeFromCache(String keyName) throws ServiceException {
		cache.evict(keyName);
	}
	
	public void removeAllFromCache() throws ServiceException {
		  net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache) cache.getNativeCache();
		  cacheImpl.removeAll();
//		  for (Object key: cacheImpl.getKeys()) {
//				try {
//					String sKey = (String)key;
//					cache.evict(key);
					// a key should be <storeId>_<rest of the key>
//					int delimiterPosition = sKey.indexOf(KEY_DELIMITER);
//					
//					if(delimiterPosition>0 && Character.isDigit(sKey.charAt(0))) {
//						cache.evict(key);
//					}
//				} catch (Exception e) {
//					LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
//				}  
//		  }
	}
	


}
