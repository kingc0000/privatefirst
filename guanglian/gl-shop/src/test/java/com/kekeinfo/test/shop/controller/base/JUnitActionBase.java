package com.kekeinfo.test.shop.controller.base;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @ClassName: JUnitActionBase
 * @Description: 这里用一句话描述这个类的作用
 *
 */
public class JUnitActionBase {
    private static HandlerMapping handlerMapping;
    private static HandlerAdapter handlerAdapter;
    
    /** 
     * 读取spring3 MVC配置文件 
     * 加载文件有些问题，无法读取servlet-context.xml中配置的tiles的文件，问题暂时还没有解决
     * @throws UnsupportedEncodingException 
     */
    @BeforeClass
    public static void setUp() throws UnsupportedEncodingException {
        if (handlerMapping == null) {
          //  File file = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
         //   String separator = File.separator;
//            /WEB-INF/spring/root-context.xml
//            /WEB-INF/spring/appServlet/servlet-context.xml
//            /WEB-INF/spring/appServlet/shopizer-properties.xml
//			/WEB-INF/spring/appServlet/shopizer-security.xml
        //    String path = URLDecoder.decode(file.getAbsolutePath(), "utf-8").replace("", "").replace("\\", "/");
            String[] configs = { 
            		"classpath:spring/test-spring-context.xml",
            		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
            		"file:src/main/webapp/WEB-INF/spring/appServlet/guanglian-security.xml"
                    };
            XmlWebApplicationContext context = new XmlWebApplicationContext();
            context.setConfigLocations(configs);
            MockServletContext msc = new MockServletContext();
            context.setServletContext(msc);
            context.refresh();
            msc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            handlerMapping = (HandlerMapping) context.getBean(RequestMappingHandlerMapping.class);
            handlerAdapter = (HandlerAdapter) context.getBean(context
                    .getBeanNamesForType(RequestMappingHandlerAdapter.class)[0]);
        }
    }
    
    /** 
     * 执行request对象请求的action 
     *  
     * @param request 
     * @param response 
     * @return 
     * @throws Exception 
     */
    public ModelAndView excuteAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        final ModelAndView model = handlerAdapter.handle(request, response, chain.getHandler());
        return model;
    }
    
}
