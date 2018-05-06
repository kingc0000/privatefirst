package com.kekeinfo.core.business.last.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.last.dao.MonitLastDao;
import com.kekeinfo.core.business.last.model.MonitLast;

@Service("monitlastService")
public class MonitLastServiceImpl extends KekeinfoEntityServiceImpl<Long, MonitLast> implements MonitLastService {
	private MonitLastDao monitlastDao;

	@Autowired
	public MonitLastServiceImpl(MonitLastDao monitlastDao) {
		super(monitlastDao);
		this.monitlastDao = monitlastDao;
	}

	@Override
	public List<MonitLast> getByUserID(long userid) throws ServiceException {
		// TODO Auto-generated method stub
		return monitlastDao.getByUserID(userid);
	}

	@Override
	public void saveNew(MonitLast clast) throws ServiceException {
		// TODO Auto-generated method stub
				List<MonitLast> clist=this.getByUserID(clast.getUid());
				this.save(clast);
				if(clast!=null){
					boolean found =false;
					for(MonitLast cl:clist){
						if(cl.getMid()==clast.getMid()){
							cl.setDateModified(clast.getDateModified());
							this.delete(cl);
							found =true;
							break;
						}
					}
					if(!found){
						if(clist.size()>4){
							for(int i=4;i<clist.size();i++){
								this.delete(clist.get(i));
							}
						}
					}
				}
			}
	
}
