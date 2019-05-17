<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>活动页面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap-table.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/activity/list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/css/fileinput.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/chosen/chosen.min.css"/>
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
  </head>
  <body>
   <table id="ArbetTable">
       <form id="searchForm">
            <div id="search-div">
                <p class="text-center" style="font-size:25px;margin-top:10px;padding-top: 10px;">活动管理</p>
               <div class="form-group" >
                   <label for="activityId-search">活动ID</label>
                   <input type="text" class="form-control search-container" id="activityId-search" name="activityId">
               </div>
               <div class="form-group" >
                   <label for="activityName-search">活动名称</label>
                   <input type="text" class="form-control search-container" id="activityName-search" name="activityName">
               </div>
               <div class="form-group" >
                   <label for="validTime-search">开始时间</label>
                   <input type="text" class="form-control search-container search-date" id="validTime-search" name="validTime">
               </div>
                <div class="form-group" >
                    <label for="expireTime-search">结束时间</label>
                    <input type="text" class="form-control search-container search-date" id="expireTime-search" name="expireTime">
                </div>
                <div class="form-group">
                     <label for="statusType-search">状态</label>
                     <select name="statusType" id="statusType-search" class="form-control search-container">
                       <option value="" selected="selected">全部</option>
                       <option value="VALID">有效</option>
                       <option value="EXPIRED" >无效</option>
                     </select>
               </div>
            </div>
       </form>

   </table>
    <div id="toolbar1" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_edit" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
        </button>
        <button id="btn_delete" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="btn_search" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>搜索
        </button>
    </div>

            <!--  add modal start-->
            <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="addModal">
                      <div class="modal-dialog modal-lg" role="document">
                          <div class="modal-content">
                            <!-- content start -->
                            <div class="modal-header">
                                <!--<button type="button" class="close" onclick="activityObj.closeAddModal()" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
                                <h4 class="modal-title">新增活动</h4>
                              </div>
                              <div class="modal-body">
                                <form id="addForm">
                                  <div class="form-group">
                                    <label for="activityId">活动ID</label>
                                    <input type="text" class="form-control" id="activityId" name="activityId" placeholder="请填写活动id">
                                  </div>
                                  <div class="form-group">
                                    <label for="activityName">活动名称</label>
                                    <input type="text" class="form-control" id="activityName" name="activityName" placeholder="请填写活动名称">
                                  </div>
                                  <div class="form-group">
                                    <label for="validTime">开始时间</label>
                                    <input type="text" class="form-control activity-date" id="validTime"
                                            name="validTime" placeholder="请选择开始时间">
                                  </div>
                                  <div class="form-group">
                                    <label for="expireTime">结束时间</label>
                                    <input type="text" class="form-control activity-date" id="expireTime"
                                            name="expireTime" placeholder="请选择结束时间">
                                  </div>

                                    <div class="form-group">
                                        <label for="activityType">活动类型</label>
                                        <select name="activityType" id="activityType" class="form-control">
                                            <option value="INCREASE" selected="selected">增加</option>
                                            <option value="DECREASE" >消减</option>
                                        </select>
                                      </div>
                                        <div class="form-group">
                                          <label for="sysId">运营商</label>
                                          <select name="sysId" id="sysId" class="form-control">
                                              <option value="t" selected="selected">电信</option>
                                              <option value="m" >移动</option>
                                              <option value="u" >联通</option>
                                          </select>
                                        </div>
                                        <div class="form-group">
                                          <label for="joinType">参与类型</label>
                                          <select name="joinType" id="joinType" class="form-control">
                                              <option value="t" selected="selected">当日清零</option>
                                              <option value="m" >持续累积</option>
                                          </select>
                                        </div>
                                    <div class="form-group">
                                          <label for="dataType">媒资内容</label>
                                          <select name="dataType" class="form-control" id="dataType">
                                            <option value="" >请选择媒资内容</option>
                                            <option value="CATEGORY" >栏目</option>
                                            <option value="TAG" >标签</option>
                                            <option value="COLUMN" >一级分类</option>
                                            <option value="TYPE" >类型</option>
                                            <option value="TEXT" >文本导入</option>
                                          </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="duration">最少观看时长</label>
                                        <input type="text" class="form-control" id="duration" name="duration" placeholder="请填写观看时长(s)">
                                    </div>
                                    <div class="form-group">
                                        <label for="limit">参与上限</label>
                                        <input type="text" class="form-control" id="limit" name="limit" placeholder="请填写参与上限">
                                    </div>
                                    <div class="form-group">
                                        <label for="point">积分</label>
                                        <input type="text" class="form-control" id="point" name="point" placeholder="请填写积分">
                                    </div>
                                    <div class="form-group">
                                          <label for="transmissionMode">数据获取方式</label>
                                          <select name="transmissionMode" id="transmissionMode" class="form-control">
                                            <option value="FTP" selected="selected">ftp</option>
                                            <option value="INTERFACE" >接口查询</option>
                                          </select>
                                    </div>
                                    <div class="form-group">
                                      <label for="statusType">状态</label>
                                      <select name="statusType" id="statusType" class="form-control">
                                          <option value="VALID" selected="selected">有效</option>
                                          <option value="EXPIRED" >无效</option>
                                      </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="usertype">test</label>
                                        <select id="usertype" name="usertype" class="selectpicker show-tick form-control" multiple data-live-search="true">
                                            <c:forEach var="category" items="${categorys}">
                                                <option value="${category.CODE}">"${category.NAME}"</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <input name="uuid" id="uuid" type="hidden">
                                    <input name="mediaCode" id="mediaCode" type="hidden">
                                    <input name="id" id="id" type="hidden">
                                </form>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="button" id="saveBtn" class="btn btn-primary">提交</button>
                              </div>
                            <!-- content end -->

                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
        </div>
        <!--  add modal end-->
        <!--  category modal start-->
        <div class="modal fade bs-example-modal-md" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" id="CATEGORY-ChooseModal">
          <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <!-- content start -->
                <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">选择栏目</h4>
                      </div>
                      <div class="modal-body">
                            <form id="chooseSourceForm">
                            <div>
                               <select id = "choose-CATEGORY" class="chosenCategory chosen-select" style="width:80%;">
                                   <option value="">请选择栏目</option>
                                   <c:forEach var="category" items="${categorys}">
                                       <option value="${category.CODE}">"${category.NAME}"</option>
                                   </c:forEach>
                               </select>
                               </div>
                              </form>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
                      </div>
                <!-- content end -->
            </div>
          </div>
        </div>
        <!--  category modal end-->
        <!--  text modal end-->
            <div class="modal fade bs-example-modal-md" id="TEXT-ChooseModal" tabindex="-1" role="dialog" aria-labelledby="textModalLabel">
            <div class="modal-dialog modal-md" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">请选择Excel文件</h4>
                    </div>
                    <div class="modal-body">
                        <a href="${pageContext.request.contextPath}/excelTemplate/example.xls" download=""   class="form-control" style="border:none;">下载导入模板</a>
                        <input type="file" name="txt_file" id="txt_file" multiple class="file-loading" />
                        <select id="choose-TEXT" style="width:80%;display:none;">
                           <option value="" selected="selected"></option>
                           <option value="true" ></option>
                       </select>
                    </div>
                </div>
            </div>
        </div>
        <!--  text modal end-->
        <!--  tag modal start-->
                <div class="modal fade bs-example-modal-md" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" id="TAG-ChooseModal">
                  <div class="modal-dialog modal-md" role="document">
                    <div class="modal-content">
                        <!-- content start -->
                        <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">选择标签</h4>
                              </div>
                              <div class="modal-body">
                                    <div>
                                       <select id="choose-TAG" style="width:80%;">
                                           <option value="" >请选择标签</option>
                                           <option value="operate" >运营标签</option>
                                           <option value="base" >基础标签</option>
                                       </select>
                                       </div>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
                              </div>
                        <!-- content end -->
                    </div>
                  </div>
                </div>
                <!--  tag modal end-->
                <!--  column modal start-->
                <div class="modal fade bs-example-modal-md" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" id="COLUMN-ChooseModal">
                  <div class="modal-dialog modal-md" role="document">
                    <div class="modal-content">
                        <!-- content start -->
                        <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">选择一级分类</h4>
                              </div>
                              <div class="modal-body">
                                    <div>
                                       <select id="choose-COLUMN" style="width:80%;">
                                           <option value="">请选择一级分类</option>
                                       </select>
                                       </div>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
                              </div>
                        <!-- content end -->
                    </div>
                  </div>
                </div>
                <!--  column modal end-->
                <!--  type modal start-->
                <div class="modal fade bs-example-modal-md" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" id="TYPE-ChooseModal">
                  <div class="modal-dialog modal-md" role="document">
                    <div class="modal-content">
                        <!-- content start -->
                        <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">选择类型</h4>
                              </div>
                              <div class="modal-body">
                                    <div>
                                       <select id="choose-TYPE" style="width:80%;">
                                            <option value="" >请选择类型</option>
                                            <option value="free" >点播免费</option>
                                            <option value="toll" >点播收费</option>
                                            <option value="live" >直播</option>
                                       </select>
                                       </div>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
                              </div>
                        <!-- content end -->
                    </div>
                  </div>
                </div>
                <!--  type modal end-->

    <script src="${pageContext.request.contextPath}/js/common/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap-table-zh-CN.js"></script>
    <script src="${pageContext.request.contextPath}/js/laydate/laydate.js" charset="UTF-8"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootbox.min.js" charset="UTF-8"></script>
    <script src="${pageContext.request.contextPath}/js/common/globa.js"></script>
    <script src="${pageContext.request.contextPath}/query/bootstrap-table-export.js"></script>
    <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/fileinput.min.js"></script>
    <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/chosen.jquery.min.js"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

    <script type = "text/javascript">
        $(function ()
        {
             $('#usertype').selectpicker({
        'selectedText': 'cat'
    });
             //1.初始化Table
             var oTable = new TableInit();
             oTable.Init();

            //0.初始化fileinput
            var oFileInput = new FileInput();
            oFileInput.Init("txt_file", "${pageContext.request.contextPath}/activity/excelInput");

                lay('.search-date').each(function(){
                    laydate.render({
                    elem: this,
                    trigger: 'click',
                    type: 'datetime'
                    });
                });

             $("#btn_add").off('click').on('click', activityObj.addShow);
             $("#btn_edit").off('click').on('click', activityObj.editShow);
             $("#btn_search").off('click').on('click', activityObj.search);
             $("#dataType").on('change', function()
             {
                chooseObj.showChooseModal($(this).val());
             });

             $('#addModal').on('shown.bs.modal', function (e)
             {
                   laydate.render(
                   {
                     elem: '#expireTime',
                     type: 'datetime',
                     format: 'yyyy-MM-dd HH:mm:ss',
                     value: $.isEmptyObject(activityObj.selectRow) ?  '' : activityObj.selectRow.expireTime
                   });
                   laydate.render(
                   {
                     elem: '#validTime',
                     type: 'datetime',
                     format: 'yyyy-MM-dd HH:mm:ss',
                     value: $.isEmptyObject(activityObj.selectRow) ?  '' : activityObj.selectRow.validTime
                   });


                 $("#saveBtn").off('click').on('click',activityObj.add);
             })
         });

    var TableInit = function () {
        var htmlHeight = $('html')[0].clientHeight;
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
                url: '${pageContext.request.contextPath}/activity/list',         //请求后台的URL（*）
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
                height : htmlHeight,
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
                        switch(value)
                        {
                            case "m":
                                r = "移动";
                              break;
                            case "u":
                              r = "联通";
                              break;
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
                    field: 'createType',
                    title: '活动类型',
                    align : 'center'
                 }, {
                    field: 'duration',
                    title: '观看时长',
                    align : 'center'
                 }, {
                    field: 'point',
                    title: '积分',
                    align : 'center'
                }, {
                    field: 'operatePerson',
                    title: '操作人',
                    align : 'center'
                }, {
                    field: 'lastUpdate',
                    title: '操作时间',
                    align : 'center'
                }
                ],
                rowStyle: function (row, index) {
                    var classesArr = ['success', 'info'];
                    var strclass = "";
                    if (row.statusType == "VALID") {//偶数行
                        strclass = classesArr[0];
                    } else {//奇数行
                        strclass = classesArr[1];
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

/*
    function operateFormatter(value, row, index) {//赋予的参数
        return [
            '<a class="btn active disabled" href="#">编辑</a>',
            '<a class="btn active" href="#">档案</a>',
            '<a class="btn btn-default" href="#">记录</a>',
            '<a class="btn active" href="#">准入</a>'
        ].join('');
    }
*/
    function msgAlert(msg)
    {
        bootbox.alert({
                        size: "small",
                        title: "操作提示",
                        message: msg,
                        callback: function(){ /* your callback code */ }
                    });
    }

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

    var activityObj =
    {
        closeAddModal : function()
        {
            $("#addModal").removeData("bs.modal");
        },
        initAddForm : function()
        {
            $("input").val("");
            $('select').not(".chosen-select").prop('selectedIndex', 0);
            chooseObj.uuid = chooseObj.generateUUID();
            activityObj.selectRow = {};
        },
        addShow : function()
        {
            activityObj.initAddForm();
             // 打开模态框
            $("#addModal").modal();
        },
        add : function()
        {
            var dataType = $("#dataType").val();
            if(!dataType)
            {
                msgAlert("请选择媒资内容");
                return;
            }
            var isSelected = $("#choose-" + dataType).val();
            if(!isSelected)
            {
                chooseObj.showChooseModal(dataType);
                return;
            }
            $("#mediaCode").val(isSelected);
            $("#uuid").val(chooseObj.uuid);
            $.ajax
            ({
                url:'${pageContext.request.contextPath}/activity/add',
                type:'post',
                data:$("#addForm").serialize(),
                dataType:"json",
                success: function(data)
                {
                    if(data.success)
                    {
                        $('#addModal').modal('hide');
                        $('#ArbetTable').bootstrapTable('refresh');
                    }
                    msgAlert(data.message);
                }
            });
        },
        editShow : function()
        {
            var rows = $('#ArbetTable').bootstrapTable('getSelections');
            if(rows.length != 1)
            {
                msgAlert("请选中一行记录操作!");
                return;
            }
            var row = rows[0];
            activityObj.selectRow = row;
            console.info($.isEmptyObject(activityObj.selectRow) ?  '' : activityObj.selectRow.validTime);
            activityObj.initEditForm(row);
        },
        initEditForm : function(row)
        {
            chooseObj.loadColumnCode();
            $("#id").val(row.id);
            $("#activityId").val(row.activityId);
            $("#activityName").val(row.activityName);
            console.info();
            $("#statusType").find("option[value="+row.statusType+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#joinType").find("option[value="+row.joinType+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#dataType").find("option[value="+row.dataType+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#duration").val(row.duration);
            $("#limit").val(row.limit);
            $("#point").val(row.point);
            $("#transmissionMode").find("option[value="+row.transmissionMode+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#mediaCode").val(row.mediaCode);
            $("#choose-" + row.dataType).find("option[value="+row.mediaCode+"]").attr("selected",true);
            chooseObj.uuid = chooseObj.generateUUID();
            $("#addModal").modal();
        },
        search : function()
        {
            $('#ArbetTable').bootstrapTable('refresh');
        },
        selectRow : {}
    }

    var chooseObj =
    {
        chooseStatus : false,
        showChooseModal : function(chooseType)
        {
                switch(chooseType)
                {
                    case "CATEGORY"://栏目
                        $("#CATEGORY-ChooseModal").modal({"backdrop": false});
                        $('#CATEGORY-ChooseModal').on('shown.bs.modal', function (e)
                          {
                            if(!chooseObj.chooseStatus)
                            {
                                $('.chosen-select').chosen(
                                {
                                    no_results_text: "没有找到结果！",//搜索无结果时显示的提示
                              search_contains:true,   //关键字模糊搜索。设置为true，只要选项包含搜索词就会显示；设置为false，则要求从选项开头开始匹配
                             allow_single_deselect:true, //单选下拉框是否允许取消选择。如果允许，选中选项会有一个x号可以删除选项
                            disable_search: false, //禁用搜索。设置为true，则无法搜索选项。
                            disable_search_threshold: 0, //当选项少等于于指定个数时禁用搜索。
                            inherit_select_classes: true, //是否继承原下拉框的样式类，此处设为继承
                            //placeholder_text_single: "请选择栏目",
                            case_sensitive_search: false, //搜索大小写敏感。此处设为不敏感
                            group_search: false, //选项组是否可搜。此处搜索不可搜
                            include_group_label_in_selected: true //选中选项是否显示选项分组。false不显示，true显示。默认false。
                                });
                                    chooseObj.chooseStatus = true;
                            }
                                $(".chosen-select").trigger("chosen:updated");
                          });
                        break;
                    case "TAG"://标签
                        $("#TAG-ChooseModal").modal({"backdrop": false});
                        break;
                    case "COLUMN"://一级分类
                        chooseObj.loadColumnCode();
                        $("#COLUMN-ChooseModal").modal({"backdrop": false});
                        break;
                    case "TYPE"://类型
                        $("#TYPE-ChooseModal").modal({"backdrop": false});
                        break;
                    case "TEXT"://文本导入
                        $("#TEXT-ChooseModal").modal({"backdrop": false});
                        break;
                }
        },
        columnCode : [
                        {code : "001", name : "电影"},
                        {code : "002", name : "电视剧"},
                        {code : "003", name : "新闻"},
                        {code : "004", name : "少儿"},
                        {code : "005", name : "综艺"},
                        {code : "006", name : "体育"},
                        {code : "007", name : "纪实"},
                        {code : "008", name : "生活"},
                        {code : "009", name : "财经"}
                      ],
        loadColumnCode : function()
            {
                var columnContainer = $("#choose-COLUMN");
                if(columnContainer.children("option").length > 1)
                {
                    return;
                }
                var columnCode = this.columnCode;
                for(var i in columnCode)
                {
                    var $op = $("<option></option>");
                    $op.val(columnCode[i].code);
                    $op.text(columnCode[i].name);
                    columnContainer.append($op);
                }
            },
         generateUUID : function()
         {
            var romdomStr = Math.random().toString();
            return new Date().getTime() +  romdomStr.substr(romdomStr.length-2, 2);
         },
         uuid : ''
    }

    </script>
  </body>
</html>