(function($, window, document, undefined) {
	$.fn.quicksearch = function (target, opt) {
		
		var timeout, cache, rowcache, jq_results, val = '', cpLock = false,e = this, options = $.extend({ 
			delay: 100,
			selector: null,
			stripeRows: null,
			loader: null,
			noResults: '',
			matchedResultsCount: 0,
			bind: 'keyup',
			isfirt:true,
			onBefore: function () { 
				return;
			},
			onAfter: function () { 
				return;
			},
			show: function () {
				this.style.display = "";
			},
			hide: function () {
				this.style.display = "none";
			},
			prepareQuery: function (val) {
				return val.toLowerCase().split(' ');
			},
			testQuery: function (query, txt, _row) {
				for (var i = 0; i < query.length; i += 1) {
					if (txt.indexOf(query[i]) === -1) {
						return false;
					}
				}
				return true;
			}
		}, opt);
		
		this.go = function () {
			if(options.isfirt){
				options.isfirt=false;
				return;
			}
			var i = 0,
				numMatchedRows = 0,
				noresults = true, 
				query = options.prepareQuery(val),
				val_empty = (val.replace(' ', '').length === 0);
			//当为空时
			if(val_empty){
				$('.zone').show();
				for (var i = 0, len = rowcache.length; i < len; i++) {
					options.show.apply(rowcache[i]);
					noresults = false;
					numMatchedRows++;
				}
				$('.zone').removeClass("active");
				$('.zone').next().hide();
			}else{
				//所有zone都隐藏
				$('.zone').hide();
				$('.sub').hide();
				for (var i = 0, len = rowcache.length; i < len; i++) {
					if (options.testQuery(query, cache[i], rowcache[i])) {
						var zone=rowcache[i].getAttribute("data-zone");
						$('#zname_'+zone).show();
						$('#zname_'+zone).addClass("active");
						options.show.apply(rowcache[i]);
						noresults = false;
						numMatchedRows++;
						$('#zname_'+zone).next().show();
					} else {
						options.hide.apply(rowcache[i]);
					}
				}
			}
			
			if (noresults) {
				this.results(false);
			} else {
				this.results(true);
				this.stripe();
			}
			
			this.matchedResultsCount = numMatchedRows;
			this.loader(false);
			options.onAfter();
			
			return this;
		};
		
		/*
		 * External API so that users can perform search programatically. 
		 * */
		this.search = function (submittedVal) {
			val = submittedVal;
			e.trigger();
		};
		
		/*
		 * External API to get the number of matched results as seen in 
		 * https://github.com/ruiz107/quicksearch/commit/f78dc440b42d95ce9caed1d087174dd4359982d6
		 * */
		this.currentMatchedResults = function() {
			return this.matchedResultsCount;
		};
		
		this.stripe = function () {
			
			if (typeof options.stripeRows === "object" && options.stripeRows !== null)
			{
				var joined = options.stripeRows.join(' ');
				var stripeRows_length = options.stripeRows.length;
				
				jq_results.not(':hidden').each(function (i) {
					$(this).removeClass(joined).addClass(options.stripeRows[i % stripeRows_length]);
				});
			}
			
			return this;
		};
		
		this.strip_html = function (input) {
			var output = input.replace(new RegExp('<[^<]+\>', 'g'), "");
			output = $.trim(output.toLowerCase());
			return output;
		};
		
		this.results = function (bool) {
			if (typeof options.noResults === "string" && options.noResults !== "") {
				if (bool) {
					$(options.noResults).hide();
				} else {
					$(options.noResults).show();
				}
			}
			return this;
		};
		
		this.loader = function (bool) {
			if (typeof options.loader === "string" && options.loader !== "") {
				 (bool) ? $(options.loader).show() : $(options.loader).hide();
			}
			return this;
		};
		
		this.cache = function () {
			
			jq_results = $(target);
			
			if (typeof options.noResults === "string" && options.noResults !== "") {
				jq_results = jq_results.not(options.noResults);
			}
			
			var t = (typeof options.selector === "string") ? jq_results.find(options.selector) : $(target).not(options.noResults);
			cache = t.map(function () {
				return e.strip_html(this.innerHTML);
			});
			
			rowcache = jq_results.map(function () {
				return this;
			});

			/*
			 * Modified fix for sync-ing "val". 
			 * Original fix https://github.com/michaellwest/quicksearch/commit/4ace4008d079298a01f97f885ba8fa956a9703d1
			 * */
			val = val || this.val() || "";
			
			return this.go();
		};
		
		this.trigger = function () {
			this.loader(true);
			options.onBefore();
			
			window.clearTimeout(timeout);
			timeout = window.setTimeout(function () {
				e.go();
			}, options.delay);
			
			return this;
		};
		
		this.cache();
		this.results(true);
		this.stripe();
		this.loader(false);
		var bind_name = 'input';
	      if (navigator.userAgent.indexOf("MSIE") != -1){
	        bind_name = 'propertychange';
	      }
		return this.each(function () {
			
			/*
			 * Changed from .bind to .on.
			 * */
			//将keyup事件修改
			$(this).on('compositionstart', function () {
		        cpLock = true;
		        return;
		    });
			$(this).on('compositionend', function () {
		        cpLock = false;
		        val = $(this).val();
				 e.trigger(); 
		    });
			
			$(this).on(bind_name, function () {
				if(!cpLock){
					val = $(this).val();
					 e.trigger(); 
				}
			});
		});
		
	};

}(jQuery, this, document));
