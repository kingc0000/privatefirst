package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.projectimg.dao.ProjectImgDao;
import com.kekeinfo.core.business.projectimg.dao.PumpWellMarkerDao;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;
import com.kekeinfo.core.business.projectimg.model.PumpWellMarker;

/**
 * Created by WangChong on 2018/5/6.
 */
@Service
public class PumpWellMarkerServiceImpl extends KekeinfoEntityServiceImpl<Long, PumpWellMarker> implements PumpWellMarkerService {

    private PumpWellMarkerDao pumpWellMarkerDao;

    @Autowired
    public PumpWellMarkerServiceImpl(PumpWellMarkerDao pumpWellMarkerDao) {
        super(pumpWellMarkerDao);
        this.pumpWellMarkerDao = pumpWellMarkerDao;
    }

}
