function() {
    var cates = db.product_category.find({});
    var cateMap = {};
    var idMap = {};
    var aMap = {};
    while(cates.hasNext()){
        var cate = cates.next();
        if(cate.cateAlias){
            for(var i = 0;i < cate.cateAlias.length;i++){
                var key = '';
                key=key+(cate.cateAlias[i].level1?cate.cateAlias[i].level1:'');
                key=key+cate.cateAlias[i].level2;
                aMap[key] = cate;
            }
        }
        idMap[cate._id] = cate;
    }
    cateMap.idMap = idMap;
    cateMap.aMap = aMap;
    return cateMap;
}