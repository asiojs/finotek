/*!
 * jQuery Upload File Plugin
 * version: 3.1.10
 * @requires jQuery v1.5 or later & form plugin
 * Copyright (c) 2013 Ravishanker Kusuma
 * http://hayageek.com/
 */
(function ($) {
    if($.fn.ajaxForm == undefined) {
        //$.getScript(("https:" == document.location.protocol ? "https://" : "http://") + "malsup.github.io/jquery.form.js");
    	$.getScript("../js/plugins/jquery/jquery.form.js");		// modify ellepark
    }
    var feature = {};
    feature.fileapi = $("<input type='file'/>").get(0).files !== undefined;
    feature.formdata = window.FormData !== undefined;
    $.fn.uploadFile = function (options) {
        // This is the easiest way to have default options.
        var s = $.extend({
            // These are the defaults.
            url: "",
            method: "POST",
            enctype: "multipart/form-data",
            returnType: null,
            allowDuplicates: false,			// 3.1.10 신규
            allowDbDuplicates: false,		// 3.1.10 신규
            duplicateStrict: false,			// 3.1.10 신규
            allowedTypes: "*",
            //For list of acceptFiles
            // http://stackoverflow.com/questions/11832930/html-input-file-accept-attribute-file-type-csv
            acceptFiles: "*",
            fileName: "file",
            formData: {},
            dynamicFormData: function () {
                return {};
            },
            maxFileSize: -1,
            maxFileCount: -1,
            totalFileSize:-1,							// added by ellepark          
            multiple: true,
            dragDrop: true,
            autoSubmit: true,
            showCancel: true,
            showAbort: true,
            showDone: true,
            showDelete: false,
            showError: true,
            showStatusAfterSuccess: true,
            showStatusAfterError: true,
            showFileCounter: true,
            fileCounterStyle: ") ",					// 파일표시 점제거처리
            showProgress: false,
            nestedForms: true,
            showDownload: false,
            onLoad: function (obj) {},
            onSelect: function (files) {
                return true;
            },
            onSubmit: function (files, xhr) {},
            onSuccess: function (files, response, xhr, pd) {},
            onError: function (files, status, message, pd) {},
            onCancel: function (files, pd) {},
            downloadCallback: false,
            deleteCallback: false,
            afterUploadAll: false,
            // 3.1.10 신규 start
            abortButtonClass: "ajax-file-upload-abort",
            cancelButtonClass: "ajax-file-upload-cancel",            
            dragDropContainerClass: "ajax-upload-dragdrop",
            dragDropHoverClass: "state-hover",
            errorClass: "ajax-file-upload-error",
            // 3.1.10 신규 start
            uploadButtonClass: "ajax-file-upload",
            dragDropStr: "<span><b>Drag &amp; Drop Files</b></span>",
            abortStr: "Abort",
            cancelStr: "Cancel",			// modify ellepark
//            cancelStr: "<a href=''><img src='' alt='' title=''></a>",			// modify ellepark
            deletelStr: "Delete",
            doneStr: "Done",
            multiDragErrorStr: "Multiple File Drag &amp; Drop is not allowed.",
            extErrorStr: "is not allowed. Allowed extensions: ",
            duplicateErrorStr: "is not allowed. File already exists.",
            sizeErrorStr: "is not allowed. Allowed Max size: ",
            totalSizeErrorStr: "is not allowed. Allowed Total Max size: ",				// add ellepark
            noSizeErrorStr: "0k is not allowed.",			// add ellepark
            uploadErrorStr: "Upload is not allowed",
            maxFileCountErrorStr: " is not allowed. Maximum allowed files are:",            
            downloadStr: "Download",
            customErrorKeyStr: "jquery-upload-file-error",
            serverErrorStr : "Server not found or Server Down",							// added by ellepark
            showQueueDiv: false,
            statusBarWidth: 450,			// modify ellepark
            dragdropWidth: 450,		// modify ellepark
            showPreview: false,
            previewHeight: "auto",
            previewWidth: "100%",
            uploadFolder:"uploads/"
        }, options);

        this.fileCounter = 1;
        this.selectedFiles = 0;
        this.fCounter = 0; //failed uploads
        this.sCounter = 0; //success uploads
        this.tCounter = 0; //total uploads
        this.totalSize = 0;		  			// 전체용량 구하기 added by ellepark
        this.upCounter = 0;		// added by ellepark
        var formGroup = "ajax-file-upload-" + (new Date().getTime());
        this.formGroup = formGroup;
        this.hide();
        this.errorLog = $("<div></div>"); //Writing errors
        this.after(this.errorLog);
        this.responses = [];
        this.existingFileNames = [];
        this.dbFileNames = [];				// 문서수정시 기첨부 파일 목록 added by ellepark 
        if(!feature.formdata) //check drag drop enabled.
        {
            s.dragDrop = false;
        }
        if(!feature.formdata) {
            s.multiple = false;
        }

        var obj = this;
        var uploadLabel = $('<div>' + $(this).html() + '</div>');
        $(uploadLabel).addClass(s.uploadButtonClass);

        // wait form ajax Form plugin and initialize
        (function checkAjaxFormLoaded() {
            if($.fn.ajaxForm) {

                if(s.dragDrop) {
                    var dragDrop = $('<div class="' + s.dragDropContainerClass + '" style="vertical-align:top;"></div>').width(s.dragdropWidth);
                    $(obj).before(dragDrop);
                    $(dragDrop).append(uploadLabel);
                    $(dragDrop).append($(s.dragDropStr));
                    setDragDropHandlers(obj, s, dragDrop);

                } else {
                    $(obj).before(uploadLabel);
                }
                s.onLoad.call(this, obj);
                createCutomInputFile(obj, formGroup, s, uploadLabel);

            } else window.setTimeout(checkAjaxFormLoaded, 10);
        })();

        this.startUpload = function () {
            $("." + this.formGroup).each(function (i, items) {
                if($(this).is('form')) $(this).submit();
            });
        }
        
        // check part
        

        this.getFileCount = function () {
            return obj.selectedFiles;

        }
        this.stopUpload = function () {
            $("." + s.abortButtonClass).each(function (i, items) {
                if($(this).hasClass(obj.formGroup)) $(this).click();
            });
        }
        this.cancelAll = function () {
            $("." + s.cancelButtonClass).each(function (i, items) {
                if($(this).hasClass(obj.formGroup)) $(this).click();
            });
        }
        this.update = function (settings) {
            //update new settings
            s = $.extend(s, settings);
        }
        
        // added by ellepark        
        this.updateMaxFileCounter = function(counter) {        	
        	s.maxFileCount = counter;            
        }
        
        this.MaxFileCounter = function() {        	
        	return s.maxFileCount;            
        }
        
        this.updateMaxFileSize = function(totalSize) {        	
        	s.maxFileSize = totalSize;            
        }
        
        this.MaxFileSize = function(totalSize) {        	
        	return s.maxFileSize;            
        }
            
        /**
         * 기 첨부된 파일 목록 삭제처리 added by ellepark
         */
        this.removeDbExistingFileName = function(fileArr) {        
        	 if (obj.dbFileNames.length) {
                 for (var x=0; x<fileArr.length; x++) {
                     var pos = obj.dbFileNames.indexOf(fileArr[x]);
                     if (pos != -1) {
                         obj.dbFileNames.splice(pos, 1);
                     }
                 }
             }
        }
        
        // added by ellepark                

        //This is for showing Old files to user.
        this.createProgress = function (filename) {
            var pd = new createProgressDiv(this, s);
            pd.progressDiv.show();
            pd.progressbar.width('100%');

            var fileNameStr = "";
            if(s.showFileCounter) fileNameStr = obj.fileCounter + s.fileCounterStyle + filename;
            else fileNameStr = filename;

            pd.filename.html(fileNameStr);
            obj.fileCounter++;
            obj.selectedFiles++;
            if(s.showPreview)
            {
                pd.preview.attr('src',s.uploadFolder+filename);
                pd.preview.show();
            }
            
            if(s.showDownload) {
                pd.download.show();
                pd.download.click(function () {
                    if(s.downloadCallback) s.downloadCallback.call(obj, [filename]);
                });
            }
            pd.del.show();

            pd.del.click(function () {
                pd.statusbar.hide().remove();
                var arr = [filename];
                if(s.deleteCallback) s.deleteCallback.call(this, arr, pd);
                obj.selectedFiles -= 1;
                updateFileCounter(s, obj);
            });

        }

        this.getResponses = function () {
            return this.responses;
        }
        var checking = false;

        function checkPendingUploads() {
            if(s.afterUploadAll && !checking) {
                checking = true;
                (function checkPending() {
                    if(obj.sCounter != 0 && (obj.sCounter + obj.fCounter == obj.tCounter)) {
                        s.afterUploadAll(obj);
                        checking = false;
                    } else window.setTimeout(checkPending, 100);
                })();
            }

        }

        function setDragDropHandlers(obj, s, ddObj) {
            ddObj.on('dragenter', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(this).addClass(s.dragDropHoverClass);
            });
            ddObj.on('dragover', function (e) {
                e.stopPropagation();
                e.preventDefault();
                var that = $(this);
                if (that.hasClass(s.dragDropContainerClass) && !that.hasClass(s.dragDropHoverClass)) {
                    that.addClass(s.dragDropHoverClass);
                }
            });
            ddObj.on('drop', function (e) {
                e.preventDefault();
                $(this).removeClass(s.dragDropHoverClass);
                obj.errorLog.html("");
                var files = e.originalEvent.dataTransfer.files;
                if(!s.multiple && files.length > 1) {
                    //if(s.showError) $("<div class='" + s.errorClass + "'>" + s.multiDragErrorStr + "</div>").appendTo(obj.errorLog);
                	if(s.showError) jAlert(s.multiDragErrorStr);			// modify ellepark
                    return;
                }
                if(s.onSelect(files) == false) return;
                serializeAndUploadFiles(s, obj, files);
            });
            ddObj.on('dragleave', function (e) {
                $(this).removeClass(s.dragDropHoverClass);
            });

            $(document).on('dragenter', function (e) {
                e.stopPropagation();
                e.preventDefault();
            });
            $(document).on('dragover', function (e) {
                e.stopPropagation();
                e.preventDefault();
                var that = $(this);
                if (!that.hasClass(s.dragDropContainerClass)) {
                    that.removeClass(s.dragDropHoverClass);
                }
            });
            $(document).on('drop', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(this).removeClass(s.dragDropHoverClass);
            });

        }

        function getSizeStr(size) {
            var sizeStr = "";
            var sizeKB = size / 1024;
            // modify ellepark
            if(parseInt(sizeKB) > 1024*1024) {
                var sizeMB = sizeKB / (1024*1024);
                sizeStr = sizeMB.toFixed(2) + " GB";
            } 
            else  if(parseInt(sizeKB) > 1024) {
                var sizeMB = sizeKB / 1024;
                sizeStr = sizeMB.toFixed(2) + " MB";
            } else {
                sizeStr = sizeKB.toFixed(2) + " KB";
            }
            return sizeStr;
        }

        function serializeData(extraData) {
            var serialized = [];
            if(jQuery.type(extraData) == "string") {
                serialized = extraData.split('&');
            } else {
                serialized = $.param(extraData).split('&');
            }
            var len = serialized.length;
            var result = [];
            var i, part;
            for(i = 0; i < len; i++) {
                serialized[i] = serialized[i].replace(/\+/g, ' ');
                part = serialized[i].split('=');
                result.push([decodeURIComponent(part[0]), decodeURIComponent(part[1])]);
            }
            return result;
        }

        function serializeAndUploadFiles(s, obj, files) {
            for(var i = 0; i < files.length; i++) {
            	// modifty ellepark
                if(isFileTypeAllowed(obj, s, files[i].name)) {
                    //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + files[i].name + "</b> " + s.extErrorStr + s.allowedTypes + "</div>").appendTo(obj.errorLog);
                	if(s.showError) jAlert(s.extErrorStr + s.allowedTypes);
                    continue;
                }
                if(!s.allowDuplicates && isFileDuplicate(obj, files[i].name)) {
                    //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + files[i].name + "</b> " + s.duplicateErrorStr + "</div>").appendTo(obj.errorLog);
                	if(s.showError) jAlert(files[i].name + " " + s.duplicateErrorStr);
                    continue;
                }
                
                // 기첨부된 파일 중복체크(2015/01/08)
                if(!s.allowDbDuplicates && isDbFileDuplicate(obj, files[i].name)) {
                	if(s.showError) jAlert(files[i].name + " " + s.duplicateErrorStr);
                    continue;
                }                
                
                // 0k not upload
                if(files[i].size == 0) {
                	if(s.showError) jAlert(files[i].name + " " + s.noSizeErrorStr);
                    continue;
                }
                
                if(s.maxFileSize != -1 && files[i].size > s.maxFileSize) {
                    //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + files[i].name + "</b> " + s.sizeErrorStr + getSizeStr(s.maxFileSize) + "</div>").appendTo(obj.errorLog);
                	if(s.showError) jAlert(s.sizeErrorStr + getSizeStr(s.maxFileSize));
                    continue;
                }
                if(s.maxFileCount != -1 && obj.selectedFiles >= s.maxFileCount) {
                    //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + files[i].name + "</b> " + s.maxFileCountErrorStr + s.maxFileCount + "</div>").appendTo(obj.errorLog);
                	if(s.showError) jAlert(s.maxFileCountErrorStr + s.maxFileCount);
                    continue;
                }
                             
                
             // 전체용량 구하기 added by ellepark                 
                obj.totalSize = obj.totalSize + files[i].size;
                
                if (s.totalFileSize != -1 && obj.totalSize > s.totalFileSize) {
                	//if(s.showError) $("<div style='color:red;'><b>" + files[i].name + "</b> " + s.totalSizeErrorStr + "</div>").appendTo(obj.errorLog);
                	if(s.showError)  jAlert(s.totalSizeErrorStr + s.totalFileSize);
              	  	obj.totalSize = obj.totalSize - files[i].size;
                    continue;                
                }
                
                $("#totalSize").html(getSizeStr(obj.totalSize)+"/"+getSizeStr(s.totalFileSize));			                
                // 전체용량 구하기 added by ellepark
                
                obj.selectedFiles++;
                obj.existingFileNames.push(files[i].name);			// 3.1.10 신규
                var ts = s;
                var fd = new FormData();
                var fileName = s.fileName.replace("[]", "");
                fd.append(fileName, files[i]);
                var extraData = s.formData;
                if(extraData) {
                    var sData = serializeData(extraData);
                    for(var j = 0; j < sData.length; j++) {
                        if(sData[j]) {
                            fd.append(sData[j][0], sData[j][1]);
                        }
                    }
                }
                ts.fileData = fd;

                var pd = new createProgressDiv(obj, s);
                var fileNameStr = "";
                if(s.showFileCounter) fileNameStr = obj.fileCounter + s.fileCounterStyle + files[i].name
                else fileNameStr = files[i].name;

                pd.filename.html(fileNameStr);
                pd.filesize.html(files[i].size);		// 파일 사이즈 added by ellepark
                var form = $("<form style='display:block; position:absolute;left: 150px;' class='" + obj.formGroup + "' method='" + s.method + "' action='" +
                    s.url + "' enctype='" + s.enctype + "'></form>");
                form.appendTo('body');
                var fileArray = [];
                fileArray.push(files[i].name);
                ajaxFormSubmit(form, ts, pd, fileArray, obj, files[i]);
                obj.fileCounter++;
            }
        }

        function isFileTypeAllowed(obj, s, fileName) {
            var fileExtensions = s.allowedTypes.toLowerCase().split(",");
            var ext = fileName.split('.').pop().toLowerCase();
            if(s.allowedTypes != "*" && jQuery.inArray(ext, fileExtensions) < 0) {
                return false;
            }
            return true;
        }

        function isFileDuplicate(obj, filename) {
            var duplicate = false;
            if (obj.existingFileNames.length) {
                for (var x=0; x<obj.existingFileNames.length; x++) {
                    if (obj.existingFileNames[x] == filename
                        || s.duplicateStrict && obj.existingFileNames[x].toLowerCase() == filename.toLowerCase()
                    ) {
                        duplicate = true;
                    }
                }
            }
            return duplicate;
        }

        function removeExistingFileName(obj, fileArr) {
            if (obj.existingFileNames.length) {
                for (var x=0; x<fileArr.length; x++) {
                    var pos = obj.existingFileNames.indexOf(fileArr[x]);
                    if (pos != -1) {
                        obj.existingFileNames.splice(pos, 1);
                    }
                }
            }
        }
        
        /**
         * 기 첨부된 파일 목록 삭제처리 added by ellepark
         */
        function isDbFileDuplicate(obj, filename) {
            var duplicate = false;
            if (obj.dbFileNames.length) {
                for (var x=0; x<obj.dbFileNames.length; x++) {
                    if (obj.dbFileNames[x] == filename
                        || s.duplicateStrict && obj.dbFileNames[x].toLowerCase() == filename.toLowerCase()
                    ) {
                        duplicate = true;
                    }
                }
            }
            return duplicate;
        }

        function getSrcToPreview(file, obj) {
            if(file) {
                obj.show();
                var reader = new FileReader();
                reader.onload = function (e) {
                    obj.attr('src', e.target.result);
                };
                reader.readAsDataURL(file);
            }
        }

        function updateFileCounter(s, obj) {
            if(s.showFileCounter) {
                var count = $(".ajax-file-upload-filename").length;
                obj.fileCounter = count + 1;
                $(".ajax-file-upload-filename").each(function (i, items) {
                    var arr = $(this).html().split(s.fileCounterStyle);
                    var fileNum = parseInt(arr[0]) - 1; //decrement;
                    var name = count + s.fileCounterStyle + arr[1];
                    $(this).html(name);
                    count--;
                });
            }
        }

        function createCutomInputFile(obj, group, s, uploadLabel) {

            var fileUploadId = "ajax-upload-id-" + (new Date().getTime());

            var form = $("<form method='" + s.method + "' action='" + s.url + "' enctype='" + s.enctype + "'></form>");
            var fileInputStr = "<input type='file' id='" + fileUploadId + "' name='" + s.fileName + "' accept='" + s.acceptFiles + "'/>";
            if(s.multiple) {
                if(s.fileName.indexOf("[]") != s.fileName.length - 2) // if it does not endwith
                {
                    s.fileName += "[]";
                }
                fileInputStr = "<input type='file' id='" + fileUploadId + "' name='" + s.fileName + "' accept='" + s.acceptFiles + "' multiple/>";
            }
            var fileInput = $(fileInputStr).appendTo(form);

            fileInput.change(function () {

                obj.errorLog.html("");
                var fileExtensions = s.allowedTypes.toLowerCase().split(",");
                var fileArray = [];
                if(this.files) //support reading files
                {
                    for(i = 0; i < this.files.length; i++) {
                        fileArray.push(this.files[i].name);
                    }

                    if(s.onSelect(this.files) == false) return;
                } else {
                    var filenameStr = $(this).val();
                    var flist = [];
                    fileArray.push(filenameStr);
                    // modify ellepark
                    if(isFileTypeAllowed(obj, s, filenameStr)) {
                        //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + filenameStr + "</b> " + s.extErrorStr + s.allowedTypes + "</div>").appendTo(obj.errorLog);
                    	if(s.showError)  jAlert(s.extErrorStr + s.allowedTypes);
                        return;
                    }
                    //fallback for browser without FileAPI
                    flist.push({
                        name: filenameStr,
                        size: 'NA'
                    });
                    if(s.onSelect(flist) == false) return;

                }
                updateFileCounter(s, obj);

                uploadLabel.unbind("click");
                form.hide();
                createCutomInputFile(obj, group, s, uploadLabel);

                form.addClass(group);
                if(feature.fileapi && feature.formdata) //use HTML5 support and split file submission
                {
                    form.removeClass(group); //Stop Submitting when.
                    var files = this.files;
                    serializeAndUploadFiles(s, obj, files);
                } else {
                    var fileList = "";
                    for(var i = 0; i < fileArray.length; i++) {
                        if(s.showFileCounter) fileList += obj.fileCounter + s.fileCounterStyle + fileArray[i] + "<br>";
                        else fileList += fileArray[i] + "<br>";;
                        obj.fileCounter++;

                    }
                    if(s.maxFileCount != -1 && (obj.selectedFiles + fileArray.length) > s.maxFileCount) {
                        //if(s.showError) $("<div class='" + s.errorClass + "'><b>" + fileList + "</b> " + s.maxFileCountErrorStr + s.maxFileCount + "</div>").appendTo(obj.errorLog);
                    	if(s.showError) jAlert(s.maxFileCountErrorStr + s.maxFileCount);
                        return;
                    }
                    obj.selectedFiles += fileArray.length;

                    var pd = new createProgressDiv(obj, s);
                    pd.filename.html(fileList);
                    ajaxFormSubmit(form, s, pd, fileArray, obj, null);
                }



            });

            if(s.nestedForms) {
                form.css({
                    'margin': 0,
                    'padding': 0
                });
                uploadLabel.css({
                    position: 'relative',
                    overflow: 'hidden',
                    cursor: 'default'
                });
                fileInput.css({
                    position: 'absolute',
                    'cursor': 'pointer',
                    'top': '0px',
                    'width': '100%',
                    'height': '100%',
                    'left': '0px',
                    'z-index': '100',
                    'opacity': '0.0',
                    'filter': 'alpha(opacity=0)',
                    '-ms-filter': "alpha(opacity=0)",
                    '-khtml-opacity': '0.0',
                    '-moz-opacity': '0.0'
                });
                form.appendTo(uploadLabel);

            } else {
                form.appendTo($('body'));
                form.css({
                    margin: 0,
                    padding: 0,
                    display: 'block',
                    position: 'absolute',
                    left: '-250px'
                });
                if(navigator.appVersion.indexOf("MSIE ") != -1) //IE Browser
                {
                    uploadLabel.attr('for', fileUploadId);
                } else {
                    uploadLabel.click(function () {
                        fileInput.click();
                    });
                }
            }
        }


        function createProgressDiv(obj, s) {
            this.statusbar = $("<div class='ajax-file-upload-statusbar'></div>");//.width(s.statusBarWidth);
            this.preview = $("<img class='ajax-file-upload-preview' />").width(s.previewWidth).height(s.previewHeight).appendTo(this.statusbar).hide();
            this.filename = $("<div class='ajax-file-upload-filename'></div>").appendTo(this.statusbar);
            this.progressDiv = $("<div class='ajax-file-upload-progress'>").appendTo(this.statusbar).hide();
            this.progressbar = $("<div class='ajax-file-upload-bar " + obj.formGroup + "'></div>").appendTo(this.progressDiv);
            this.abort = $("<div class='ajax-file-upload-red " + s.abortButtonClass + " " + obj.formGroup + "'>" + s.abortStr + "</div>").appendTo(this.statusbar)
                .hide();
            this.cancel = $("<div class='x_button ajax-file-upload-cancel " + obj.formGroup + "'>" + s.cancelStr + "</div>").appendTo(this.statusbar).hide(); 
            this.done = $("<div class='ajax-file-upload-green'>" + s.doneStr + "</div>").appendTo(this.statusbar).hide();
            this.download = $("<div class='ajax-file-upload-green'>" + s.downloadStr + "</div>").appendTo(this.statusbar).hide();
            this.del = $("<div class='ajax-file-upload-red'>" + s.deletelStr + "</div>").appendTo(this.statusbar).hide();
            this.filesize = $("<div class='ajax-file-upload-filesize'></div>").appendTo(this.statusbar).hide();			// add elleaprk
            if(s.showQueueDiv)
                $("#" + s.showQueueDiv).append(this.statusbar);
            else
                obj.errorLog.after(this.statusbar);
            return this;
        }


        function ajaxFormSubmit(form, s, pd, fileArray, obj, file) {
            var currentXHR = null;
            var options = {
                cache: false,
                contentType: false,
                processData: false,
                forceSync: false,
                type: s.method,
                data: s.formData,
                formData: s.fileData,
                dataType: s.returnType,
                beforeSubmit: function (formData, $form, options) {
                    if(s.onSubmit.call(this, fileArray) != false) {
                        var dynData = s.dynamicFormData();
                        if(dynData) {
                            var sData = serializeData(dynData);
                            if(sData) {
                                for(var j = 0; j < sData.length; j++) {
                                    if(sData[j]) {
                                        if(s.fileData != undefined) options.formData.append(sData[j][0], sData[j][1]);
                                        else options.data[sData[j][0]] = sData[j][1];
                                    }
                                }
                            }
                        }
                        obj.tCounter += fileArray.length;
                        //window.setTimeout(checkPendingUploads, 1000); //not so critical
                        checkPendingUploads();
                        return true;
                    }
                    pd.statusbar.append("<div class='" + s.errorClass + "'>" + s.uploadErrorStr + "</div>");
                    pd.cancel.show()
                    form.remove();
                    pd.cancel.click(function () {
                        removeExistingFileName(obj, fileArray);
                        pd.statusbar.remove();
                        s.onCancel.call(obj, fileArray, pd);
                        obj.selectedFiles -= fileArray.length; //reduce selected File count
                        updateFileCounter(s, obj);
                    });
                    return false;
                },
                beforeSend: function (xhr, o) {

                    pd.progressDiv.show();
                    pd.cancel.hide();
                    pd.done.hide();
                    if(s.showAbort) {
                        pd.abort.show();
                        pd.abort.click(function () {
                            removeExistingFileName(obj, fileArray);
                            xhr.abort();
                            obj.selectedFiles -= fileArray.length; //reduce selected File count
                        });
                    }
                    if(!feature.formdata) //For iframe based push
                    {
                        pd.progressbar.width('5%');
                    } else pd.progressbar.width('1%'); //Fix for small files
                },
                uploadProgress: function (event, position, total, percentComplete) {
                    //Fix for smaller file uploads in MAC
                    if(percentComplete > 98) percentComplete = 98;

                    var percentVal = percentComplete + '%';
                    if(percentComplete > 1) pd.progressbar.width(percentVal)
                    if(s.showProgress) {
                        pd.progressbar.html(percentVal);
                        pd.progressbar.css('text-align', 'center');
                    }

                },
                success: function (data, message, xhr) {

                	var ret = JSON.stringify(data);
    				var server = JSON.parse(ret);
                    //For custom errors.
                    //if(s.returnType == "json" && $.type(data) == "object" && data.hasOwnProperty(s.customErrorKeyStr)) {
    				if(s.returnType == "json" && $.type(data) == "object" && server.result == "fail") {    					
                        pd.abort.hide();
                        //var msg = data[s.customErrorKeyStr];
                        var msg = server.message;	// modify ellepark
                        s.onError.call(this, fileArray, 200, msg, pd);
                        if(s.showStatusAfterError) {
                            pd.progressDiv.hide();
                            pd.statusbar.append("<span class='" + s.errorClass + "'>ERROR: " + msg + "</span>");
                            //jAlert(server.message);		 // added by ellepark        
                        } else {
                            pd.statusbar.hide();
                            pd.statusbar.remove();
                        }
                        /**************** deleted by ellepark ***************************************
                        obj.selectedFiles -= fileArray.length; //reduce selected File count
                        form.remove();
                        obj.fCounter += fileArray.length;
                         ******************************************************************************/
                        // added by ellepark 
                        if(s.showCancel) {                	
                            pd.cancel.show();
                            pd.cancel.click(function () {
                                form.remove();                       
                                pd.statusbar.remove();
                                s.onCancel.call(obj, fileArray, pd);                                             
                                obj.selectedFiles -= fileArray.length; //reduce selected File count                                
                                updateFileCounter(s, obj);
                                
                            });
                        }
                        // added by ellepark 
                        
                        return;
                    }
                    //For custom errors.
                    
                    obj.responses.push(data);
                    pd.progressbar.width('100%')
                    if(s.showProgress) {
                        pd.progressbar.html('100%');
                        pd.progressbar.css('text-align', 'center');
                    }

                    pd.abort.hide();
                    s.onSuccess.call(this, fileArray, data, xhr, pd);
                    if(s.showStatusAfterSuccess) {
                        if(s.showDone) {
                            pd.done.show();
                            pd.done.click(function () {
                                pd.statusbar.hide("slow");
                                pd.statusbar.remove();
                            });
                        } else {
                            pd.done.hide();
                        }
                        if(s.showDelete) {
                            pd.del.show();
                            pd.del.click(function () {
                                pd.statusbar.hide().remove();
                                if(s.deleteCallback) s.deleteCallback.call(this, data, pd);
                                obj.selectedFiles -= fileArray.length; //reduce selected File count
                                updateFileCounter(s, obj);

                            });
                        } else {
                            pd.del.hide();
                        }
                    } else {
                        pd.statusbar.hide("slow");
                        pd.statusbar.remove();

                    }
                    if(s.showDownload) {
                        pd.download.show();
                        pd.download.click(function () {
                            if(s.downloadCallback) s.downloadCallback(data);
                        });
                    }
                    form.remove();
                    obj.sCounter += fileArray.length;
                },
                error: function (xhr, status, errMsg) {
                    pd.abort.hide();
                    if(xhr.statusText == "abort") //we aborted it
                    {
                        pd.statusbar.hide("slow").remove();
                        updateFileCounter(s, obj);

                    } else {
                        s.onError.call(this, fileArray, status, errMsg, pd);
                        if(s.showStatusAfterError) {
                            pd.progressDiv.hide();
                            //pd.statusbar.append("<span class='" + s.errorClass + "'>ERROR: " + errMsg + "</span>");
                            jAlert(s.serverErrorStr);	 // added by ellepark        
                        } else {
                            pd.statusbar.hide();
                            pd.statusbar.remove();
                        }
                        
                        //obj.selectedFiles -= fileArray.length; //reduce selected File count
                    }

                    /**************** deleted by ellepark ***************************************
                    form.remove();
                    obj.fCounter += fileArray.length;
                     ******************************************************************************/

                    // added by ellepark 
                    if(s.showCancel) {                	
                        pd.cancel.show();
                        pd.cancel.click(function () {
                            form.remove();                       
                            pd.statusbar.remove();
                            s.onCancel.call(obj, fileArray, pd);                                             
                            obj.selectedFiles -= fileArray.length; //reduce selected File count
                            updateFileCounter(s, obj);
                        });
                    }
                    // added by ellepark 
                    
                }
            };

            if(s.showPreview && file != null) {
                if(file.type.toLowerCase().split("/").shift() == "image") getSrcToPreview(file, pd.preview);
            }

            if(s.autoSubmit) {
                form.ajaxSubmit(options);
            } else {
                if(s.showCancel) {
                    pd.cancel.show();
                    pd.cancel.click(function () {
                        removeExistingFileName(obj, fileArray);
                        form.remove();
                        pd.statusbar.remove();
                        s.onCancel.call(obj, fileArray, pd);
                        obj.selectedFiles -= fileArray.length; //reduce selected File count
                        updateFileCounter(s, obj);
                        
                        // file size refresh added by ellepark
                        var filesize = pd.filesize.html();		
                        obj.totalSize = obj.totalSize - filesize;
                        $("#totalSize").html(getSizeStr(obj.totalSize)+"/"+getSizeStr(s.totalFileSize));
                        
                    });
                }
                form.ajaxForm(options);

            }

        }
        return this;

    }
}(jQuery));
