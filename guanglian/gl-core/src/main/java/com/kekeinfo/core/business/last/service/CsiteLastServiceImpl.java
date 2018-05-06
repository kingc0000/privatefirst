package com.kekeinfo.core.business.last.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.last.model.CsiteLast;
import com.kekeinfo.core.business.last.dao.CsiteLastDao;

@Service("csitelastService")
public class CsiteLastServiceImpl extends KekeinfoEntityServiceImpl<Long, CsiteLast> implements CsiteLastService {
	private CsiteLastDao csitelastDao;

	@Autowired
	public CsiteLastServiceImpl(CsiteLastDao csitelastDao) {
		super(csitelastDao);
		this.csitelastDao = csitelastDao;
	}

	@Override
	public List<CsiteLast> getByUserID(long userid) {
		// TODO Auto-generated method stub
		return this.csitelastDao.getByUserID(userid);
	}

	@Override
	public void saveNew(CsiteLast clast) throws ServiceException {
		// TODO Auto-generated method stub
		List<CsiteLast> clist=this.getByUserID(clast.getUid());
		this.save(clast);
		if(clast!=null){
			boolean found =false;
			for(CsiteLast cl:clist){
				if(cl.getCid()==clast.getCid()){
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
