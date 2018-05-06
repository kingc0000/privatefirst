package com.kekeinfo.core.business.camera.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;

@Repository("cameraDao")
public class CameraDaoImpl extends KekeinfoEntityDaoImpl<Long, Camera> implements CameraDao {

	public CameraDaoImpl() {
		super();
	}
	
}
