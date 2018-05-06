package com.kekeinfo.web.filter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"groups","pNodes"})
public class UserFilter {

}
