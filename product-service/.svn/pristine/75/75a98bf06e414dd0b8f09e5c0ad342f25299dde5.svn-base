//创建供应商库存。如果该库存已经存在则返回已存在的库存
function (productRawVo,lastNewId){
    //没有提供供应商名称和型号，则返回空
    if(!productRawVo||!productRawVo.skuId||!productRawVo.vendorName)
        return null;
    //根据供应商型号、供应商名称查询sku
    var productVo = productRawVo.sku;
    var isNew = false;
    //查询到则返回
    if(!productVo){
        productVo = {};//新建对象
        productVo._id = lastNewId.toString();
        isNew = true;
    }
//     printjson(productRawVo);
//     printjson(productVo);
    //spu
    if(productRawVo.spu&&productRawVo.spu.length>0){
        productVo.spu=productRawVo.spu[0];
    }
    //没有spu则创建一些基本信息
    else{
        var spu = {};
        if(productRawVo.manufacturer){
            spu.manufacturer = productRawVo.manufacturer;
            spu.manufacturerAgg = productRawVo.manufacturer;
        }
        if(productRawVo.manufacturerPartNumber)
            spu.manufacturerPartNumber = productRawVo.manufacturerPartNumber;
        if(productRawVo.description)
            spu.description = productRawVo.description;
        var rohs = false;
        if(productRawVo.rohs)
            rohs = true;
        spu.rohs = rohs;
        productVo.spu = spu;
    }
    productVo.skuId = productRawVo.skuId;//skuId
    productVo.rawId = productRawVo._id;//保存原先导入的那条id
    //供应商信息
    if(productRawVo.partStatus)
        productVo.partStatus = productRawVo.partStatus;//物料状态
    else
        productVo.partStatus = "Active";
    productVo.vendorName = productRawVo.vendorName;//供应商名称
    if(productRawVo.vendorId)
        productVo.vendorId = productRawVo.vendorId;//供应商id
    else
        productVo.vendorId = "";
    if(productRawVo.vendorDetailsLink)
        productVo.vendorDetailsLink = productRawVo.vendorDetailsLink;//供应商链接
    else
        productVo.vendorDetailsLink = "";
    
    
    //供应商分类
    if(productRawVo.vendorCategories){
        productVo.vendorCategories = [];
        for(var i=0;i<productRawVo.vendorCategories.length;i++){
            var vendorCategorie = productRawVo.vendorCategories[i];
            if(vendorCategorie){
                productVo.vendorCategories.push(vendorCategorie);
            }
        }
    }
    else
        productVo.vendorCategories = [];
    
    //系列
    if(productRawVo.vendorSeries){
        productVo.vendorSeries = productRawVo.vendorSeries;
    }
    if(productRawVo.packaging)
        productVo.packaging = productRawVo.packaging;
    if(productRawVo.countryCode)
        productVo.countryCode = productRawVo.countryCode;//国家码
    else
        productVo.countryCode = "";
    //库存信息
    //库存数量
//     productVo.stocks = [];
    var qty = 0;//现货库存数量
    if(productRawVo.stocks){
        for(var i = 0;i < productRawVo.stocks.length;i++){
            var stock = productRawVo.stocks[i];
            if(stock){
                if(stock.source==100){
                    qty = NumberInt(stock.quantity);
                }
//                 productVo.stocks.push(stock);
            }
        }
    }
    productVo.qty = qty;//数量加和
    productVo.sourceId='digikey-100';//仓库id
    productVo.leadTime=NumberInt(0);
    //单位
    if(productRawVo.unit)
        productVo.unit = productRawVo.unit;
    //价格
    var moq = 1;
    var cnyPrice = {"currencyCode":"CNY"};
    var usdPrice = {"currencyCode":"USD"};
    if(productRawVo.prices){
        for(var i = 0;i < productRawVo.prices.length;i++){
            var price = productRawVo.prices[i];
            var newPrice = {};
            if(price.unitPrice||price.unitPrice==0)
                newPrice.unitPrice = price.unitPrice;
            if(price.priceLevels){
                var levels = [];
                for(var j = 0;j < price.priceLevels.length;j++){
                    var level = price.priceLevels[j];
                    if(level){
                        levels.push(level);
                        if(moq>level.breakQuantity)
                            moq = level.breakQuantity;
                    }
                }
                newPrice.priceLevels = levels;
            }
            if(price.currencyCode&&price.currencyCode=='CNY'){
                cnyPrice = newPrice;
                cnyPrice.currencyCode = "CNY";
            }else if(price.currencyCode&&price.currencyCode=='USD'){
                usdPrice = newPrice;
                usdPrice.currencyCode = "USD";
            }else{
                continue;
            }
        }
    }
    //如果已有数据价格为0则不更新
    if(!isNew&&usdPrice.unitPrice==0){
        return null;
    }
    var prices = [cnyPrice,usdPrice];
    productVo.prices = prices
    productVo.minimumQuantity = moq;
    
    productVo.storeId = 99999999;
    //计算快速查找码
    var brand = productVo.spu.manufacturer;
    if(!brand)brand = "";
    productVo.quickFindKey = md5(productVo.skuId+brand+"100");
    
    return productVo;
}