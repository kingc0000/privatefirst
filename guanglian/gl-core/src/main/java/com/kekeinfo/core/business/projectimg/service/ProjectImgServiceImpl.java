package com.kekeinfo.core.business.projectimg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.projectimg.dao.ProjectImgDao;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;

/**
 * Created by WangChong on 2018/5/6.
 */
@Service
public class ProjectImgServiceImpl extends KekeinfoEntityServiceImpl<Long, ProjectImg> implements ProjectImgService {

    private ProjectImgDao projectImgDao;

    @Autowired
    public ProjectImgServiceImpl(ProjectImgDao projectImgDao) {
        super(projectImgDao);
        this.projectImgDao = projectImgDao;
    }


}
