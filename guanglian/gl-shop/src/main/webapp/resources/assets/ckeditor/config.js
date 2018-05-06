/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
//手机版的隐藏部分功能
if($(window).width()<768){
	CKEDITOR.editorConfig = function( config ) {
		// Define changes to default configuration here. For example:
		config.language = 'zh-cn';
		config.removeDialogTabs = 'image:advanced;image:Link';
		config.toolbarCanCollapse = true;
		config.toolbarStartupExpanded = false;
		config.toolbar = 'Full';
		// config.uiColor = '#AADC6E';
		config.toolbar_Full = [
		                       ['NewPage','-','Undo','Redo','-','SelectAll','RemoveFormat'],
		                       ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		                       '/',
		                       ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
		                       ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		                       '/',
		                       ['Styles','Format','Font','FontSize'],
		                       ['TextColor','BGColor']
		                     ];
	};
}else{
	CKEDITOR.editorConfig = function( config ) {
		// Define changes to default configuration here. For example:
		config.language = 'zh-cn';
		config.removeDialogTabs = 'image:advanced;image:Link';
		config.toolbarCanCollapse = true;
		config.toolbarStartupExpanded = false;
		config.toolbar = 'Full';
		// config.uiColor = '#AADC6E';
		config.toolbar_Full = [
		                       ['NewPage','Preview','-','Templates'],
		                       ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print','SpellChecker', 'Scayt'],
		                       ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
		                       ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select','Button', 'ImageButton', 'HiddenField'],
		                        '/',
		                       ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		                        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
		                        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		                        ['Link','Unlink','Anchor'],
		                       ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
		                       '/',
		                         ['Styles','Format','Font','FontSize'],
		                        ['TextColor','BGColor']
		                     ];
	};
}

