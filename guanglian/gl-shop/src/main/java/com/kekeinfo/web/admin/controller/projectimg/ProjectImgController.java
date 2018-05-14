package com.kekeinfo.web.admin.controller.projectimg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.deformmonitor.service.DeformmonitorService;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.dewatering.service.DewateringService;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.invertedwell.service.InvertedwellService;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
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
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
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

    @Autowired
    private ObservewellService observewellService;

    @Autowired
    private InvertedwellService invertedwellService;

    @Autowired
    private DewateringService dewateringService;

    @Autowired
    private DeformmonitorService deformmonitorService;

    @Autowired
    private CSiteService cSiteService;

    @PreAuthorize("hasRole('VIEW-PROJECT')")
    @RequestMapping(value = {"/water/projectimg/previewbyext/{fileName}.{extension}"}, method = RequestMethod.GET)
    public String previewFileByExt(@PathVariable final String fileName, @PathVariable String extension,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ftype = request.getParameter("ftype");
        return "redirect:/files/previewbyext/" + fileName + "." + extension + "?ftype=" + ftype;
    }

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping(value = "/water/projectimg/getwellprofile.shtml", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = " application/json; charset=utf-8")
    public @ResponseBody
    String getWellProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String csiteID = request.getParameter("cid");
        if (!StringUtils.isBlank(csiteID)) {
            String[] filed = {"csite.id"};
            String[] values = {csiteID};
            Entitites<ProjectImg> projectImgEntitites = projectImgService.getListByAttributes(filed, values, null);
            List<ProjectImg> projectImgs = projectImgEntitites.getEntites();
            if (CollectionUtils.isEmpty(projectImgs)) {
                ConstructionSite c = cSiteService.getByCid(Long.parseLong(csiteID));
                List<Images> images = c.getImages();
                if (CollectionUtils.isNotEmpty(images)) {
                    ProjectImg projectImg = new ProjectImg();
                    projectImg.setCsite(c);
                    projectImg.setUrl(images.get(0).getJpeg());
                    projectImgs.add(projectImg);
                    projectImgService.save(projectImg);
                }
            }
            if (CollectionUtils.isNotEmpty(projectImgs)) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(projectImgs);
                return json;
            } else {
                return "";
            }
        }
        return "";
    }

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping("/water/projectimg/list.html")
    public String getImg(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setMenu(model, request);
        String csiteID = request.getParameter("cid");
        request.setAttribute("activeFun", "projectimg");
        model.addAttribute("pid", csiteID);
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
                String[] filed = {"csite.id"};
                String[] values = {csiteID};
                Entitites<ProjectImg> projectImgEntitites = projectImgService.getListByAttributes(filed, values, null);
                List<ProjectImg> projectImgs = projectImgEntitites.getEntites();
                if (CollectionUtils.isEmpty(projectImgs)) {
                    ConstructionSite c = cSiteService.getByCid(Long.parseLong(csiteID));
                    List<Images> images = c.getImages();
                    if (CollectionUtils.isNotEmpty(images)) {
                        ProjectImg projectImg = new ProjectImg();
                        projectImg.setCsite(c);
                        projectImg.setUrl(images.get(0).getJpeg());
                        projectImgs.add(projectImg);
                        projectImgService.save(projectImg);
                    }
                }
                if (CollectionUtils.isNotEmpty(projectImgs)) {
                    model.addAttribute("projectImgs", projectImgs);
                } else {
                    return "redirect:/water/csite/wlist.html?pid=" + csiteID;
                }
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }

        } else {
            return "redirect:/water/csite/wlist.html?pid=" + csiteID;
        }
        return "warter-projectimg";
    }

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping(value = "/water/projectimg/getWarning.shtml", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = " application/json; charset=utf-8")
    public String getWarning(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setMenu(model, request);
        String csiteID = request.getParameter("cid");
        return "redirect:/water/csite/getWarning.shtml?pid=" + csiteID;
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
    String savePumpWellMarker(@RequestBody String markerString, HttpServletRequest request,
            HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BaseMarker marker = objectMapper.readValue(markerString, BaseMarker.class);
            String markerTyper = marker.getMarkerType();
            ProjectImg projectImg = projectImgService.getById(marker.getProjectImg().getId());
            switch (markerTyper) {
                case "pwell":
                    PumpWellMarker pumpWellMarker = new PumpWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, pumpWellMarker);
                    Pumpwell pumpwell = pumpwellService.getByIdWithCSite(marker.getWell().getId());
                    String wellDepth1 = String.valueOf(pumpwell.getPointInfo().getDeepWell());
                    if (StringUtils.isNotBlank(wellDepth1) && !wellDepth1.equals("null")) {
                        pumpWellMarker.setWellDepth(wellDepth1);
                    } else {
                        pumpWellMarker.setWellDepth("0");
                    }
                    pumpWellMarker.setWell(pumpwell);
                    pumpWellMarker.setProjectImg(projectImg);
                    pumpWellMarkerService.save(pumpWellMarker);
                    break;
                case "dwell":
                    DewateringMarker dewateringMarker = new DewateringMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, dewateringMarker);
                    Dewatering dewatering = dewateringService.getByIdWithCSite(marker.getWell().getId());
                    String wellDepth2 = String.valueOf(dewatering.getPointInfo().getDeepWell());
                    if (StringUtils.isNotBlank(wellDepth2)&& !wellDepth2.equals("null")) {
                        dewateringMarker.setWellDepth(wellDepth2);
                    } else {
                        dewateringMarker.setWellDepth("0");
                    }
                    dewateringMarker.setWell(dewatering);
                    dewateringMarker.setProjectImg(projectImg);
                    dewateringMarkerService.save(dewateringMarker);
                    break;
                case "owell":
                    ObserveWellMarker observeWellMarker = new ObserveWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, observeWellMarker);
                    Observewell observewell = observewellService.getByIdWithCSite(marker.getWell().getId());
                    String wellDepth3 = String.valueOf(observewell.getPointInfo().getDeepWell());
                    if (StringUtils.isNotBlank(wellDepth3) && !wellDepth3.equals("null")) {
                        observeWellMarker.setWellDepth(wellDepth3);
                    } else {
                        observeWellMarker.setWellDepth("0");
                    }
                    observeWellMarker.setWell(observewell);
                    observeWellMarker.setProjectImg(projectImg);
                    observeWellMarkerService.save(observeWellMarker);
                    break;
                case "iwell":
                    InvertedWellMarker invertedWellMarker = new InvertedWellMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, invertedWellMarker);
                    Invertedwell invertedwell = invertedwellService.getByIdWithCSite(marker.getWell().getId());
                    String wellDepth4 = String.valueOf(invertedwell.getPointInfo().getDeepWell());
                    if (StringUtils.isNotBlank(wellDepth4) && !wellDepth4.equals("null")) {
                        invertedWellMarker.setWellDepth(wellDepth4);
                    } else {
                        invertedWellMarker.setWellDepth("0");
                    }
                    invertedWellMarker.setWell(invertedwell);
                    invertedWellMarker.setProjectImg(projectImg);
                    invertedWellMarkerService.save(invertedWellMarker);
                    break;
                case "ewell":
                    DeformmonitorMarker deformmonitorMarker = new DeformmonitorMarker();
                    org.springframework.beans.BeanUtils.copyProperties(marker, deformmonitorMarker);
                    Deformmonitor deformmonitor = deformmonitorService.getByIdWithCSite(marker.getWell().getId());
                    String wellDepth5 = String.valueOf(deformmonitor.getPointInfo().getDeepWell());
                    if (StringUtils.isNotBlank(wellDepth5) && !wellDepth5.equals("null")) {
                        deformmonitorMarker.setWellDepth(wellDepth5);
                    } else {
                        deformmonitorMarker.setWellDepth("0");
                    }
                    deformmonitorMarker.setWell(deformmonitor);
                    deformmonitorMarker.setProjectImg(projectImg);
                    deformmonitorMarkerService.save(deformmonitorMarker);
                    break;
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

    @PreAuthorize("hasRole('EDIT-PROJECT')")
    @RequestMapping(value = "/water/projectimg/delete.shtml", method = RequestMethod.POST, produces =
            "application/json; charset=utf-8", consumes = "application/json")
    public @ResponseBody
    String deletePumpWellMarker(@RequestBody String markerString, HttpServletRequest request,
            HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BaseMarker marker = objectMapper.readValue(markerString, BaseMarker.class);
            String markerTyper = marker.getMarkerType();
            switch (markerTyper) {
                case "pwell":
                    Pumpwell pumpwell = pumpwellService.getById(marker.getWellid());
                    Entitites<PumpWellMarker> pumpWellMarkerEntitites = pumpWellMarkerService
                            .getListByAttributes(new String[] {"name", "well"},
                                    new Object[] {marker.getName(), pumpwell}, null);
                    pumpWellMarkerService.delete(pumpWellMarkerEntitites.getEntites().get(0));
                    break;
                case "dwell":
                    Dewatering dewatering = dewateringService.getById(marker.getWellid());
                    Entitites<DewateringMarker> dewateringMarkerEntitites = dewateringMarkerService
                            .getListByAttributes(new String[] {"name", "well"},
                                    new Object[] {marker.getName(), dewatering}, null);
                    dewateringMarkerService.delete(dewateringMarkerEntitites.getEntites().get(0));
                    break;
                case "owell":
                    Observewell observewell = observewellService.getById(marker.getWellid());
                    Entitites<ObserveWellMarker> observeWellMarkerEntitites = observeWellMarkerService
                            .getListByAttributes(new String[] {"name", "well"},
                                    new Object[] {marker.getName(), observewell}, null);
                    observeWellMarkerService.delete(observeWellMarkerEntitites.getEntites().get(0));
                    break;
                case "iwell":
                    Invertedwell invertedwell = invertedwellService.getById(marker.getWellid());
                    Entitites<InvertedWellMarker> invertedWellMarkerEntitites = invertedWellMarkerService
                            .getListByAttributes(new String[] {"name", "well"},
                                    new Object[] {marker.getName(), invertedwell}, null);
                    invertedWellMarkerService.delete(invertedWellMarkerEntitites.getEntites().get(0));
                    break;
                case "ewell":
                    Deformmonitor deformmonitor = deformmonitorService.getById(marker.getWellid());
                    Entitites<DeformmonitorMarker> deformmonitorMarkerEntitites = deformmonitorMarkerService
                            .getListByAttributes(new String[] {"name", "well"},
                                    new Object[] {marker.getName(), deformmonitor}, null);
                    deformmonitorMarkerService.delete(deformmonitorMarkerEntitites.getEntites().get(0));
                    break;
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Error while delete Project", e);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorMessage(e);
        }
        String returnString = resp.toJSONString();
        return returnString;
    }
}
