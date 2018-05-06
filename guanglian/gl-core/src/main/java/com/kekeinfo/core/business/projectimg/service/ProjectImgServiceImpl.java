/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.kekeinfo.core.business.projectimg.service;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDao;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.projectimg.dao.ProjectImgDao;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;

/**
 * Created by WangChong on 2018/5/6.
 */
public class ProjectImgServiceImpl extends KekeinfoEntityServiceImpl<Long, ProjectImg> implements ProjectImgService {

    private ProjectImgDao projectImgDao;

    public ProjectImgServiceImpl(ProjectImgDao projectImgDao) {
        super(projectImgDao);
    }
}
