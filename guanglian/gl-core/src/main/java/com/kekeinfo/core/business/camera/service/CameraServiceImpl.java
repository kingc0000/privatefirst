package com.kekeinfo.core.business.camera.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.camera.dao.CameraDao;
import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;

@Service("cameraService")
public class CameraServiceImpl extends KekeinfoEntityServiceImpl<Long, Camera> implements CameraService {
	
	@SuppressWarnings("unused")
	private CameraDao cameraDao;
	
	@Autowired
	public CameraServiceImpl(CameraDao cameraDao) {
		super(cameraDao);
		this.cameraDao = cameraDao;
	}
}
