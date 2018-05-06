package com.kekeinfo.web.entity.filter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"pointLink", "pointInfo"})
public interface PointFilter {

}
