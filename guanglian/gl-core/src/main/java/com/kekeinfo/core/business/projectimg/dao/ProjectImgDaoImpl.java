package com.kekeinfo.core.business.projectimg.dao;

import org.springframework.stereotype.Repository;

import com.kekeinfo.core.business.generic.dao.KekeinfoEntityDaoImpl;
import com.kekeinfo.core.business.project.dao.ProjectDao;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;

/**
 * Created by WangChong on 2018/5/6.
 */
@Repository
public class ProjectImgDaoImpl extends KekeinfoEntityDaoImpl<Long, ProjectImg> implements ProjectImgDao {

}
