/**
 *
 * Created by UTSC0885 on 2017/5/17.
 */


function  setSelectVsp(id){
    $.ajax({
        type: 'GET',
        url: projectName + "/admin/vsp/listAllVsp.do",
        //data: {type:type},
        async: false,
        success: function (result) {
            if (result.code == 0) {
                var arr = result.vo;
                for(var i=0;i<arr.length;i++){
                    var vo = arr[i];
                    $("#"+id).append("<option value='"+vo.vspid+"'>"+vo.name+"</option>");
                }
            } else {
                $.fn.modalAlert("获取vsp下拉框数据发生错误", "error");
            }
        }
    });
}

//get upload picture path得到配置文件里文件上传的路径
function  getUploadPicturePath(){
	var path;
    $.ajax({
        type: 'GET',
        url: projectName + "/content/getUploadPicturePath.do",
        //data: {type:type},
        async: false,
        success: function (result) {
            if (result.code == 0) {
                  path = result.message;
            } else {
                $.fn.modalAlert("获取vsp下拉框数据发生错误", "error");
            }
        }
    });
    return path;
}


/*
 * jeferry 下载视频 
 * 表格id
 *  
 */
function btnDownloadVideo(mediacontentid,this_e){
	/*tableid = "#" + tableid; 
	var arrayData = $(tableid).bootstrapTable('getSelections');
	if (arrayData[0] == undefined || arrayData[0] == null
			|| arrayData[0] == '') {
		$.fn.modalAlert("请选择表格数据", "error");
		return;
	}
	
	if (arrayData.length > 1) {
		$.fn.modalAlert("请选择一行数据", "error");
		return;
	}
	
	//只能单个下载
	var mediacontentid = arrayData[0].videoid;
	*/
	 $(this_e).css("color","red"); 
	 var url = projectName + "/content/downloadFtpFile.do?mediacontentid=" + mediacontentid;	 	
	 window.location.href = url;
	 	
	 //do not use ajax submmit, otherwise the reponse will not show the dialog.
	 //$.ajaxSubmit(url,false ,null, onDownloadSuccess);
}

//jeferry 验证日期（判断结束日期是否大于开始日期）日期格式为YY—MM—DD  
function verifiedDate(startTime,endTime){
	if(startTime && endTime){
	    if(startTime.length>0 && endTime.length>0){
	    	startTime = startTime.substring(0,10);
	    	endTime = endTime.substring(0,10);    
	        var startTmp=startTime.split("-");  
	        var endTmp=endTime.split("-");  
	        var sd=new Date(startTmp[0],startTmp[1],startTmp[2]);  
	        var ed=new Date(endTmp[0],endTmp[1],endTmp[2]);  
	        if(sd.getTime()>ed.getTime()){   
	            return false;     
	        }     
	    } 
	}
    return true;     
}

function propertyList(objArr, property) {
    var result = [];
    if (typeof objArr == "undefined" || typeof property == "undefined") {
        return;
    }
    if (typeof objArr == "object") {
        for (var i = 0; i < objArr.length; i++) {
            result.push(objArr[i][property]);
        }
    }
    return result;
}

function objListByPropValList(objArr, property, value) {
    var result = [];
    if (typeof objArr == "undefined" || typeof property == "undefined" || typeof value == "undefined" || value.length <= 0) {
        return;
    }
    if (typeof objArr == "object") {
        for (var i = 0; i < objArr.length; i++) {
            for (var j = 0; j < value.length; j++) {
                if (objArr[i][property] == value[j]) {
                    result.push(objArr[i]);
                    break;
                }
            }
        }
    }
    return result;
}

function isExistProVal(objArr, property, value) {
    if (typeof objArr == "undefined" || typeof property == "undefined" || typeof value == "undefined") {
        return false;
    }
    if (typeof objArr == "object") {
        for (var i = 0; i < objArr.length; i++) {
            var currval = objArr[i][property];
            if (currval == value) {
                return true;
            }
        }
    }
    return false;
}

function arr2json(arr) {
    var json = {};
    for (var i = 0; i < arr.length; i++) {
        var temp = arr[i];
        json[temp.name] = temp.value;
    }
    return json;
}

function showErrcode(code) {
    if (typeof errCodeDef[code] == 'undefined') {
        $.fn.modalAlert("错误码未定义", "error");
        return;
    }
    var type = "error";
    if (code == "0") {
        type = "success";
    }
    $.fn.modalAlert(errCodeDef[code], type);
}




