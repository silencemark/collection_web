function FormDataAjax(dataUrl,data,successCall){
	var oMyForm = new FormData();     //创建formdata
	var blobBin = dataURLtoBlob(dataUrl);     //base64转换blob
	oMyForm.append("upload", blobBin,data.fileName);    //想formData添加blob数据
	
	var USER = JSON.parse(localStorage.GlobalData).userinfo;
	oMyForm.append("userid",USER.userid);
	oMyForm.append("type",data.type);
	oMyForm.append("FileDirectory",USER.companyid)
	$.ajax({
	        url:projectpath+"/file/upload",
	        type: "POST",
	        data: oMyForm,
	        async: false,
	        cache: false,
	        contentType: false,
	        processData: false,
	        success: function (msg) {
	        	successCall(msg);
	        }
	});

	//**dataURL to blob**  dataURL转换blob
	function dataURLtoBlob(dataurl) {
	   var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	   bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	   while (n--) {
	        u8arr[n] = bstr.charCodeAt(n);
	   }
	   return new Blob([u8arr], { uploadContentType: mime });
	}
} 