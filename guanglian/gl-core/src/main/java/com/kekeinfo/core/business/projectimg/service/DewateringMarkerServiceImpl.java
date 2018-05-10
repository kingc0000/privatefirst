package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.projectimg.dao.DewateringMarkerDao;
import com.kekeinfo.core.business.projectimg.model.DewateringMarker;

/**
 * Created by WangChong on 2018/5/10.
 */
@Service
public class DewateringMarkerServiceImpl extends KekeinfoEntityServiceImpl<Long, DewateringMarker>
        implements DewateringMarkerService {

    private DewateringMarkerDao dewateringMarkerDao;

    @Autowired
    public DewateringMarkerServiceImpl(DewateringMarkerDao dewateringMarkerDao) {
        super(dewateringMarkerDao);
        this.dewateringMarkerDao = dewateringMarkerDao;
    }
}
