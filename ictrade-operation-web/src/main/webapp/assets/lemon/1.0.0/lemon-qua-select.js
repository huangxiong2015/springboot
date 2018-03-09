var imagesRoot = 'images/';

Vue.component('qua-select', {
    template: `<div id="qual" class="ui-qual">
        <div class="input-group">
            <label class="input-text"><span class="must">*</span>公司注册地：</label>
            <div class="input-content qual-type ui-select">
                <select class="form-control ui-select-list" v-model="selectedRegAddr">
                    <option class="ui-select-item" v-for="item in initData.regAddrList" :value="item.code">{{ item.text }}</option>
                </select>
            </div>
        </div>
        <div v-if="this.selectedRegAddr == '0'" class="input-group dalu_type" style="display: table;">
            <label class="input-text"><span class="must">*</span>营业执照类型：</label>
            <div class="input-content">
                <div class="radio-item" v-for="item in initData.busilisTypeList" @click="changeBusliType($event)"><input type="radio" name="a" :value="item.code"> <i :class="['radio', {'radio-selected': item.selected}]"></i>{{ item.text }}</div>
            </div>
        </div>
        <div class="qual-list">
            <div class="input-group qual-item" v-for="(item, index) in currentQuaList">
                <label class="input-text"><span class="must">*</span>{{ item.quaType }}：</label>
                <div class="input-content">
                    <div class="uploadImg">
                        <img :id="'img'+(index+1)" :class="[{'img': imgSrc(index) && imgSrc(index).indexOf('.pdf')==-1}, {'pdf': imgSrc(index).indexOf('.pdf')!=-1}]" data-src="" :src="imgSrc(index) | translateImage" @click="previewImg($event, index)"/>
                        <button :id="'btn'+(index+1)" class="upload-btn">点此上传</button>
                    </div>
                    <div class="uploadCz">
                        <p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br /> 上传的文件需加盖公司公章或财务章</p>
                        <span v-if="item.showRef" class="ck-btn" @click="layerPic(item.refsImgSrc)">参考样本</span>
                    </div>
                </div>
            </div>
        </div>
    </div>`,
    props: {
        busilisType: String,
        regAddr: String,
        busiPic: String,
        taxPic: String,
        oocPic: String,
        busiPdfName: String,
        taxPdfName: String,
        oocPdfName: String,
        fileSize: Number
    },
    data: function () {
        return  {
            localBusilisType: this.busilisType || '3-TO-1',     //三证类型
            selectedBusLiType: '',          //营业执照类型
            selectedRegAddr: this.regAddr || '0',   //注册地区
            zzData: {
                busiPic: this.busiPic || '',
                taxPic: this.taxPic || '',
                oocPic: this.oocPic || '',
                busiPdfName: this.busiPdfName || '',
                taxPdfName: this.taxPdfName || '',
                oocPdfName: this.oocPdfName || ''
            },
            uploaderArr: [],	//uploader对象集合
            initData: {
                regAddrList: [
                    {text: '大陆', code: '0'},
                    {text: '香港', code: '1'},
                    {text: '台湾', code: '2'},
                    {text: '境外', code: '3'}
                ],
                busilisTypeList: [
                    {text: '三证合一营业执照（自2015年10月1日起登记机关颁发的含统一信用代码的营业执照）', code: '3-TO-1', selected: true},
                    {text: '普通营业执照', code: 'COMMON'}
                ],
                quaList: {
                    '3-TO-1': [{
                        quaType: '营业执照',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/dl-busiLic.jpg'
                    }],
                    'COMMON': [{
                        quaType: '营业执照',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/dl-busiLic.jpg'
                    },{
                        quaType: '税务登记证',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/dl-taxReg.jpg'
                    },{
                        quaType: '组织机构代码证',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/dl-occ.jpg'
                    }],
                    'HK-CODE': [{
                        quaType: '注册证书(CR)',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/hk-CR.jpg'
                    },{
                        quaType: '商业登记证(BR)',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/hk-BR.jpg'
                    }],
                    'TW-CODE': [{
                        quaType: '盈利事業登記證',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/tw-busiLic.jpg'
                    },{
                        quaType: '稅籍登記證',
                        img: '',
                        showRef: false
                    }],
                    'ABROAD-CODE': [{
                        quaType: 'CERTIFICATE OF INCORPORATION',
                        img: '',
                        showRef: true,
                        refsImgSrc: imagesRoot + 'sanzheng/jw-limited.jpg'
                    }]
                }
            }

        }
    },
    computed: {
        currentQuaList: function () {
            return this.initData.quaList[this.localBusilisType];
        }
    },
    watch: {
        'selectedRegAddr': function (newValue) {
            this.localBusilisType = ['3-TO-1', 'HK-CODE', 'TW-CODE', 'ABROAD-CODE'][newValue];
            if(this.selectedRegAddr == '0') {
                this.selectedBusLiType = '3-TO-1';
            }
            
            this.$emit('zz-data-change', 'regAddr', newValue);
        },
        'selectedBusLiType': function (newValue) {
            if(this.selectedRegAddr == '0') {
                this.localBusilisType = newValue;
            }
        },
        'localBusilisType': function() {
            this.$emit('zz-data-change', 'busilisType', this.localBusilisType);
        },
        'currentQuaList': function () {
            var _this = this;

            this.uploaderArr.forEach(function (uploader) {
                uploader.destroy();
            })
            this.$nextTick(function () {
                _this.uploaderArr = [];
                _this.uploadInit();
            })

            this.zzData = {
                busiPic: '',
                taxPic: '',
                oocPic: '',
                busiPdfName: '',
                taxPdfName: '',
                oocPdfName: ''
            }
        },
        'zzData.busiPic': function(newVal) {
        	this.$emit('zz-data-change', 'busilicPic', newVal);
        },
        'zzData.taxPic': function(newVal) {
        	this.$emit('zz-data-change', 'taxPic', newVal);
        },
        'zzData.oocPic': function(newVal) {
        	this.$emit('zz-data-change', 'oocPic', newVal);
        },
        'zzData.busiPdfName': function(newVal) {
        	this.$emit('zz-data-change', 'busiPdfName', newVal);
        },
        'zzData.taxPdfName': function(newVal) {
        	this.$emit('zz-data-change', 'taxPdfName', newVal);
        },
        'zzData.oocPdfName': function(newVal) {
        	this.$emit('zz-data-change', 'oocPdfName', newVal);
        }
    },
    mounted: function () {
        this.uploadInit();
        if(this.regAddr && this.busilisType == 'COMMON'){
            $('.radio-item .radio').removeClass('radio-selected');
            $('.radio-item .radio').eq(1).addClass('radio-selected');
        }
    },
    filters: {
        //图片转码
        translateImage: function (imageUrl) {
            var url = imageUrl ? imageUrl : '';

            if(url == '') return '';

            if(imageUrl.indexOf('.pdf') != -1) {
                return ykyUrl._this + '/images/pdf_icon.png';
            }

            //获取图片公共方法
            $.aAjax({
                url: ykyUrl.party + "/v1/enterprises/getImgUrl",
                type: "POST",
                async: false,
                data: JSON.stringify({
                    "id": imageUrl
                }),
                success: function(data) {
                    if (data !== "") {
                        url = data;
                    }
                }
            });
            return url;
        }
    },
    methods: {
        getPrevViewImage: function (imageUrl) {
        	
        	if(imageUrl == '') return '';
        	
            $.aAjax({
                url: ykyUrl.party + "/v1/enterprises/getImgUrl",
                type: "POST",
                async: false,
                data: JSON.stringify({
                    "id": imageUrl
                }),
                success: function(data) {
                    if (data !== "") {
                        url = data;
                    }
                }
            });
            return url;
        },
        /*改变营业执照类型回调事件*/
        changeBusliType: function (event) {
            var target = $(event.target);
            this.selectedBusLiType = target.closest('.radio-item').find('input[type=radio]').val();
            if(!$(target).find('.radio').hasClass('radio-selected')){
                $('.radio-item .radio').toggleClass('radio-selected');
            }
        },
        /*通过index可以拿到图片地址*/
        imgSrc: function (index) {
            var picNameList = ['busiPic', 'taxPic', 'oocPic'];
            var curPicName = picNameList[index];
            return this.zzData[curPicName];
        },
        /*所有图片上传初始化*/
        uploadInit: function() {

            var currentQuaList = this.currentQuaList;
            var _this = this;
            
            var setImgSrc = function(index) {
                var picNameList = ['busi', 'tax', 'ooc'];
                var curPicName = picNameList[index];
                var imgSrc = _this.zzData[curPicName + 'Pic'];
                $('#img' + (index+1)).attr('data-src', _this.getPrevViewImage(imgSrc));
            }

            var initUpload = function (index) {
                var uploader = createUploader({
                    buttonId: 'btn' + (index + 1),
                    uploadType: 'ent.certificates',
                    url:  ykyUrl.webres,
                    types: 'jpg,jpeg,bmp,png,pdf,PDF,gif',
                    fileSize:  _this.fileSize + 'm',
                    isImage: true,
                    init:{
                        FileUploaded : function(up, file, info) {
                            layer.closeAll();
                            if (info.status == 200 || info.status == 203) {
                                var imageUrl = signatureData["imageUrl"];
                                var isPdf = (imageUrl.toLowerCase().indexOf('.pdf') != -1)	//是否是pdf

                                var picNameList = ['busi', 'tax', 'ooc'];
                                var curPicName = picNameList[index];

                                _this.zzData[curPicName + 'Pic'] = imageUrl;
                                if(isPdf) {
                                    _this.zzData[curPicName + 'PdfName'] = file.name;
                                }else {
                                    _this.zzData[curPicName + 'PdfName'] = '';
                                }

                                setImgSrc(index);

                            } else {
                                layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
                            }
                            up.files=[];
                        }
                    }
                })
                _this.uploaderArr.push(uploader);
                uploader.init();
            }

            for(var i=0; i<currentQuaList.length; i++) {
                initUpload(i);
                setImgSrc(i);
            }
        },
        //预览大图
        previewImg: function (event, index) {
            var target = $(event.target);
            var className = target.attr('class');
            var picNameList = ['busiPic', 'taxPic', 'oocPic'];
            var curPicName = picNameList[index];
            var prevViewImage = $('#img' + (index+1)).attr('data-src');

            if(className == 'pdf') {
                window.open(prevViewImage);
            }else {
                this.layerPic(prevViewImage);
            }

        },
        layerPic: function (prevViewImage) {
        	
            var winW = $(window).width() * 0.9;
            var winH = $(window).height() * 0.9;
            var winScale = winW / winH;
            var imgW = 'auto';
            var imgH = 'auto';
            
            var imgObj = new Image();
            imgObj.src = prevViewImage;
            imgObj.onload = function() {
            	imgW = this.width;
            	imgH = this.height;
            	var scale = imgW / imgH;
            	
            	if(imgW > winW && imgH < winH) {
            		imgW = winW + 'px';
            		imgH = 'auto';
            	}else if(imgH > winH && imgW < winW) {
            		imgH = winH + 'px';
            		imgW = 'auto';
            	}else if(imgW > winW && imgH > winH) {
            		
            		if(scale > 1) {
            			if(scale > winScale) {
                			imgW = winW + 'px';
                			imgH = 'auto';
            			}else {
            				imgH = winH + 'px';
                			imgW = 'auto';
            			}
            		}else {
            			if(scale < winScale) {
                			imgH = winH + 'px';
                			imgW = 'auto';
            			}else {
                			imgW = winW + 'px';
                			imgH = 'auto';
            			}
            		}
            	}
            	
            	layer.open({
                    type : 1,
                    title : false,
                    closeBtn : 0,
                    area: [imgW, imgH],
                    skin : 'layer_cla', // 没有背景色
                    shadeClose : true,
                    content : '<img style="width:'+ imgW +';height:'+ imgH +'" src="'+ prevViewImage +'">'
                });
            }
        }
    }
});