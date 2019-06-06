/**
 *
 * Created by
 */

window.operateEvents = {
    'click .RoleOfdelete': function (e, value, row, index) {
        console.log(row)
        /*var arrselections = $("#tb_departments").bootstrapTable('getSelections');
        if (arrselections.length <= 0) {
            toastr.warning('请选择有效数据');
            return;
        }*/
        var code = row.mediacode;
        console.log(code)
       var boo =  confirm( "确认要删除《"+row.medianame+"》这部媒资吗？")
            if (boo==false) {
                return;
            }
            $.ajax({
                type: "post",
                contentType : "application/json",
                url: projectName + "/index/delete.do",
                data:  JSON.stringify(code),
                success: function (data, status) {
                    console.log(data)
                    if(data.success == true){
                        alert("删除成功！");
                    }else{
                        alert("删除失败:"+data.message);
                    }
                    $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
                },
                error: function () {
                    alert("删除失败");
                    $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
                },
                complete: function () {
                }

            });
    },
    'click .RoleOfedit': function (e, value, row, index) {
        console.log(row)
        $('#updatemyModal').modal();
        $('#update_key').val(row.mediacode);
        $('#update_mediacode').val(row.mediacode);
        $('#update_medianame').val(row.medianame);
    }
};

$(function () {
    $('#btn_add').click(function () {
        $("#myModalLabel").text("新增媒资");
        $('#myModal').modal();
    });
});

$(function () {
    $('#btn_submit').click(function () {
        var codekey = $('#update_key').val();
        var mediacode = $('#update_mediacode').val();
         var medianame = $('#update_medianame').val();
        var boo =  confirm( "确认要修改这部媒资吗？")
        if (boo==false) {
            return;
        }
        var code = codekey+"@"+mediacode+"@"+medianame+"@";
        console.log(code)
        $.ajax({
            type: "post",
            contentType : "application/json",
            url: projectName + "/index/edit.do",
            data:  JSON.stringify(code),
            success: function (data, status) {
                console.log(data)
                if(data.success == true){
                    alert("修改成功！");
                }else{
                    alert("修改失败:"+data.message);
                }
                $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
            },
            error: function () {
                alert("修改失败");
                $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
            },
            complete: function () {
            }

        });
    });
});;

$(function () {
    $('#btn_remove').click(function () {
        var arrselections = $("#listViewingLogTable").bootstrapTable('getSelections');
        console.log(arrselections)
         if (arrselections.length <= 0) {
             alert('当前未勾选数据');
             return;
         }
        var code = '';
        for(var i=0;i<arrselections.length;i++){
            code +=arrselections[i].mediacode+"@";
         }
        console.log(code)
        var boo =  confirm( "当前删除媒资数量"+arrselections.length+",确定删除吗？")
        if (boo==false) {
            return;
        }
        $.ajax({
            type: "post",
            contentType : "application/json",
            url: projectName + "/index/delete.do",
            data:  JSON.stringify(code),
            success: function (data, status) {
                console.log(data)
                if(data.success == true){
                    alert("删除成功！");
                }else{
                    alert("删除失败:"+data.message);
                }
                $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
            },
            error: function () {
                alert("删除失败");
                $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
            },
            complete: function () {
            }

        });
    });
});

var TableInit = function () {
    var oTableInit = {};
    oTableInit.Init = function () {
        var htmlHeight = $('html')[0].clientHeight;
        var contentBoxHeight = $('.content-box')[0].offsetHeight;
        var height;
        if (parseInt(htmlHeight) < parseInt(contentBoxHeight)) {
            height = null;
        } else {
            height = htmlHeight - contentBoxHeight;
        }
        $('#listViewingLogTable').bootstrapTable({
            url: projectName + '/index/ListviewLog.do',  //请求后台的URL（*）
            contentType: 'application/x-www-form-urlencoded',
            method: 'post',                      //请求方式（*）
            undefinedText: '',
            striped: true,                      //是否显示行间隔色
            pagination: true,                   //是否显示分页（*）
            queryParams: oTableInit.queryParams,//传递参数（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 20,                       //每页的记录行数（*）
            pageList: [20, 50],        //可供选择的每页的行数（*）
            clickToSelect: false,                //是否启用点击选中行
            height: height,
            singleSelect: false,
            showExport: phoneOrPc(),              //是否显示导出按钮(此方法是自己写的目的是判断终端是电脑还是手机,电脑则返回true,手机返回falsee,手机不显示按钮)
            exportDataType: "basic",           //basic', 'all', 'selected'.
            idField: 'id',
            exportTypes:['excel'],	    //导出类型
            //exportButton: $('#btn_export'),     //为按钮btn_export  绑定导出事件  自定义导出按钮(可以不用)
            exportOptions:{
                //ignoresysid: [0,0],            //忽略某一列的索引
                fileName: '数据导出',              //文件名称设置
                worksheetName: 'Sheet1',          //表格工作区名称
                tableName: '数据表',
                excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
                onMsoNumberFormat: DoOnMsoNumberFormat
            },
            columns: [
                { //编辑datagrid的列
                    title : '勾选',
                    align : 'center',
                    checkbox : true
                },
                {
                    field: 'number',
                    align: 'center',
                    title: '序号',
                    formatter:function (value, row, index) {
                        var options = $('#listViewingLogTable').bootstrapTable("getOptions");
                        return (options.pageNumber-1)*(options.pageSize)+index+1;
                    }
                },
                {
                    field: 'medianame',
                    align: 'center',
                    title: '媒质名称',
                },{
                    field: 'mediacode',
                    align: 'center',
                    title: '媒质code',
                },{
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    width : 100,
                    events: operateEvents,
                    formatter: operateFormatter
                }],
            pagination:true
        });

        $('#listViewingLogTable').on('check.bs.table', function (e, row, $element) {
            var parent = $element.parents('tr');
            parent.addClass('success');
        });
        $('#listViewingLogTable').on('uncheck.bs.table', function (e, row, $element) {
            var parent = $element.parents('tr');
            parent.removeClass('success');
        });

        $('#listViewingLogTable').on('check-all.bs.table', function (e, row, $element) {
            var trElement = $(this).find('tr');
            trElement.addClass('success');
        });
        $('#listViewingLogTable').on('uncheck-all.bs.table', function (e, row, $element) {
            var trElement = $(this).find('tr');
            trElement.removeClass('success');
        });
    }

    function DoOnMsoNumberFormat(cell, row, col) {
        var result = "";
        if (row > 0 && col == 0)
            result = "\\@";
        return result;
    }

    function phoneOrPc() {
        var sUserAgent = navigator.userAgent.toLowerCase();
        var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
        var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
        var bIsMidp = sUserAgent.match(/midp/i) == "midp";
        var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
        var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
        var bIsAndroid = sUserAgent.match(/android/i) == "android";
        var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
        var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
        if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
            return false;
        } else {
            return true;
        }
    }

    //得到查询的参数


    oTableInit.queryParams = function (params) {
        var temp = {
            limit: params.limit,
            offset: params.offset,
            sort: params.sort,
            order: params.order,
            madianame: $('#medianame').val(),
            madiacode: $('#mediacode').val(),
        };
        console.log(temp)
        return temp;
    };
    return oTableInit;
};


$(function () {

    $('#btn_query').click(function () {
        $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
    });


    $(document).keypress(function (e) {
        // 回车键事件
        if (e.which == 13) {
            $("#btn_query").click();
        }
    });

    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2、初始化区域下拉框
    // initPlatform();


});
var FileInput = function () {
    var oFile = new Object();

    //初始化fileinput控件（第一次初始化）
    oFile.Init = function(ctrlName, uploadUrl) {
        var control = $('#' + ctrlName);

        //初始化上传控件的样式
        control.fileinput({
            language:"zh",//设置语言
            uploadUrl:uploadUrl,//上传的地址
            uploadAsync:true,//默认异步上传
            showCaption:true,//是否显示标题
            showUpload:true,//是否显示上传按钮
            browseClass:"btn btn-primary",//按钮样式
            allowedFileExtensions: ["xls", "xlsx"], //接收的文件后缀
            maxFileCount: 1,//最大上传文件数限制
            previewFileIcon:'<i class="glyphicon glyphcion-file"></i>',
            showPreview: true, //是否显示预览
// 									allowPreviewTypes:null,//是否显示预览
            previewFileIconSettings:{
                'docx':'<i class="glyphicon glyphcion-file"></i>',
                'xlsx':'<i class="glyphicon glyphcion-file"></i>',
                'pptx':'<i class="glyphicon glyphcion-file"></i>',
                'jpg':'<i class="glyphicon glyphcion-picture"></i>',
                'pdf':'<i class="glyphicon glyphcion-file"></i>',
                'zip':'<i class="glyphicon glyphcion-file"></i>',
            }
        });

        //导入文件上传完成之后的事件
        $("#txt_file").on("fileuploaded", function (event, data, previewId, index) {
            console.log("data:"+data.response.success);
            if(data.response.success == true){
                alert("导入成功！");
                $("#txt_file").fileinput("clear");
                $("#txt_file").fileinput("reset");
                $("#txt_file").fileinput("refresh");
                $("#txt_file").fileinput("enable");
                $(".close").click();
                $("#reload").click();
            }else{
                alert("导入失败:"+data.response.message);
                $("#txt_file").fileinput("clear");
                $("#txt_file").fileinput("reset");
                $("#txt_file").fileinput("refresh");
                $("#txt_file").fileinput("enable");
            }
            $("#myModal").modal("hide");
            $('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
        });
    }
    return oFile;
};