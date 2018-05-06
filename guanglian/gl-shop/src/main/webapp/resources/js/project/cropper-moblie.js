(function(root, factory) {
  if (typeof define === 'function' && define.amd) {
    define(['jquery'], factory);
  } else if (typeof exports === 'object') {
    module.exports = factory(require('jquery'));
  } else {
    root.Dropify = factory(root.$);
  }
}(this, function($) {
var pluginName = "cropAvatar";

  'use strict';

  var console = window.console || { log: function () {} };

  /**
   * $element crop容器对象
   * $savatarView 图片DIV对象
   * $savatar 图片Image对象
   * aspectRatio 图片裁剪比例
   * asyncRemove 是否异步删除图片
   */
  function CropAvatar(element, options) {
	  var defaults = {
        removeImgUrl: '',
        removeImgId: '', 
        aspectRatio: NaN,
        autoCropArea: 0.8,
        avatarView: "",
        avatar: "",
        avatarModal: "#avatar-modal",
        asyncRemove: true,
        imagetype: 'image/jpeg', //设定图片的格式
        finishdo:"",
        deletedo:""
	  }
	var currentAvatarView;
	this.delImageIds = "";
	this.$container = $(element);
	this.settings = $.extend(true, defaults, options);
    
    this._aspectRatio = this.settings.aspectRatio; //类型：Number，默认值NaN。设置剪裁容器的比例
    this._autoCropArea = this.settings.autoCropArea; //默认值0.8（图片的80%）。0-1之间的数值，定义自动剪裁区域的大小
    
    this.$avatarView = $(""+this.settings.avatarView); 
   
    this.initDiv();
    

    this.init();
    console.log(this);
  }
  
  CropAvatar.prototype = {
    constructor: CropAvatar,

    support: {
      fileList: !!$('<input type="file">').prop('files'),
      blobURLs: !!window.URL && URL.createObjectURL,
      formData: !!window.FormData
    },

    init: function () {
      this.support.datauri = this.support.fileList && this.support.blobURLs;

      if (!this.support.formData) {
        this.initIframe();
      }

      this.initTooltip();
      this.initModal();
      this.addListener();
    },

    addListener: function () {
      this.$avatarView.on('click', $.proxy(this.click, this));
      var element = this;

      this.$avatarInput.on('change', $.proxy(this.change, this));
      //this.$avatarForm.on('submit', $.proxy(this.submit, this));
      this.$avatarSave.on('click', $.proxy(this.getData, this));
      var ro= this;
      this.$clearBtn.each(function(i,o){
    	  $(o).on('click', $.proxy(ro.removeImg, ro,i));
      });
     // this.$clearBtn.on('click', $.proxy(this.removeImg, this));
    },

   initTooltip: function () {
      this.$avatarView.tooltip({
        placement: 'bottom'
      });
    },
    initDiv: function (){
  	  this.$avatarView = $(""+this.settings.avatarView); 
  	   
  	    if (this.$avatarView!=null&&this.$avatarView.length>0) {
  	    	var element = this;
  	    	this.$avatarView.each(function(i,o){
  	    		var clearBtn = $('<button type="button" class="dropify-clear">删除</button>');
  	    		$(o).attr("id", element.settings.avatarView+i);
  	    		$(o).append(clearBtn);
  	    	});
  		}
  	    this.$clearBtn = this.$avatarView.find('.dropify-clear');
  	    this.$avatar = $(""+this.settings.avatar);
  	    this.$avatarModal = this.$container.find(""+this.settings.avatarModal);
  	    this.$loading = this.$container.find('.loading');
  	   
  	    this.$infos = $('<div class="dropify-infos"></div>');
  	   
  	    this.$avatarView.append(this.$infos);
  	    

  	    
  	    this.$avatarUpload = this.$avatarModal.find('.avatar-upload'); //div input defined
  	    this.$avatarSrc = this.$avatarModal.find('.avatar-src'); //隐藏域变量
  	    this.$avatarData = this.$avatarModal.find('.avatar-data'); //隐藏域变量
  	    this.$avatarInput = this.$avatarModal.find('.avatar-input'); //上传文件input
  	    this.$avatarSave = this.$avatarModal.find('.avatar-save'); //提交按钮

  	    this.$avatarWrapper = this.$avatarModal.find('.avatar-wrapper');
    },
    initModal: function () {
      this.$avatarModal.modal({
        show: false
      });
    },

    initPreview: function () {
      var url = this.$avatar.attr('src');

      //this.$avatarPreview.empty().html('<img src="' + url + '">');
    },

    initIframe: function () {
      var target = 'upload-iframe-' + (new Date()).getTime(),
          $iframe = $('<iframe>').attr({
            name: target,
            src: ''
          }),
          _this = this;

      // Ready ifrmae
      $iframe.one('load', function () {

        // respond response
        $iframe.on('load', function () {
          var data;

          try {
            data = $(this).contents().find('body').text();
          } catch (e) {
            console.log(e.message);
          }

          if (data) {
            try {
              data = $.parseJSON(data);
            } catch (e) {
              console.log(e.message);
            }

            _this.submitDone(data);
          } else {
            _this.submitFail('Image upload failed!');
          }

          _this.submitEnd();

        });
      });

      this.$iframe = $iframe;
      this.$avatarForm.attr('target', target).after($iframe.hide());
    },

    click: function (event) {
      this.$avatarModal.modal('show');
      this.currentAvatarView = $(event.target);
      if(!this.currentAvatarView.hasClass("avatar-view")) {
    	  this.currentAvatarView = $(event.target).parent(".avatar-view");
      }
      this.initPreview();
    },

    change: function () {
      var files,
          file;

      if (this.support.datauri) {
        files = this.$avatarInput.prop('files');

        if (files.length > 0) {
          file = files[0];
          this.imagetype = file.type; //获取文件类型
          if (this.isImageFile(file)) {
            if (this.url) {
              URL.revokeObjectURL(this.url); // Revoke the old one
            }

            this.url = URL.createObjectURL(file);
            this.startCropper();
          }
        }
      } else {
        file = this.$avatarInput.val();

        if (this.isImageFile(file)) {
          this.syncUpload();
        }
      }
    },
    removeImg: function(idi,event) {
    	console.log("removeImg method");
    	$('#loading').show();
    	var o = this;
    	if (this.settings.removeImgUrl!=''&&this.$avatar.attr("imgId")!=''&&this.settings.asyncRemove) {
			var data={"id":this.$avatar.attr("imgId")};
    		//同一个页面两个不同类型的删除
    		
			if(this.$avatar.attr("imgId")==undefined && idi !=null){
				var iobj = this.$avatar[idi].id;
    			data={"id":iobj};
    		}
    		$.ajax({
				type : "POST",
				cache: false, //cache设置为false
				url : this.settings.removeImgUrl,
				data : data,
				success: function(response, stat){  
					$('#loading').hide();
					var status = response.response.status;
					if (status == 0 || status == 9999) {
						o.clear(event);
						toastr.success('操作成功');
					} else if (status == 11) {
						o.clear(event);
						toastr.success('操作成功');
						afterRemoveImg(response); //自定义外部接口
					} else {
						toastr.error('操作失败');
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					$('#loading').hide();
					alert('error ' + errorThrown);
				}
			});
		} else {
			var obj = $(event.target);
	    	var avatarView = obj.parent();
	    	if(this.settings.deletedo!=""){
		    	delimage(avatarView.parent())
		    }
	    	var currentAvatar = avatarView.children("img");
			if(currentAvatar.attr("imgId")!='') {
				this.delImageIds += currentAvatar.attr("imgId") + "#";
			}
			this.clear(event);
			$('#loading').hide();
		}
    	event.stopPropagation();
    },
    //获取crop等信息
    getData: function() {
      if (!this.$avatarSrc.val() && !this.$avatarInput.val()) {
        return false;
      }
      var canvas = this.$img.cropper("getCroppedCanvas");
      //为避免png图片丢失透明属性，统一将图片转出png格式
      if(this.imagetype==null) this.imagetype = "image/jpeg";
      if (!(this.imagetype.indexOf("png")>0||this.imagetype.indexOf("gif"))) {
		this.imagetype = "image/jpeg";
	}
      var _src = canvas.toDataURL(this.imagetype,0.7);
      var currentAvatar = this.currentAvatarView.children("img");
      if (!this.settings.asyncRemove&&currentAvatar.attr("imgId")!="") {
    	  this.delImageIds += currentAvatar.attr("imgId") + "#"; //处理需要删除的服务端图片ID
    	  currentAvatar.attr('imgId', "");
      }
      currentAvatar.attr('src',_src);
      currentAvatar.show();
      this.currentAvatarView.addClass('has-preview');

      this.$avatarInput.val(""); //清空inputFile数据
      
		
	    this.stopCropper();
	    this.$avatarModal.modal('hide');
	    if(this.settings.finishdo!=""){
	    	addimage();
	    	this.initDiv();
	    	this.init();
	    }
	    return false;
    },
    
    rotate: function (e) {
      var data;

      if (this.active) {
        data = $(e.target).data();

        if (data.method) {
          this.$img.cropper(data.method, data.option);
        }
      }
    },

    isImageFile: function (file) {
      if (file.type) {
        return /^image\/\w+$/.test(file.type);
      } else {
        return /\.(jpg|jpeg|png|gif)$/.test(file);
      }
    },

    startCropper: function () {
      var _this = this;
      if (this.active) {
        this.$img.cropper('replace', this.url);
      } else {
        this.$img = $('<img src="' + this.url + '">');
        this.$avatarWrapper.empty().html(this.$img);
        this.$img.cropper({
          aspectRatio: this._aspectRatio,
          autoCropArea: this._autoCropArea,
          //preview: this.$avatarPreview.selector,
          strict: true,
          crop: function (data) {
            var json = [
                  '{"x":' + data.x,
                  '"y":' + data.y,
                  '"height":' + data.height,
                  '"width":' + data.width,
                  '"rotate":' + data.rotate + '}'
                ].join();

            _this.$avatarData.val(json);
          }
        });

        this.active = true;
      }
    },

    stopCropper: function () {
      if (this.active) {
        this.$img.cropper('destroy');
        this.$img.remove();
        this.active = false;
      }
    },

    syncUpload: function () {
      this.$avatarSave.click();
    },

    cropDone: function () {
      this.$avatarForm.get(0).reset();
      this.$avatar.attr('src', this.url);
      this.stopCropper();
      this.$avatarModal.modal('hide');
    },

    alert: function (msg) {
      var $alert = [
            '<div class="alert alert-danger avater-alert">',
              '<button type="button" class="close" data-dismiss="alert">&times;</button>',
              msg,
            '</div>'
          ].join('');

      this.$avatarUpload.after($alert);
    },
    //	清空图片和预览class
    clear: function(event) {
    	var o = $(event.target);
    	var avatarView = o.parent();
    	var currentAvatar = avatarView.children("img");
    	avatarView.removeClass("has-preview");
    	currentAvatar.attr('src','');
        currentAvatar.attr("imgId", "");
        currentAvatar.hide();
        

    },
    
    clearAll: function() {
    	this.$avatarView.removeClass("has-preview");
    	this.$avatar.attr("src", "");
    	this.$avatar.attr("imgId", "");
    	this.$avatar.hide();
    },
    
    clearInputFile: function() {
    	var file = this.$avatarInput;
    	file.after(file.clone().val(""));      
    	file.remove(); 
    }
  };
  $.fn[pluginName] = function(options) {
	    var array = new Array();
	    this.each(function() {
	        if (!$.data(this, "plugin_" + pluginName)) {
	        	var obj = new CropAvatar(this, options);
	            $.data(this, "plugin_" + pluginName, obj);
	            array.push(obj);
	        }
	    });

	    return array;
	};

  return CropAvatar;
}));
