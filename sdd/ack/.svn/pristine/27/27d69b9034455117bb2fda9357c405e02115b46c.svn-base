<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title">新增活动</h4>
  </div>
  <div class="modal-body">
    <form id="addForm">
      <div class="form-group">
        <label for="id">活动ID</label>
        <input type="text" class="form-control" id="activity-id" name="id" placeholder="请填写活动id">
      </div>
      <div class="form-group">
        <label for="effectiveTime">生效时间</label>
        <input type="text" class="form-control activity-date" id="activity-effectiveTime"
                name="effectiveTime" placeholder="请选择生效时间">
      </div>
      <div class="form-group">
        <label for="failureTime">失效时间</label>
        <input type="text" class="form-control activity-date" id="activity-failureTime"
                name="failureTime" placeholder="请选择失效时间">
      </div>
       <div class="form-group">
              <label for="status">状态</label>
              <select name="status" class="form-control">
                <option value="EFFECTIVE" selected="selected">有效</option>
                <option value="INVALID" >无效</option>
              </select>
        </div>
        <div class="form-group">
              <label for="sourceType">影片来源</label>
              <select name="sourceType" class="form-control">
                <option value="CP" selected="selected">cp</option>
                <option value="COLUMN" >栏目</option>
                <option value="TEXT_IMPORT" >文本导入</option>
              </select>
        </div>
        <div class="form-group">
            <label for="watchTime">观看时长</label>
            <input type="text" class="form-control" id="activity-watchTime" name="watchTime" placeholder="请填写观看时长">
        </div>
        <div class="form-group">
            <label for="integral">积分</label>
            <input type="text" class="form-control" id="activity-integral" name="integral" placeholder="请填写积分">
        </div>
        <div class="form-group">
              <label for="dataTransmissionType">数据传输方式</label>
              <select name="dataTransmissionType" id="dataTransmissionType" class="form-control">
                <option value="FTP" selected="selected">ftp</option>
                <option value="INTERFACE" >接口</option>
              </select>
        </div>
    </form>
  </div>
  <div class="modal-footer">
    <button type="button" id="resetBtn" class="btn btn-default" >重置</button>
    <button type="button" id="saveBtn" class="btn btn-primary">提交</button>
  </div>
