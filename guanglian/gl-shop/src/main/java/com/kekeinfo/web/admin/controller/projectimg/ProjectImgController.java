package com.kekeinfo.web.admin.controller.projectimg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.generic.service.KekeinfoEntityServiceImpl;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.projectimg.model.BaseMarker;
import com.kekeinfo.core.business.projectimg.model.DeformmonitorMarker;
import com.kekeinfo.core.business.projectimg.model.DewateringMarker;
import com.kekeinfo.core.business.projectimg.model.InvertedWellMarker;
import com.kekeinfo.core.business.projectimg.model.ObserveWellMarker;
import com.kekeinfo.core.business.projectimg.model.ProjectImg;
import com.kekeinfo.core.business.projectimg.model.PumpWellMarker;
import com.kekeinfo.core.business.projectimg.service.DeformmonitorMarkerService;
import com.kekeinfo.core.business.projectimg.service.DewateringMarkerService;
import com.kekeinfo.core.business.projectimg.service.InvertedWellMarkerService;
import com.kekeinfo.core.business.projectimg.service.ObserveWellMarkerService;
import com.kekeinfo.core.business.projectimg.service.ProjectImgService;
import com.kekeinfo.core.business.projectimg.service.PumpWellMarkerService;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.pumpwell.PWellController;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.utils.BeanUtils;
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
    @Autowired
    private PumpWellMarkerService pumpWellMarkerService;
    @Autowired
    private ObserveWellMarkerService observeWellMarkerService;
    @Autowired
    private DeformmonitorMarkerService deformmonitorMarkerService;
    @Autowired
    private DewateringMarkerService dewateringMarkerService;
    @Autowired
    private InvertedWellMarkerService invertedWellMarkerService;
    @Autowired
    private PumpwellService pumpwellService;

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping("/water/projectimg/list.html")
    public String getImg(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setMenu(model, request);
        String csiteID = request.getParameter("cid");
        request.setAttribute("activeFun", "projectimg");
        model.addAttribute("pid", csiteID);
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

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping(value = "/water/projectimg/save.shtml", method = RequestMethod.POST, produces =
            "application/json; charset=utf-8", consumes = "application/json")
    public @ResponseBody
    String savePumpWellMarker(@RequestBody String markerString,
            HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BaseMarker marker = objectMapper.readValue(markerString, BaseMarker.class);
            String markerTyper = marker.getMarkerType();
            switch (markerTyper) {
                case "pwell" :
                    PumpWellMarker pumpWellMarker = new PumpWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, pumpWellMarker);
                    pumpWellMarkerService.save(pumpWellMarker);
                case "dewell" :
                    DewateringMarker dewateringMarker = new DewateringMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, dewateringMarker);
                    dewateringMarkerService.save(dewateringMarker);
                case "owell" :
                    ObserveWellMarker observeWellMarker = new ObserveWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, observeWellMarker);
                    observeWellMarkerService.save(observeWellMarker);
                case "iwell" :
                    InvertedWellMarker invertedWellMarker = new InvertedWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, invertedWellMarker);
                    invertedWellMarkerService.save(invertedWellMarker);
                case "ewell" :
                    DeformmonitorMarker deformmonitorMarker = new DeformmonitorMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, deformmonitorMarker);
                    deformmonitorMarkerService.save(deformmonitorMarker);
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Error while save Project", e);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorMessage(e);
        }
        String returnString = resp.toJSONString();
        return returnString;
    }
}
