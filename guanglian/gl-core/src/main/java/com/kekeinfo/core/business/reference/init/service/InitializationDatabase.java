package com.kekeinfo.core.business.reference.init.service;

import com.kekeinfo.core.business.generic.exception.ServiceException;

public interface InitializationDatabase {
	boolean isEmpty();
		
	void populate(String name) throws ServiceException;

}
