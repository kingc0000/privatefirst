package com.kekeinfo.web.init.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.generic.exception.ServiceException;

@Component
public class InitStoreData implements InitData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitStoreData.class);

	public void initInitialData() throws ServiceException {
		LOGGER.info("Starting the initialization of test data");
		LOGGER.info("Ending the initialization of test data");
		
	}
}
