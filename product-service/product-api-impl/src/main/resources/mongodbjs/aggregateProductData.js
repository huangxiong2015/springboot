function(pageMax,lastId,cateMap,brandMap) {
    var lastDate = Date.now();
    var lastNewId = IdGen();
    var param = {};
    if(lastId){
         param._id={$gt:lastId};
    }
    var productRaws = db.digikey_temp.find(param).sort({_id:1}).limit(pageMax);//查询所有产品
    //组装插入的sku数组
    var skuArray = [];//录入的的sku数组
    var skuSearchArray = [];//查找用的sku数组
    var skuMap = {};//录入的sku的映射
    var spuSearchArrayForSku = [];//spu映射
    var spuSearchMapForSku = {};//spu映射
    //组装插入的spu数组
    var spuArray = [];//查找用的数组
    var spuSearchArray = [];//搜索用的数组，与查找用的数组index要一样
    var spuMap = {};//index映射
    var index = 0;
    var indexSku = 0;
    var raws = [];
    while (productRaws.hasNext()) {
        var productRawVo = productRaws.next();//product游标
        if(productRawVo.packaging&&productRawVo.packaging=='Digi-Reel®'){
            //收尾
            index++
            if(!productRaws.hasNext()){
                lastId = productRawVo._id;
            }
            continue;
        }
        raws.push(productRawVo);
        //组装spu数组
        if(productRawVo.manufacturerPartNumber){
            spuArray.push(productRawVo.manufacturerPartNumber);//查找用的数组增加
            spuSearchArray.push(productRawVo);//搜索用的数组增加
            //映射index增加
            var spuIndexArr = spuMap[productRawVo.manufacturerPartNumber];
            if(spuIndexArr==null){
                spuIndexArr = [];
                spuMap[productRawVo.manufacturerPartNumber] = spuIndexArr;
            }
            spuIndexArr.push(index++);//映射index
        }
        //组装sku数组
        if(productRawVo.skuId){
            skuArray.push(productRawVo);
            skuSearchArray.push(productRawVo.skuId);
            skuMap[productRawVo.skuId]=indexSku++;
        } 
        //收尾
        index++
        if(!productRaws.hasNext()){
            lastId = productRawVo._id;
        }
    }
    //过滤掉已有的spu数组
    var saveSpu = [];
    var spuIns = db.product_stand.find({"manufacturerPartNumber":{$in:spuArray}});
    index = 0;//接下来用于保存sku的index
    //不需要插入的spu列表
    while(spuIns.hasNext()){
        var inO = spuIns.next();
        var spuIndexArr = spuMap[inO.manufacturerPartNumber];
        for(var j=0;j<spuIndexArr.length;j++){
            if(spuSearchArray[spuIndexArr[j]]){
                spuSearchArray[spuIndexArr[j]].spu = [];
                spuSearchArray[spuIndexArr[j]].spu.push(inO);
                spuSearchArray[spuIndexArr[j]].noneed=true;
            }
        }
    }
    //要插入的spu列表
    var indexSpu = 0;
    for(var i = 0;i < spuSearchArray.length;i++){
        var opObj = createProductStand(spuSearchArray[i],cateMap,brandMap,lastNewId++);
        if(opObj){
            spuSearchArray[i].spu = [];
            spuSearchArray[i].spu.push(opObj);
            //查找重复
            var deu = false;
            for(var j=0;j<saveSpu.length;j++){
                if(opObj.manufacturerPartNumber==saveSpu[j].insertOne.document.manufacturerPartNumber){
                    deu = true;
                    break;
                }
            }
            spuSearchArrayForSku.push(opObj);
            spuSearchMapForSku[opObj.manufacturerPartNumber] = indexSpu++;
            //如果不重复才会插入
            if(!deu)
                saveSpu.push({"insertOne":{"document":opObj}});//保存spu
        }
    }
    //过滤spu、组装sku完毕
    //回查已有的sku数组
    var skuNum = 0;
    var saveSku = [];
    var skuIns = db.product.find({"skuId":{$in:skuSearchArray}});
    //组装要保存的sku数组
    while(skuIns.hasNext()){
        var inO = skuIns.next();
        var skuRaw = skuArray[skuMap[inO.skuId]];
        if(!skuRaw){
//             print((skuNum++)+" deu:"+inO.skuId);
            continue;
        }
        skuRaw.sku = inO;
        var spu = spuSearchArrayForSku[spuSearchMapForSku[skuRaw.manufacturerPartNumber]];
        if(spu){
            skuRaw.spu = [];
            skuRaw.spu.push(spu);
        }
//         printjson(skuRaw.spu);
        var opObj = createProduct(skuRaw,lastNewId++);
        //不创建则跳过
        if(opObj){
            saveSku.push({"replaceOne":{"filter":{"_id":opObj._id},"replacement":opObj}});//保存spu
        }
//         print((skuNum++)+" rep:"+opObj.skuId);
        //已有的数据不再更新
        for(var i = 0;i < skuArray.length;i++){
            if(skuArray[i]!=null&&skuArray[i].skuId == inO.skuId){
                skuArray[i] = null;
            }
        }
    }
    //要插入的sku列表
    for(var i = 0;i < skuArray.length;i++){
        var skuRaw = skuArray[i];
        if(!skuRaw)continue;//由于已经替换了则不需要再插入
        var spu = spuSearchArrayForSku[spuSearchMapForSku[skuRaw.manufacturerPartNumber]];
        if(spu){
            skuRaw.spu = [];
            skuRaw.spu.push(spu);
        }
        var opObj = createProduct(skuRaw,lastNewId++);
        saveSku.push({"insertOne":{"document":opObj}});//保存spu
        //后面有相同的不再更新
        for(var j = i+1;j < skuArray.length;j++){
            if(skuArray[j]!=null&&skuArray[j].skuId == skuRaw.skuId){
                skuArray[j] = null;
            }
        }
//         print((skuNum++)+" insert:"+opObj.skuId);
    }
    //保存过滤后的spu数据
    if(saveSpu.length>0){
        db.product_stand.bulkWrite(saveSpu);
    }
    if(skuArray.length>0){
        db.product.bulkWrite(saveSku);
    }
    return lastId;
}