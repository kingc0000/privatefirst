package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.projectimg.dao.ObserveWellMarkerDao;
import com.kekeinfo.core.business.projectimg.dao.PumpWellMarkerDao;
import com.kekeinfo.core.business.projectimg.model.ObserveWellMarker;
import com.kekeinfo.core.business.projectimg.model.PumpWellMarker;

/**
 * Created by WangChong on 2018/5/6.
 */
@Service
public class ObserveWellMarkerServiceImpl extends KekeinfoEntityServiceImpl<Long, ObserveWellMarker> implements ObserveWellMarkerService {

    private ObserveWellMarkerDao observeWellMarkerDao;

    @Autowired
    public ObserveWellMarkerServiceImpl(ObserveWellMarkerDao observeWellMarkerDao) {
        super(observeWellMarkerDao);
        this.observeWellMarkerDao = observeWellMarkerDao;
    }


}
