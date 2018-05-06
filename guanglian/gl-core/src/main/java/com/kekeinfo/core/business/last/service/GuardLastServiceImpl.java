package com.kekeinfo.core.business.last.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.last.dao.GuardLastDao;
import com.kekeinfo.core.business.last.model.GuardLast;

@Service("guardlastService")
public class GuardLastServiceImpl extends KekeinfoEntityServiceImpl<Long, GuardLast> implements GuardLastService {
	private GuardLastDao guardlastDao;

	@Autowired
	public GuardLastServiceImpl(GuardLastDao guardlastDao) {
		super(guardlastDao);
		this.guardlastDao = guardlastDao;
	}

	@Override
	public List<GuardLast> getByUserID(long userid) throws ServiceException {
		// TODO Auto-generated method stub
		return guardlastDao.getByUserID(userid);
	}

	@Override
	public void saveNew(GuardLast clast) throws ServiceException {
		// TODO Auto-generated method stub
		List<GuardLast> clist=this.getByUserID(clast.getUid());
		this.save(clast);
		if(clast!=null){
			boolean found =false;
			for(GuardLast cl:clist){
				if(cl.getGid()==clast.getGid()){
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
