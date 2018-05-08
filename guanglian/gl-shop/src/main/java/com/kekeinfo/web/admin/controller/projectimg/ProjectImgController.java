package com.kekeinfo.web.admin.controller.projectimg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;
import com.kekeinfo.core.business.projectimg.service.ProjectImgService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.web.admin.controller.pumpwell.PWellController;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

/**
 * Created by WangChong on 2018/5/6.
 */
@Controller
public class ProjectImgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectImgController.class);

    @Autowired
    private PNodeUtils pnodeUtils;
    @Autowired
    private WebApplicationCacheUtils webCacheUtils;
    @Autowired
    private ProjectImgService projectImgService;
    @Autowired
    private GroupService groupService;
    @RequestMapping("/water/projectimg/list.html")
    public String getImg(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setMenu(model, request);
        String csiteID = request.getParameter("cid");
        request.setAttribute("activeFun", "projectimg");
        if (StringUtils.isNotBlank(csiteID)) {
            String[] filed = {"csite.id"};
            String[] values = {csiteID};
            Entitites<ProjectImg> projectImgEntitites = projectImgService.getListByAttributes(filed, values, null);
            List<ProjectImg> projectImgs = projectImgEntitites.getEntites();
            model.addAttribute("projectImgs", projectImgs);
        }
        if (!StringUtils.isBlank(csiteID)) {
            try {
                //
                List<UnderWater> cs = (List<UnderWater>) webCacheUtils.getFromCache(Constants.WATER_CSITE);
                UnderWater csite = null;
                for (UnderWater c : cs) {
                    if (c.getId().equals(Long.parseLong(csiteID))) {
                        csite = c;
                        break;
                    }
                }
                if (csite == null) {
                    csite = pnodeUtils.getByCid(Long.parseLong(csiteID));
                }
                model.addAttribute("csite", csite);
                boolean hasRight = false;
                if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
                    if (request.isUserInRole("EDIT-PROJECT")) {
                        List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT", 1);
                        hasRight = pnodeUtils.hasProjectRight(request, egroups, csite);
                    }
                } else {
                    hasRight = true;
                }
                model.addAttribute("hasRight", hasRight);
                String ctype = request.getParameter("ctype");
                //手机版
                if (StringUtils.isNotBlank(ctype)) {
                    model.addAttribute("project", csite);
                    return "phone-pwells";
                }
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }

        } else {
            return "csite-wlist";
        }
        return "warter-projectimg";
    }

    private void setMenu(Model model, HttpServletRequest request) throws Exception {

        //display menu
        Map<String, String> activeMenus = new HashMap<String, String>();
        activeMenus.put("csite", "csite");
        model.addAttribute("activeMenus", activeMenus);
        //

    }
}
