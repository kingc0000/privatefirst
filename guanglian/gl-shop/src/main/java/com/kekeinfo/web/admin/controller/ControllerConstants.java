/**
 * 
 */
package com.kekeinfo.web.admin.controller;

/**
 * Interface contain constant for Controller.These constant will be used throughout
 * gl-shop to  providing constant values to various Controllers being used in the
 * application.
 * @author Umesh A
 *
 */
public interface ControllerConstants
{

    interface Tiles{
    	
    	final String dashboard="dashboard";
    	final String waterDashboard="water-dashboard";
    	final String cameraDashboard="camera-dashboard";
    	final String systemDashboard="system-dashboard";
    	
        interface ContentImages{
            final String addContentImages="admin-contentImages-add";
            final String contentImages="admin-content-images";
            final String fileBrowser="admin-content-filebrowser";
 
        }
        
        interface Department{
        	final String department="water-departments";
        }
        
        interface ContentFiles{
            final String addContentFiles="admin-content-files-add";
            final String contentFiles="admin-content-files";
        }
        
        interface User{
            final String profile="water-user-profile";
            final String users="water-users";
            final String password="water-user-password";
        }
        
        interface Configuration{
            final String accounts="config-accounts";
            final String email="config-email";
            final String logo="config-logo";
            final String dreport="config-dreport";
        }  
        
        interface Camera {
        	final String monitor="water-camera-monitor";
        }
        
    }
}
