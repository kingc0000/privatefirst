<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="container" id="crop-avatar">

    <!-- Cropping modal -->
    <div class="modal fade" id="avatar-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title" id="avatar-modal-label">修改图片</h4>
            </div>
            <div class="modal-body">
              <div class="avatar-body">

                <!-- Upload image and data -->
                <div class="avatar-upload">
                  <input class="avatar-src" name="avatar_src" type="hidden">
                  <input class="avatar-data" name="avatar_data" type="hidden">
                  <label for="avatarInput">选择图片</label>
                  <input class="avatar-input" id="avatarInput" name="images" type="file">
                </div>

                <!-- Crop and preview -->
                <div class="row">
                  <div class="col-md-12">
                    <div class="avatar-wrapper"></div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-9">
                  </div>
                  <div class="col-md-3">
                  	<br/>
                    <input class="btn btn-primary btn-block avatar-save" type="button" value="确认"/>
                  </div>
                </div>
              </div>
            </div>
        </div>
      </div>
    </div>
    <div class="loading" aria-label="Loading" role="img" tabindex="-1"></div>
  </div>

