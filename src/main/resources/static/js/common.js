/**
 * 你的名字
 */
function ajax_upload(fileId,imgId,urlId){
	//表单对象 html5新特性
	var formData = new FormData();
	
	//参数名称：file
	formData.append("file", document.getElementById(fileId).files[0]);
	
	//上传
	$.ajax({
		url : '/upload',
		type : 'POST',
		cache : false, //上传文件不需要缓存
		data : formData,
		processData : false, // 告诉jQuery不要去处理发送的数据
		contentType : false, // 告诉jQuery不要去设置Content-Type请求头
		success : function(data) {
			//后端返回的字符串：失败就是fail，成功了就是 图片的url链接地址
			if (data == "fail") {
				alert("上传失败");
			} else {
				//图片回显
				document.getElementById(imgId).src=data;
				//保存url
				document.getElementById(urlId).value=data;
				//调试用的
				alert(data);
			}
		}
	})
}