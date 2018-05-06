package com.kekeinfo.web.admin.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.PermissionService;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.utils.LabelUtils;
import com.kekeinfo.core.business.user.model.Group;

@Controller
public class GroupsController {

	//private static final Logger LOGGER = LoggerFactory.getLogger(GroupsController.class);


	@Autowired
	protected GroupService groupService;
	
	@Autowired
	PermissionService permissionService;

	
	@Autowired
	LabelUtils messages;



	@PreAuthorize("hasRole('CONFIGURATION')")
	@RequestMapping(value = "/water/groups/editGroup.html", method = RequestMethod.GET)
	public String displayGroup(@RequestParam("id") Integer groupId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);

		Group group = groupService.getById(groupId);

		model.addAttribute("group", group);

		return "admin-user-group";
	}



	@PreAuthorize("hasRole('CONFIGURATION')")
	@RequestMapping(value = "/water/groups/groups.html", method = RequestMethod.GET)
	public String displayGroups(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);
		List<Group> groups = groupService.list();
		model.addAttribute("groups", groups);

		return "admin-user-groups";
	}

/**
	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/water/groups/paging.html", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String pageGroups(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {

		AjaxResponse resp = new AjaxResponse();
		try {

				List<Group> groups = groupService.list();

				for(Group group : groups) {
					Map entry = new HashMap();
					entry.put("groupId", group.getId());
					entry.put("name", group.getGroupName());

					StringBuilder key = new StringBuilder().append("security.group.description.").append(group.getGroupName());
					try {
					
						String message =  messages.getMessage(key.toString(), locale);
						entry.put("description",message);
					
					} catch(Exception noLabelException) {
						LOGGER.error("No label found for key [" + key.toString() + "]");
					}
					
					
					

					resp.addDataEntry(entry);
				}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging permissions", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
*/
	private void setMenu(Model model, HttpServletRequest request)
			throws Exception {

		// display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("security", "security");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request
				.getAttribute("MENUMAP");

		Menu currentMenu = (Menu) menus.get("profile");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
