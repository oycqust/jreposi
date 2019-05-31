var TableInit = function () {
        var htmlHeight = $('html')[0].clientHeight;
        var topHeight = $("#search-div").outerHeight(true);
        var height = htmlHeight - topHeight;
        //var contentBoxHeight = $('.content-box')[0].offsetHeight;
        var height;
        //if (parseInt(htmlHeight) < parseInt(contentBoxHeight)) {
            //height = null;
        //} else {
            //height = htmlHeight - contentBoxHeight;
        //}
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#ArbetTable').bootstrapTable({
                url: '/acp/activity/list',         //请求后台的URL（*）
                method: 'post',                      //请求方式（*）
                toolbar: '#toolbar1',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                locale : 'zh-CN',
                pagination: true,                  //是否显示分页（*）
                paginationPreText:'上一页',
                paginationNextText:'下一页',
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
                contentType: "application/x-www-form-urlencoded",
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                uniqueId: "no",                     //每一行的唯一标识，一般为主键列
                height : height,
                showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                columns: [
                { //编辑datagrid的列
                    title : '勾选',
                    align : 'center',
                    checkbox : true
                },{
                    field: 'rowNum',
                    title: '序号',
                    align : 'center',
                    formatter: function (value, row, index)
                    {
                        return index+1;
                    }
                }, {
                    field: 'id',
                    title: 'ID'
                }, {
                    field: 'activityId',
                    title: '活动ID',
                    align : 'center'
                }, {
                    field: 'activityName',
                    title: '活动名称',
                    align : 'center'
                }, {
                    field: 'sysId',
                    title: '运营商',
                    align : 'center',
                    formatter: function(value, row, index)
                    {
                        var r = "电信";
                        if(value){
                            var sysIdArr = value.split(",");
                            var resultArr = [];
                            for(var i in sysIdArr){
                                var item = sysIdArr[i];
                                switch(item)
                                {
                                    case "m":
                                      r = "移动";
                                      break;
                                    case "u":
                                      r = "联通";
                                      break;
                                    case "t":
                                      r = "电信";
                                      break;
                                }
                                resultArr.push(r);
                            }
                                return resultArr.join(",");
                        }

                        return r;
                    }
                }, {
                    field: 'validTime',
                    title: '开始时间',
                    align : 'center'
                }, {
                    field: 'expireTime',
                    title: '结束时间',
                    align : 'center'
                }, {
                    field: 'statusType',
                    title: '状态',
                    align : 'center',
                    formatter: function(value, row, index)
                    {
                        var r = "无效";
                        switch(value)
                        {
                            case "VALID":
                                r = "有效";
                              break;
                        }
                        return r;
                    }
                 }, {
                    field: 'zeroDel',
                    title: '活动类型',
                    align : 'center',
                    formatter: function(value, row, index)
                    {
                        var r = "持续增长";
                        switch(value)
                        {
                            case "DEL_DAY":
                                r = "每日清零";
                              break;
                        }
                        return r;
                    }
                 }, {
                    field: 'duration',
                    title: '观看时长',
                    align : 'center'
                 }, {
                    field: 'point',
                    title: '积分',
                    align : 'center'
                }, {
                field: 'limit',
                    title: '上限',
                    align : 'center'
                }, {
                    field: 'operation',
                    title: '操作人',
                    align : 'center'
                }, {
                    field: 'dataType',
                    title: '媒资内容',
                    align : 'center',
                    formatter: function(value, row, index)
                    {
                        return activityObj.dataTypeObj[value];
                    }
                }, {
                    field: 'lastUpdate',
                    title: '操作时间',
                    align : 'center'
                }
                ],
                rowStyle: function (row, index) {
                    var classesArr = ['success', 'info'];
                    var strclass = "";
                    if (row.isValid) {
                        strclass = classesArr[0];
                    }
                    return { classes: strclass };
                },//隔行变色
            });
        $('#ArbetTable').bootstrapTable('hideColumn','id');
        };


        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset:params.offset,
                activityId: $("#activityId-search").val(),
                activityName: $("#activityName-search").val(),
                validTime: $("#validTime-search").val(),
                expireTime: $("#expireTime-search").val(),
                statusType: $("#statusType-search").val()
            };
            return temp;
        };
        return oTableInit;
    };

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
                uploadExtraData: function(previewId, index) {   //额外参数的关键点
                    var obj = {};
                    obj.uuid = chooseObj.uuid;
                    return obj;
                },
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
                    $("#choose-TEXT").val("true");
                    $("#mediaCode").val(data.response.message);
                }else{
                    alert("导入失败:"+data.response.message);
                    $("#txt_file").fileinput("clear");
                    $("#txt_file").fileinput("reset");
                    $("#txt_file").fileinput("refresh");
                    $("#txt_file").fileinput("enable");
                }
                //$("#myModal").modal("hide");
                $("#TEXT-ChooseModal").modal("hide");
                //$('#listViewingLogTable').bootstrapTable('refreshOptions', {pageNumber: 1});
            });
        }
        return oFile;
    };