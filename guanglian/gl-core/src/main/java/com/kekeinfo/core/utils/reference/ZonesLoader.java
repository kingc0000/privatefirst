package com.kekeinfo.core.utils.reference;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.zone.model.Zone;


@Component
public class ZonesLoader {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ZonesLoader.class);
	
	
	
	public Map<String, Zone> loadZones(String jsonFilePath) throws Exception {
		

		
		
		
		ObjectMapper mapper = new ObjectMapper();

        try {

              InputStream in =
                    this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);

              @SuppressWarnings("unchecked")
              Map<String,Object> data = mapper.readValue(in, Map.class);
              
              Map<String,Zone> zonesMap = new HashMap<String,Zone>();
              
                  @SuppressWarnings("rawtypes")
	            
	              List langList = (List)data.get("zh");
	              if(langList!=null) {
		              for(Object z : langList) {
		                    @SuppressWarnings("unchecked")
							Map<String,String> e = (Map<String,String>)z;
		                    String zoneCode = e.get("zoneCode");
		                    Zone zone = null;
		                    if(!zonesMap.containsKey(zoneCode)) {
		                    	zone = new Zone();
		                    	zonesMap.put(zoneCode, zone);
		                    	zone.setCode(zoneCode);
		                    	zone.setName(e.get("zoneName"));

		                    }		              
	                    }
		             }
              return zonesMap;
              
  			
  		} catch (Exception e) {
  			throw new ServiceException(e);
  		}
  		
  		

		
	
	
	
	}

}
