package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.projectimg.dao.InvertedWellMarkerDao;
import com.kekeinfo.core.business.projectimg.dao.ObserveWellMarkerDao;
import com.kekeinfo.core.business.projectimg.model.InvertedWellMarker;

/**
 * Created by WangChong on 2018/5/10.
 */
@Service
public class InvertedWellMarkerServiceImpl extends KekeinfoEntityServiceImpl<Long, InvertedWellMarker> implements
        InvertedWellMarkerService{

    private InvertedWellMarkerDao invertedWellMarkerDao;

    @Autowired
    public InvertedWellMarkerServiceImpl(InvertedWellMarkerDao invertedWellMarkerDao) {
        super(invertedWellMarkerDao);
        this.invertedWellMarkerDao = invertedWellMarkerDao;
    }
}
