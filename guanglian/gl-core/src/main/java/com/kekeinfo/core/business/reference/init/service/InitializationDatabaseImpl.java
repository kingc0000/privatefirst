package com.kekeinfo.core.business.reference.init.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.system.model.IntegrationModule;
import com.kekeinfo.core.business.system.service.ModuleConfigurationService;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.core.utils.reference.IntegrationModulesLoader;
import com.kekeinfo.core.utils.reference.ZonesLoader;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);
	

	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	protected DepartmentService merchantService;
		
	
		
	@Autowired
	private ZonesLoader zonesLoader;
	
	@Autowired
	private IntegrationModulesLoader modulesLoader;
	
	@Autowired
	private ModuleConfigurationService moduleConfigurationService;
	
	
	
	private String name;
	
	
	
	@Transactional
	public void populate(String contextName) throws ServiceException {
		this.name =  contextName;
		
		createZones();		
		
		createModules();
		
	}
	
	
 		
	private void createZones() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Zones ", name));
        try {

    		  Map<String,Zone> zonesMap = new HashMap<String,Zone>();
    		  zonesMap = zonesLoader.loadZones("reference/zoneconfig.json");
              
    		  for (Map.Entry<String, Zone> entry : zonesMap.entrySet()) {
           	   
            	  Zone value = entry.getValue();
          	      zoneService.create(value);
              
              }

  		} catch (Exception e) {
  		    
  			throw new ServiceException(e);
  		}

	}


	private void createModules() throws ServiceException {
		
		try {
			
			List<IntegrationModule> modules = modulesLoader.loadIntegrationModules("reference/integrationmodules.json");
            for (IntegrationModule entry : modules) {
        	    moduleConfigurationService.create(entry);
          }
			
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return zoneService.count()==0;
		//return languageService.count() == 0;
		//return false;
	}
	

	



}
