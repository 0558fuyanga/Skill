<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>秒杀</title>
<script type="text/javascript" src="/js/jquery-1.8.3.min.js"></script>
<style type="text/css">
table {
	border-collapse: collapse;
	margin: 0 auto;
	text-align: center;
}

table td, table th {
	border: 1px solid #cad9ea;
	color: #666;
	height: 30px;
}

table thead th {
	background-color: #CCE8EB;
	width: 100px;
}

table tr:nth-child(odd) {
	background: #fff;
}

table tr:nth-child(even) {
	background: #F5FAFA;
}
</style>

</head>
<body>
	<div
		style="float: left; width: 500px; text-align: center; margin: 0 auto;">
		<h3>秒杀后台管理</h3>
		<div style="margin-bottom: 50px">
			<a href="javascript:initData();">初始化商品数据</a>
		</div>
		<div style="margin-bottom: 30px">
			<a href="javascript:loadStock();">预热商品库存数据</a>
		</div>
		<div style="margin-bottom: 30px">
			<a href="javascript:clearCache();">清除本地缓存数据</a>
		</div>
		<h3>测试结果信息</h3>
		<div>
			<a href="javascript:report();">获取测试结果数据</a>
		</div>
		<table id="report" style="width: 100%">
			<tr>
				<td id="stockReport"></td>
			</tr>
			<tr>
				<td id="orderReport"></td>
			</tr>
		</table>
		<script type="text/javascript">
				function report(){
					$.ajax({
						url : '/admin/skill/report',
						type : 'get',
						data: {'productId':${p.id }},
						success : function(data) {
							if (data == 'error') {
								alert('失败')
							} else{
								document.getElementById('stockReport').innerText="商品库存："+data.productStock;
								document.getElementById('orderReport').innerText="下订单数："+data.orderCount;
							}
						}
					})
				}
			</script>
	</div>

	<div style="margin-top: 20px; float: left;width: 500px; text-align: center; margin: 0 auto;">
		<h3>秒杀商品信息</h3>
		<form action="/skill" method="post">
			<input type="hidden" name="productId" value="${p.id }" /> <input
				type="hidden" name="userId" value="1" />
			<table style="width: 100%">
				<tr>
					<td>名称：${p.productName }</td>
				</tr>
				<tr>
					<td>价格：${p.price }元</td>
				</tr>
				<tr>
					<td>库存：${p.stock }</td>
				</tr>
				<tr>
					<td><input type="button" value="秒杀" onclick="skill()" /></td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
				function initData(){
					$.ajax({
						url : '/admin/skill/init',
						type : 'get',
						success : function(data) {
							if (data == 'ok') {
								location.href='/skill'
							} else{alert('失败')}
						}
					})
				}
				function skill(){
					$.ajax({
						url : '/skill',
						type : 'post',
						data: $('form').serialize(),
						success : function(data) {
							if (data == 'ok') {
								alert('秒杀成功')
								location.href='/skill'
							} else if(data=='not enough'){
								alert('库存不足')
							}else{alert('失败')}
						}
					})
				}

				function loadStock(){
					$.ajax({
						url : '/admin/skill/load/stock',
						type : 'get',
						success : function(data) {
							if (data == 'ok') {
								location.href='/skill'
							} else{alert('失败')}
						}
					})
				}

				function clearCache(){
					$.ajax({
						url : '/admin/skill/cache/clear',
						type : 'get',
						success : function(data) {
							if (data == 'ok') {
								location.href='/skill'
							} else{alert('失败')}
						}
					})
				}
			</script>
	</div>
</body>
</html>