package com.kekeinfo.web.shop.controller.foreground;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kekeinfo.test.shop.controller.base.AbstractController;
import com.kekeinfo.test.shop.controller.base.SecurityRequestPostProcessors;

public class BaseinfoControllerTest extends AbstractController {
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Override
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
	}

	@Test
	public void test1() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/baseinfo.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("zonename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("storename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test21() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/baseinfo.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("zonename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("storename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/removeHead.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test22() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/removeHead.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/saveinfo.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("zonename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("storename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test23() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/saveinfo.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("zonename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("storename"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/zones.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test24() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/zones.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test5() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkphone.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test25() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkphone.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test6() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkemail.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test26() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkemail.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test7() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/lists.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test27() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/lists.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test8() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/chgpassword.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("password"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test28() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/chgpassword.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("password"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test9() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkoldpwd.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test29() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/checkoldpwd.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test10() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/headcfg.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("headimg"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test210() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/headcfg.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("headimg"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test11() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/savehead.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("headimg"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test211() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/savehead.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("headimg"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test12() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/base/savePassword.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test212() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/base/savePassword.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}