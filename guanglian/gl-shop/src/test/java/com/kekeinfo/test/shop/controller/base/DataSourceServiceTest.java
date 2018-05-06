package com.kekeinfo.test.shop.controller.base;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@Ignore
public class DataSourceServiceTest extends JUnitActionBase {

    @Test
    @Ignore
    public void testJSONDataSourceServiceSQL() throws Exception {
        System.out.println(".......................");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.setRequestURI("/water/content/pages/test.html");
        
        request.addParameter("param", "2011-11-29");
        
        request.setMethod("GET");
        System.out.println(">>>>>>>>>>>>>>");
        // 执行URI对应的action  
        this.excuteAction(request, response);
        String result = response.getContentAsString();
        Assert.assertNotNull(result);
        
    }
}