//创建标准数据对象，并返回数据。如果该型号品牌已经存在，则返回存在的数据
function (productRawVo,cateMap,brandMap,lastNewId){
    //如果没有输入品牌，型号，则直接退出
    if(!productRawVo||!productRawVo.manufacturerPartNumber||productRawVo.noneed)
        return null;
    //如果不允许创建则直接返回
    if(productRawVo.cantCreate){
        return null;
    }
    var brandVo = null;//品牌
    if(productRawVo.manufacturer){
        brandVo = brandMap[productRawVo.manufacturer.toUpperCase()];//查询制造商
    }
    //如果是标准供应商，则将信息保存到标准库中
    productStandVo = {};//创建对象
    productStandVo._id = lastNewId.toString();
    if(brandVo){
        productStandVo.manufacturer = brandVo.brandName;//制造商名称
        productStandVo.manufacturerId = brandVo._id;//制造商id
        productStandVo.manufacturerAgg = productStandVo.manufacturer;
    }
    else{
        productStandVo.manufacturer = productRawVo.manufacturer;//制造商名称
        productStandVo.manufacturerAgg = null;
    }//制造商名称（汇聚用）
    productStandVo.manufacturerPartNumber = productRawVo.manufacturerPartNumber;//型号
    if(productRawVo.description)
        productStandVo.description = productRawVo.description;//标准描述
    else
        productStandVo.description = "";
    var rohs = false;
    if(productRawVo.rohs)
        rohs = productRawVo.rohs;
    productStandVo.rohs = rohs;
    
    productStandVo.categories = [];//标准分类数据
    if(productRawVo.vendorCategories){
        var searchCates = searchProductCategory(productRawVo.vendorCategories,cateMap);//搜索分类
        //如果有这个分类则创建关联
        if(searchCates){
            productStandVo.categories = searchCates;
        }
    }
    //文档数据
    if(productRawVo.documents){
        productStandVo.documents = [];
        for(var i = 0;i < productRawVo.documents.length;i++){
            var doc = productRawVo.documents[i];
            if(doc&&doc.type&&doc.url){
                productStandVo.documents.push(doc);
            }
        }
    }
    //图片数据
    if(productRawVo.images){
        productStandVo.images = [];
        for(var i = 0;i < productRawVo.images.length;i++){
            var img = productRawVo.images[i];
            if(img&&img.type&&img.url&&img.url!='//media.digikey.com/Photos/NoPhoto/pna_en_tmb.jpg'){
                productStandVo.images.push({
                    type:img.type,
                    url:img.url
                });
            }
        }
    }
    //参数
    if(productRawVo.parameters){
        productStandVo.parameters = [];
        for(var i = 0;i < productRawVo.parameters.length;i++){
            var param = productRawVo.parameters[i];
            if(param&&param.code&&param.name&&param.value){
                productStandVo.parameters.push({
                    code:param.code,
                    name:param.name,
                    value:param.value
                });
            }
        }
    }
    //返回创建的对象
    return productStandVo;
}