package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.projectimg.dao.DeformmonitorMarkerDao;
import com.kekeinfo.core.business.projectimg.model.DeformmonitorMarker;

/**
 * Created by WangChong on 2018/5/10.
 */
@Service
public class DeformmonitorMarkerServiceImpl extends KekeinfoEntityServiceImpl<Long, DeformmonitorMarker> implements
        DeformmonitorMarkerService {

    private DeformmonitorMarkerDao deformmonitorMarkerDao;

    @Autowired
    public DeformmonitorMarkerServiceImpl(DeformmonitorMarkerDao deformmonitorMarkerDao) {
        super(deformmonitorMarkerDao);
        this.deformmonitorMarkerDao = deformmonitorMarkerDao;
    }
}
