<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>测试报告-汇总</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap -->
    <link href="${projectDomain}/static/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${projectDomain}/static/css/dashboard.css" rel="stylesheet" media="screen">
</head>
<body>
<div class="container-fluid small">
    <h2 class="sub-header mt-4"><span class="text text-info">接口自动化周报${dateRange}</span></h2>
    <table class="table table-striped mt-5">
        <thead>
        <tr>
            <th>项目ID</th>
            <th>项目名称</th>
            <th>对应测试</th>
            <th>对应开发</th>
            <th>自动执行次数（成功数|失败数）</th>
            <th>失败原因（失败数|已修复数）</th>
            <th>接口目标数|完成数|完成进度</th>
            <th>质量分</th>
            <th>上线测试任务总数|成功数|通过率</th>
        </tr>
        </thead>
        <tbody>
        <#list list as o>
            <tr>
                <td>${o.projectId}</td>
                <td><span class="text text-point">${o.projectName!''}</span></td>
                <td><#list o.testOwnerList as owner><span class="badge badge-pill badge-warning" style="color:dark;background-color:pink">${owner.name}</span> </#list></td>
                <td><#list o.devOwnerList as owner><span class="badge badge-pill badge-primary">${owner.name}</span> </#list></td>
                <td><span class="text text-primary text-point">${o.runTotalCount}</span> (<span class="text text-success text-point">${o.runSuccessCount}</span> | <span class="text text-danger text-point">${o.runFailCount}</span>)</td>
                <td><#if o.failInfoList??><#list o.failInfoList as fail><div class="mt-1"><span class="text text-primary text-point">${fail.failReason}</span> (<span class="text text-danger text-point">${fail.failNum} </span> | <span class="text text-success text-point"><#if fail.needFixFlag>${fail.fixNum}<#else>无需修复</#if></span>)</div></#list></#if></td>
                <td>${o.apiTargetCount} | ${o.apiFinishCount} | <span class="text text-point text-green"><#if o.apiFinishRate??>${o.apiFinishRate?string("#.##%")}<#else>-</#if></span></td>
                <td><#if o.qualityScore??>${o.qualityScore}<#else>-</#if></td>
                <td>${o.onlineTestCasesCount} | ${o.onlineTestCasesSuccessCount} | <span class="text text-point"><#if o.onlineTestCasesPassRate??>${o.onlineTestCasesPassRate?string("#.##%")}<#else>-</#if></span></td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<script src="${projectDomain}/static/js/jquery.js"></script>
<script src="${projectDomain}/static/js/bootstrap.min.js"></script>
</body>
</html>