package com.yikuyi.product.category.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.springboot.model.LoginUser;
import com.framewrok.springboot.web.RequestHelper;
import com.yikuyi.category.model.ProductCategory;
import com.yikuyi.category.vo.ProductCategoryChild;
import com.yikuyi.category.vo.ProductCategoryParent;
import com.yikuyi.category.vo.ProductCategoryVo;
import com.yikuyi.product.category.ICategoryResource;
import com.yikuyi.product.category.manager.CategoryManager;
import com.ykyframework.exception.BusinessException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("v1/products/categories")
public class CategoryResource implements ICategoryResource {

	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * 根据父类别ID查询子集类别，没有id查询一级菜单类别
	 * @param parentCateId
	 * @return 子分类列表
	 */
	@Override
	@RequestMapping(value = "/children", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public List<ProductCategory> getChildrenById(@RequestParam(value="parentCateId",required=false)  Integer parentCateId) {
		return categoryManager.getChildrenById(parentCateId);
	}

	
	/**
	 * 根据分类Id获取分类的层级信息
	 * @param ids
	 * @return
	 * 
	 * 
	 * @since 2016年11月22日
	 * @author zr.wenjiao@yikuyi.com
	 */
	@Override
	@ApiOperation(value = "分类层级信息", response = ProductCategoryParent.class, responseContainer = "List")
	@RequestMapping(value ="/batch", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	 public List<ProductCategoryParent> getListByIds(@RequestBody List<Long> ids){
		return categoryManager.getByCategroyId(ids);
	 }

	/**
	 * 根据ID查询分类
	 * @param cateId 类别ID
	 * @return 分类对象
	 */
	@Override
	@RequestMapping(value ="/{cateId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public ProductCategory findById(@PathVariable("cateId") Integer cateId) {
		return categoryManager.findById(cateId);
	}

    /**
     * 查询全部分类
     * @return 
     */
	@Override
	@ApiOperation(value = "查询全部分类", response = ProductCategoryChild.class, responseContainer = "List")
	@RequestMapping(value ="/list", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public List<ProductCategoryChild> getAllCategory(@RequestParam(value="status",required=false)Integer[] status) {
		return categoryManager.findAll(status);
	}


	/**
	 * 更新分类
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping( method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
	public ProductCategory update(@RequestBody ProductCategoryVo category) throws BusinessException {
		LoginUser userInfo = RequestHelper.getLoginUser();
		return categoryManager.update(category,userInfo);
	}
	
	/**
	 * 新增分类
	 * @throws BusinessException 
	 */
	@Override
	@RequestMapping( method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ProductCategoryVo add(@RequestBody ProductCategoryVo category) throws BusinessException {
		LoginUser userInfo = RequestHelper.getLoginUser();
		return categoryManager.add(category,userInfo);
	}

	/**
	 * 导出全部分类，包含所有分类别名
	 * @param resp 网络响应
	 * @since 2017年11月2日
	 * @author tongkun@yikuyi.com
	 * @throws IOException 
	 */
	@ApiOperation(value = "导出全部分类")
	@RequestMapping(value ="/export", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public void exportAllCategory(HttpServletResponse resp) throws IOException{
		resp.setHeader("content-Type", "application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("all-category","UTF-8") + ".xls");
		resp.setCharacterEncoding("UTF-8");
		
		categoryManager.exportAllCategory(resp.getOutputStream());
	}
	
	@Override
	@RequestMapping(value ="/cache", method = RequestMethod.PUT)
	public void initAliasCategoryMap() {
		categoryManager.initAliasCategory();
	}
	
	/**
	 * @param fileUrl
	 * @since 2017年11月3日
	 * @author tongkun@yikuyi.com
	 */
	@ApiOperation(value = "导入分类别名",notes="导入分类别名，原分类别名抛弃掉。文件中不包含的分类不会修改。")
	@RequestMapping(value ="/import", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public List<String> importCategoryAlias(@ApiParam("文件的阿里云路径") @RequestParam(value="fileUrl",required=true)String fileUrl,@ApiParam("文件的原名") @RequestParam(value="oriName",required=false)String oriName){
		return categoryManager.importAllAlias(fileUrl, oriName);
	}
}
