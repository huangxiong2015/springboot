function() {
    var brands = db.product_brand.find({});
    var brandMap = {};
    while(brands.hasNext()){
        var brand = brands.next();
        if(brand.brandAlias){
            for(var i = 0;i < brand.brandAlias.length;i++){
                brandMap[brand.brandAlias[i]] = brand;
            }
        }
    }
    return brandMap;
}