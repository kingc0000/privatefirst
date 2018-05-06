/* ===========================================================
 * bootstrap-fileupload.js j2
 * http://jasny.github.com/bootstrap/javascript.html#fileupload
 * ===========================================================
 * Copyright 2012 Jasny BV, Netherlands.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */

!function ($) {

  "use strict"; // jshint ;_

 /* FILEUPLOAD PUBLIC CLASS DEFINITION
  * ================================= */

  var Fileupload = function (element, options) {
    this.$element = $(element)
    this.type = this.$element.data('uploadtype') || (this.$element.find('.thumbnail').length > 0 ? "image" : "file")
      
    this.$input = this.$element.find(':file')
    if (this.$input.length === 0) return

    this.name = this.$input.attr('name') || options.name

    this.$hidden = this.$element.find('input[type=hidden][name="'+this.name+'"]')
    if (this.$hidden.length === 0) {
      this.$hidden = $('<input type="hidden" />')
      this.$element.prepend(this.$hidden)
    }

    this.$preview = this.$element.find('.fileupload-preview')
    var height = this.$preview.css('height')
    if (this.$preview.css('display') != 'inline' && height != '0px' && height != 'none') this.$preview.css('line-height', height)

    this.original = {
      'exists': this.$element.hasClass('fileupload-exists'),
      'preview': this.$preview.html(),
      'hiddenVal': this.$hidden.val()
    }
    
    this.$remove = this.$element.find('[data-dismiss="fileupload"]')

    this.$element.find('[data-trigger="fileupload"]').on('click.fileupload', $.proxy(this.trigger, this))

    this.listen()
  }
  
  Fileupload.prototype = {
    
    listen: function() {
      this.$input.on('change.fileupload', $.proxy(this.change, this))
      $(this.$input[0].form).on('reset.fileupload', $.proxy(this.reset, this))
      if (this.$remove) this.$remove.on('click.fileupload', $.proxy(this.clear, this))
    },
    
    change: function(e, invoked) {
      var file = e.target.files !== undefined ? e.target.files[0] : (e.target.value ? { name: e.target.value.replace(/^.+\\/, '') } : null)
      if (invoked === 'clear') return
      
      if (!file) {
        this.clear()
        return
      }
      
      this.$hidden.val('')
      this.$hidden.attr('name', '')
      this.$input.attr('name', this.name)
      var tinput =this.$input;
      if (typeof file.type !== "undefined" ? file.type.match('image.*') : file.name.match('\\.(gif|png|jpe?g)$') && typeof FileReader !== "undefined") {
        var reader = new FileReader()
        var preview = this.$preview
        var element = this.$element
        reader.readAsDataURL(file)
        reader.onload = function(e) {
        	var img = new Image();
            img.src = e.target.result;
          //大于200k图片压缩 
          if (file.size/1024 > 200) {
        	  if (img.complete) {
        		 // preview.html('<img src="' + compress(img) + '" ' + (preview.css('max-height') != 'none' ? 'style="max-height: ' + preview.css('max-height') + ';"' : '') + ' />')
                  element.addClass('fileupload-exists').removeClass('fileupload-new')
        		  preview.text(file.name)
        		  tinput.attr('src', compress(img))
              } else {
                  img.onload = function() {
                	 // preview.html('<img src="' + compress(img) + '" ' + (preview.css('max-height') != 'none' ? 'style="max-height: ' + preview.css('max-height') + ';"' : '') + ' />')
                      element.addClass('fileupload-exists').removeClass('fileupload-new')
                	  preview.text(file.name)
                	  tinput.attr('src', compress(img))
                  }
              }
        	  
          }
        }
        
      } else {
        this.$preview.text(file.name)
        this.$element.addClass('fileupload-exists').removeClass('fileupload-new')
      }
      $('#preview').hide();
      $('#download').hide();
    },

    clear: function(e) {
      this.$hidden.val('')
      //this.$hidden.attr('name', this.name)
      this.$hidden.attr('name', '')
      this.$input.attr('name', '')

      //ie8+ doesn't support changing the value of input with type=file so clone instead
      if( /msie/.test(navigator.userAgent.toLowerCase())){
          var inputClone = this.$input.clone(true);
          this.$input.after(inputClone);
          this.$input.remove();
          this.$input = inputClone;
      }else{
          this.$input.val('')
      }

      this.$preview.html('')
      this.$element.addClass('fileupload-new').removeClass('fileupload-exists')
      $('#preview').show();
          $('#download').show();
      if (e) {
        this.$input.trigger('change', [ 'clear' ])
        e.preventDefault()
      }
    },
    
    reset: function(e) {
      this.clear()
      
      this.$hidden.val(this.original.hiddenVal)
      this.$preview.html(this.original.preview)
      
      if (this.original.exists) this.$element.addClass('fileupload-exists').removeClass('fileupload-new')
       else this.$element.addClass('fileupload-new').removeClass('fileupload-exists')
    },
    
    trigger: function(e) {
      this.$input.trigger('click')
      e.preventDefault()
    }
  }

  
 /* FILEUPLOAD PLUGIN DEFINITION
  * =========================== */

  $.fn.fileupload = function (options) {
    return this.each(function () {
      var $this = $(this)
      , data = $this.data('fileupload')
      if (!data) $this.data('fileupload', (data = new Fileupload(this, options)))
      if (typeof options == 'string') data[options]()
    })
  }

  $.fn.fileupload.Constructor = Fileupload


 /* FILEUPLOAD DATA-API
  * ================== */
var app=0;
  $(function () {
	  
	  app = navigator.userAgent.indexOf("android");
	  if(app==-1){
	  	app = navigator.userAgent.indexOf("iOS");
	  }
	  $('body').on('click.fileupload.data-api', '[data-provides="fileupload"]', function (e) {
      var $this = $(this)
      if ($this.data('fileupload')) return
      $this.fileupload($this.data())
      
      var $target = $(e.target).is('[data-dismiss=fileupload],[data-trigger=fileupload]') ?
        $(e.target) : $(e.target).parents('[data-dismiss=fileupload],[data-trigger=fileupload]').first()
      if ($target.length > 0) {
          $target.trigger('click.fileupload')
          e.preventDefault()
      }
    })
  })

}(window.jQuery);

var canvas = document.createElement("canvas");  
var ctx = canvas.getContext('2d');  
// 瓦片canvas  
var tCanvas = document.createElement("canvas");  
var tctx = tCanvas.getContext("2d");  
var maxsize = 100 * 1024;  
function compress(img) {
	var initSize = img.src.length;  
    var width = img.width;  
    var height = img.height;  
    var bili = 1;  
    if(width>480){  
        bili = 480/width;  
    }else{  
        if(height>640){  
            bili = 640/height;  
        }else{  
            bili=1;  
        }  
    }  
    //如果图片大于四百万像素，计算压缩比并将大小压至400万以下  
    var ratio;  
    if ((ratio = width * height / 4000000) > 1) {  
        ratio = Math.sqrt(ratio);  
        width /= ratio;  
        height /= ratio;  
    } else {  
        ratio = 1;  
    }  
    canvas.width = width;  
    canvas.height = height;  
    // 铺底色  
    ctx.fillStyle = "#fff";  
    ctx.fillRect(0, 0, canvas.width, canvas.height);  
  
    //如果图片像素大于100万则使用瓦片绘制  
    var count;  
    if ((count = width * height / 1000000) > 1) {  
        count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片  
        //计算每块瓦片的宽和高  
        var nw = ~~(width / count);  
        var nh = ~~(height / count);  
        tCanvas.width = nw;  
        tCanvas.height = nh;  
        for (var i = 0; i < count; i++) {  
            for (var j = 0; j < count; j++) {  
                tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);  
                ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);  
            }  
        }  
    } else {  
        ctx.drawImage(img, 0, 0, width, height);  
    }  
    //进行最小压缩  
    var ndata = canvas.toDataURL('image/jpeg', bili);  
    tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;  
    return ndata; 
}