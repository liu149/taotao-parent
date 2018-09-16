package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 处理内容表相关的
 * @title ContentController.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
@Controller
public class ContentController {
	@Autowired
	private ContentService contentservcie;
	
	//	$.post("/content/save",$("#contentAddForm").serialize(), function(data){
	//返回值是JSON
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent tContent){
		//1.引入服务
		//2.注入服务
		//3.调用
		return contentservcie.saveContent(tContent);
	}

	@RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows){
	    EasyUIDataGridResult result = contentservcie.getContentList(categoryId,page,rows);
		return result;
	}

	@RequestMapping(value = "/content/delete",method = RequestMethod.POST)
    @ResponseBody
	public TaotaoResult deleteContentList(String ids){
	    return contentservcie.deleteContentList(ids);
    }
}
